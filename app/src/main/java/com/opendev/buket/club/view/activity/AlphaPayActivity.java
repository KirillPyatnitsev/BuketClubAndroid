    package com.opendev.buket.club.view.activity;

    import android.content.Context;
    import android.content.Intent;
    import android.os.Handler;
    import android.support.v4.content.ContextCompat;
    import android.support.v7.widget.Toolbar;
    import android.util.Log;
    import android.view.View;
    import android.webkit.WebView;
    import android.webkit.WebViewClient;

    import com.octo.android.robospice.persistence.exception.SpiceException;
    import com.octo.android.robospice.request.listener.RequestListener;
    import com.opendev.buket.club.DataController;
    import com.opendev.buket.club.R;
    import com.opendev.buket.club.model.Order;
    import com.opendev.buket.club.web.WebMethods;
    import com.opendev.buket.club.web.response.OrderResponse;

    public class AlphaPayActivity extends BaseActivity {


        @Override
        protected void onCreateInternal() {
            setContentView(R.layout.activity_alpha_pay);

            Toolbar toolbar = (Toolbar) findViewById(R.id.alpha_toolbar);
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
            toolbar.setNavigationIcon(ContextCompat.getDrawable(this, R.drawable.ic_arrow_back));

            //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            ////getSupportActionBar().setHomeButtonEnabled(true);
            // toolbar.setNavigationIcon(ContextCompat.getDrawable(this, R.drawable.ic_hamburger));

            setTitle("Назад");

            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                }
            });

            WebView webView = (WebView) findViewById(R.id.pay_web_view);
            String url = getIntent().getStringExtra("url");
            // включаем поддержку JavaScript
            webView.getSettings().setJavaScriptEnabled(true);
            // указываем страницу загрузки
            webView.loadUrl(url);
            webView.setWebViewClient(new HelloWebViewClient(this));
        }

        @Override
        protected int getCoordinatorViewId() {
            return R.id.a_b_alpha_pay_root;
        }

        class HelloWebViewClient extends WebViewClient {

        Context context;
        public HelloWebViewClient(Context context) {
            this.context = context;
        }
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (url.contains("return")){
                successPayment();

            }
            view.loadUrl(url);
            return true;
        }
    }



    private void successPayment() {
        //startLoading();

        final Order order = DataController.getInstance().getOrder();
        final Order serverOrder = order.getOrderForServer();
        Log.d("AlphaPayTag", "Sending new order2: " + serverOrder);

        WebMethods.getInstance().sendOrder(serverOrder,
                new RequestListener<OrderResponse>() {
                    @Override
                    public void onRequestFailure(SpiceException spiceException) {
                        //stopLoading();
                    }

                    @Override
                    public void onRequestSuccess(OrderResponse orderResponse) {
                        //stopLoading();
                        orderResponse.getOrder().setBouquetItemId(orderResponse.getOrder().getBouquetItem().getId());
                        DataController.getInstance().setOrder(orderResponse.getOrder());
                        DataController.getInstance().getOrder().setTypePayment(Order.TYPE_PAYMENT_CARD);
                        DataController.getInstance().getOrder().setTypePaymentIndex(Order.TYPE_PAYMENT_INDEX_CARD);
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                final Intent mainIntent = new Intent(AlphaPayActivity.this, OrdersActivity.class);
                                AlphaPayActivity.this.startActivity(mainIntent);

                            }
                        }, 5000);
                    }
                });

    }



}
