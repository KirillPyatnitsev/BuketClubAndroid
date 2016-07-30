package com.opendev.buket.club.view.activity;

import android.content.Intent;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import java.util.Timer;
import java.util.TimerTask;

import com.opendev.buket.club.DataController;
import com.opendev.buket.club.R;
import com.opendev.buket.club.consts.ServerConfig;
import com.opendev.buket.club.model.Order;
import com.opendev.buket.club.model.Shop;
import com.opendev.buket.club.tools.Helper;
import com.opendev.buket.club.web.WebMethods;
import com.opendev.buket.club.web.response.DefaultResponse;

public class PayDoneActivity extends BaseActivity {

    private static final String TAG = ServerConfig.TAG_PREFIX + "PayDoneActivity";

    //private TextView textViewPriceBouquet;
    //private TextView textViewTitleBouquet;
    //private TextView textViewCompositionBouquet;
    private TextView textViewOrderId;
    //private TextView textViewShopAddress;
    //private TextView textViewShopPhone;

    //private ImageView imageViewBouquet;

    private Timer timer = null;

    private Order order;

    @Override
    protected void onCreateInternal() {
        setContentView(R.layout.activity_pay_done);
        order = DataController.getInstance().getOrder();

        if(order == null) {
            this.finish();
        }
        assignView();
        sendOrder();
    }

    private void assignView() {
        //textViewPriceBouquet = getViewById(R.id.a_pd_price);
        //textViewTitleBouquet = getViewById(R.id.a_pd_title_bouquet);
        //textViewCompositionBouquet = getViewById(R.id.a_pd_composition_bouquet);
        textViewOrderId = getViewById(R.id.a_pd_id_order);
        //textViewShopAddress = getViewById(R.id.a_pd_shop_address);
        //textViewShopPhone = getViewById(R.id.a_pd_shop_phone);

        //imageViewBouquet = getViewById(R.id.a_pd_image_bouquet);
    }

    private void startClosingTimer() {
        timer = new Timer();
        timer.schedule(new TimerTask() {

            @Override
            public void run() {
                goToOrderDetails();
                // If you want to call Activity then call from here for 5 seconds it automatically call and your image disappear....
            }
        }, 5000);
    }

    private void goToOrderDetails() {
        startActivity(new Intent(this, OrderDetailsActivity.class));
    }

    private void sendOrder() {

        WebMethods.getInstance().orderPatchRequest(order.getOrderForServer(), order.getId(), new RequestListener<DefaultResponse>() {
            @Override
            public void onRequestFailure(SpiceException spiceException) {
                Toast.makeText(PayDoneActivity.this, "Есть проблемы с размещением заказа!", Toast.LENGTH_LONG).show();
                startClosingTimer();
            }

            @Override
            public void onRequestSuccess(DefaultResponse defaultResponse) {
                startClosingTimer();
            }
        });

        int sizeIndex = order.getSizeIndex();
        //textViewPriceBouquet.setText(String.valueOf(order.getPrice()));
        //textViewTitleBouquet.setText(order.getBouquetItem().getBouquetNameBySize(sizeIndex));
        //textViewCompositionBouquet.setText(order.getBouquetItem().getBouquetDescriptionBySize(sizeIndex));
        textViewOrderId.setText(getString(R.string.number_order, order.getId()));

        //int size = this.getWindowWidth();
        //Helper.loadImage(this, order.getBouquetItem().getImageUrl()).resize(size, size)
        //        .centerCrop().into(imageViewBouquet);
        //Helper.drawSizeOnImage(order.getSizeIndex(), imageViewBouquet);

        //Shop shop = order.getShop();
        //if (shop != null) {
        //    textViewShopAddress.setText(shop.getName());
        //    textViewShopPhone.setText(shop.getPhone());
        //}

    }

    @Override
    public void onBackPressed() {
        if(timer != null) {
            timer.cancel();
        }
        goToOrderDetails();
    }

    @Override
    protected int getCoordinatorViewId() {
        return R.id.a_pd_coordinator_root;
    }
}
