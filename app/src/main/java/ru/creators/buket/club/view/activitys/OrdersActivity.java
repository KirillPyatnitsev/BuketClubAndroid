package ru.creators.buket.club.view.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import ru.creators.buket.club.DataController;
import ru.creators.buket.club.R;
import ru.creators.buket.club.model.Order;
import ru.creators.buket.club.model.lists.ListOrder;
import ru.creators.buket.club.view.adapters.ListOrderAdapter;
import ru.creators.buket.club.web.WebMethods;
import ru.creators.buket.club.web.response.OrdersResponse;

public class OrdersActivity extends BaseActivity {

    private ListOrder listOrder = new ListOrder();
    private ListOrderAdapter listOrderAdapter;

    private ListView listView;

    private ImageView imageBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);
        assignView();
        assignListener();
        initView();
        getOrders();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getOrders();
    }

    private void assignView(){
        listView = getViewById(R.id.a_o_list_view_orders);
        imageBack = getViewById(R.id.i_ab_image_back);
    }

    private void assignListener(){
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                DataController.getInstance().setOrder(listOrder.get(position));

                if (listOrder.get(position).getStatusIndex() == Order.STATUS_DELIVERED_INDEX)
                    goToReviewActivity();
                else
                    goToOrderDetalisActivity();

            }
        });

        imageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToBouquetsActivity();
            }
        });
    }

    private void goToBouquetsActivity(){
        startActivity(new Intent(this, BucketsActivity.class));
    }

    private void initView(){
        imageBack.setVisibility(View.VISIBLE);
        listOrderAdapter = new ListOrderAdapter(this, listOrder);
        listView.setAdapter(listOrderAdapter);
    }

    @Override
    protected int getCoordinatorViewId() {
        return R.id.a_o_coordinator_root;
    }

    private void getOrders(){
        startLoading(false);
        WebMethods.getInstance().getOrders(
                DataController.getInstance().getSession().getAccessToken(),
                new RequestListener<OrdersResponse>() {
                    @Override
                    public void onRequestFailure(SpiceException spiceException) {
                        stopLoading();
                    }

                    @Override
                    public void onRequestSuccess(OrdersResponse ordersResponse) {
                        stopLoading();
                        listOrder.clear();
                        listOrder.addAll(ordersResponse.getListOrder());
                        listOrderAdapter.notifyDataSetChanged();
                    }
                });
    }

    private void goToOrderDetalisActivity(){
        startActivity(new Intent(this, OrderDetalisActivity.class));
    }

    private void goToReviewActivity(){
        startActivity(new Intent(this, ReviewActivity.class));
    }
}
