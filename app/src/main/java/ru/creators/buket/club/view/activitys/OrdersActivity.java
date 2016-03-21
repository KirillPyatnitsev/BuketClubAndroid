package ru.creators.buket.club.view.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.flurry.android.FlurryAgent;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import ru.creators.buket.club.DataController;
import ru.creators.buket.club.R;
import ru.creators.buket.club.model.Order;
import ru.creators.buket.club.model.Pagination;
import ru.creators.buket.club.model.lists.ListOrder;
import ru.creators.buket.club.view.adapters.ListOrderAdapter;
import ru.creators.buket.club.web.WebMethods;
import ru.creators.buket.club.web.response.OrdersResponse;

public class OrdersActivity extends BaseActivity {

    private ListOrder listOrder = new ListOrder();
    private ListOrderAdapter listOrderAdapter;

    private ListView listView;

    private SwipeRefreshLayout swipeRefreshLayout;

    private ImageView imageBack;

    private Pagination pagination;
    private int lastLoadedPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);
        assignView();
        assignListener();
        initView();

        FlurryAgent.logEvent("OrdersActivity");

    }

    @Override
    protected void onResume() {
        super.onResume();
        listOrdersGetRequest(true);
    }

    private void assignView(){
        listView = getViewById(R.id.a_o_list_view_orders);
        imageBack = getViewById(R.id.i_ab_image_back);
        swipeRefreshLayout = getViewById(R.id.a_o_swipe_refresh);
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
                onBackPressed();
            }
        });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                listOrdersGetRequest(true);
            }
        });

        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (!listOrder.isEmpty() && firstVisibleItem + visibleItemCount == listOrder.size())
                    listOrdersGetRequest(false);
            }
        });
    }

    @Override
    public void onBackPressed() {
        goToBouquetsActivity();
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

    private void listOrdersGetRequest(boolean reload){
        if (reload){
            lastLoadedPage = 0;
            pagination = null;
            listOrder.clear();
        }

        if (pagination==null || lastLoadedPage < pagination.getNextPage()){
            lastLoadedPage = pagination==null ? 1 : pagination.getNextPage();
            getOrders(lastLoadedPage);
        }
    }

    private void getOrders(int page){
        if (!swipeRefreshLayout.isRefreshing())
            startLoading(false);
        WebMethods.getInstance().getOrders(
                DataController.getInstance().getSession().getAccessToken(), page, Pagination.PER_PAGE,
                new RequestListener<OrdersResponse>() {
                    @Override
                    public void onRequestFailure(SpiceException spiceException) {
                        if (!swipeRefreshLayout.isRefreshing())
                            stopLoading();
                        else
                            swipeRefreshLayout.setRefreshing(false);
                    }

                    @Override
                    public void onRequestSuccess(OrdersResponse ordersResponse) {
                        if (!swipeRefreshLayout.isRefreshing())
                            stopLoading();
                        else
                            swipeRefreshLayout.setRefreshing(false);
                        listOrder.addAll(ordersResponse.getListOrder());
                        listOrderAdapter.notifyDataSetChanged();

                        pagination = ordersResponse.getMeta().getPagination();
                    }
                });
    }

    private void goToOrderDetalisActivity(){
        startActivity(new Intent(this, OrderDetailsActivity.class));
    }

    private void goToReviewActivity(){
        startActivity(new Intent(this, ReviewActivity.class));
    }
}
