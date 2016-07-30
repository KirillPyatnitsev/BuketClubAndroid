package com.opendev.buket.club.view.activity;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import com.opendev.buket.club.DataController;
import com.opendev.buket.club.R;
import com.opendev.buket.club.consts.ServerConfig;
import com.opendev.buket.club.model.Order;
import com.opendev.buket.club.model.Pagination;
import com.opendev.buket.club.model.lists.ListOrder;
import com.opendev.buket.club.view.adapters.ListOrderAdapter;
import com.opendev.buket.club.web.WebMethods;
import com.opendev.buket.club.web.response.OrdersResponse;

public class OrdersActivity extends BaseActivity {

    private static final String TAG = ServerConfig.TAG_PREFIX + "OrdersActivity";

    private ListOrderAdapter listOrderAdapter;
    private ListView listView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ImageView imageBack;
    private Pagination pagination;
    private int lastLoadedPage;

    @Override
    protected final void onCreateInternal() {
        setContentView(R.layout.activity_orders);
        assignView();
        assignListener();
        initView();
    }

    @Override
    protected final void onResume() {
        super.onResume();
        listOrdersGetRequest(true);
    }

    private void assignView() {
        listView = getViewById(R.id.a_o_list_view_orders);
        imageBack = getViewById(R.id.i_ab_image_back);
        swipeRefreshLayout = getViewById(R.id.a_o_swipe_refresh);
    }

    private void assignListener() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final Order order = listOrderAdapter.get(position);
                DataController.getInstance().setOrder(order);

                if (order.isDelivered()) {
                    goToReviewActivity();
                } else {
                    goToOrderDetailsActivity();
                }

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
                if (totalItemCount > 0 && firstVisibleItem + visibleItemCount >= totalItemCount) {
                    listOrdersGetRequest(false);
                }
            }
        });
    }

    @Override
    public final void onBackPressed() {
        goToBouquetsActivity();
    }

    private void goToBouquetsActivity() {
        startActivity(new Intent(this, BucketsActivity.class));
    }

    private void initView() {
        imageBack.setVisibility(View.VISIBLE);
        listOrderAdapter = new ListOrderAdapter(this);
        listView.setAdapter(listOrderAdapter);
    }

    @Override
    protected final int getCoordinatorViewId() {
        return R.id.a_o_coordinator_root;
    }

    private void listOrdersGetRequest(boolean reload) {
        if (reload) {
            lastLoadedPage = 0;
            pagination = null;
            listOrderAdapter.clear();
        }

        if (pagination == null || lastLoadedPage < pagination.getNextPage()) {
            lastLoadedPage = pagination == null ? 1 : pagination.getNextPage();
            getOrders(lastLoadedPage);
        }
    }

    private void getOrders(int page) {
        Log.d(TAG, "Requesting orders page " + page);
        if (!swipeRefreshLayout.isRefreshing()) {
            startLoading();
        }
        WebMethods.getInstance().getOrders(page, Pagination.PER_PAGE,
                new RequestListener<OrdersResponse>() {
                    @Override
                    public void onRequestFailure(SpiceException spiceException) {
                        if (!swipeRefreshLayout.isRefreshing()) {
                            stopLoading();
                        } else {
                            swipeRefreshLayout.setRefreshing(false);
                        }
                    }

                    @Override
                    public void onRequestSuccess(OrdersResponse ordersResponse) {
                        if (!swipeRefreshLayout.isRefreshing()) {
                            stopLoading();
                        } else {
                            swipeRefreshLayout.setRefreshing(false);
                        }
                        ListOrder orders = ordersResponse.getListOrder();
                        if(orders == null) {
                            Log.e(TAG, "In ordersResponse got orders = null!");
                        } else {
                            Log.d(TAG, "Orders loaded: " + orders.size());
                            listOrderAdapter.addAll(orders);
                        }

                        pagination = ordersResponse.getMeta().getPagination();
                    }
                });
    }

    private void goToOrderDetailsActivity() {
        startActivity(new Intent(this, OrderDetailsActivity.class));
    }

    private void goToReviewActivity() {
        startActivity(new Intent(this, ReviewActivity.class));
    }
}
