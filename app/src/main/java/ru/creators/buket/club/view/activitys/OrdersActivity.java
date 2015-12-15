package ru.creators.buket.club.view.activitys;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import ru.creators.buket.club.DataController;
import ru.creators.buket.club.R;
import ru.creators.buket.club.model.lists.ListOrder;
import ru.creators.buket.club.view.adapters.ListOrderAdapter;
import ru.creators.buket.club.web.WebMethods;
import ru.creators.buket.club.web.response.OrdersResponse;

public class OrdersActivity extends BaseActivity {

    private ListOrder listOrder = new ListOrder();
    private ListOrderAdapter listOrderAdapter;

    private ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);
        assignView();
        assignListener();
        initView();
        getOrder();
    }

    private void assignView(){
        listView = getViewById(R.id.a_o_list_view_orders);
    }

    private void assignListener(){
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                DataController.getInstance().setOrder(listOrder.get(position));
                goToOrderDetalisActivity();
            }
        });
    }

    private void initView(){
        listOrderAdapter = new ListOrderAdapter(this, listOrder);
        listView.setAdapter(listOrderAdapter);
    }

    @Override
    protected int getCoordinatorViewId() {
        return R.id.a_o_coordinator_root;
    }

    private void getOrder(){
        startLoading(false);
        WebMethods.getInstance().getOrders(
                DataController.getInstance().getSession().getAccessToken(),
                new RequestListener<OrdersResponse>() {
                    @Override
                    public void onRequestFailure(SpiceException spiceException) {
                        stopLoading();
                        showSnackBar("Response fail");
                    }

                    @Override
                    public void onRequestSuccess(OrdersResponse ordersResponse) {
                        stopLoading();
                        listOrder.clear();
                        listOrder.addAll(ordersResponse.getListOrder());
                        listOrderAdapter.notifyDataSetChanged();
                        showSnackBar("Response done");
                    }
                });
    }

    private void goToOrderDetalisActivity(){
        startActivity(new Intent(this, BouquetDeliveryStatusDetalisActivity.class));
    }
}
