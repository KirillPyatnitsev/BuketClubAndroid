package ru.creators.buket.club.view.activitys;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import java.util.Timer;
import java.util.TimerTask;

import ru.creators.buket.club.DataController;
import ru.creators.buket.club.R;
import ru.creators.buket.club.model.Order;
import ru.creators.buket.club.model.Shop;
import ru.creators.buket.club.tools.Helper;
import ru.creators.buket.club.web.WebMethods;
import ru.creators.buket.club.web.response.DefaultResponse;

public class PayDoneActivity extends BaseActivity {

    private static final String TAG = "_PayDoneActivity";

    private TextView textViewPriceBouquet;
    private TextView textViewTitleBouquet;
    private TextView textViewCompositionBouquet;
    private TextView textViewOrderId;
    private TextView textViewShopAddress;
    private TextView textViewShopPhone;

    private ImageView imageViewBouquet;

    @Override
    protected void onCreateInternal() {
        setContentView(R.layout.activity_pay_done);

        assignView();

        sendOrder();
    }

    private void assignView() {
        textViewPriceBouquet = getViewById(R.id.a_pd_price);
        textViewTitleBouquet = getViewById(R.id.a_pd_title_bouquet);
        textViewCompositionBouquet = getViewById(R.id.a_pd_composition_bouquet);
        textViewOrderId = getViewById(R.id.a_pd_id_order);
        textViewShopAddress = getViewById(R.id.a_pd_shop_address);
        textViewShopPhone = getViewById(R.id.a_pd_shop_phone);

        imageViewBouquet = getViewById(R.id.a_pd_image_bouquet);
    }

    private void startClosingTimer() {
        Timer t = new Timer();
        t.schedule(new TimerTask() {

            @Override
            public void run() {
                goToOrderDetails();
                // If you want to call Activity then call from here for 5 seconds it automatically call and your image disappear....
            }
        }, 15000);
    }

    private void goToOrderDetails() {
        startActivity(new Intent(this, OrderDetailsActivity.class));
    }

    private void sendOrder() {

        RequestListener<DefaultResponse> listenerPath = new RequestListener<DefaultResponse>() {
            @Override
            public void onRequestFailure(SpiceException spiceException) {

            }

            @Override
            public void onRequestSuccess(DefaultResponse defaultResponse) {
                startClosingTimer();
            }
        };

        WebMethods.getInstance().orderPathRequest(DataController.getInstance().getSession().getAccessToken(),
                DataController.getInstance().getOrder().getOrderForServer(),
                DataController.getInstance().getOrder().getId(), listenerPath);

        Order order = DataController.getInstance().getOrder();

        if (order != null) {

            int sizeIndex = order.getSizeIndex();

            textViewPriceBouquet.setText(String.valueOf(order.getPrice()));
            textViewTitleBouquet.setText(order.getBouquetItem().getBouquetNameBySize(sizeIndex));
            textViewCompositionBouquet.setText(order.getBouquetItem().getBouquetDescriptionBySize(sizeIndex));
            textViewOrderId.setText(getString(R.string.number_order, order.getId()));

            Helper.loadImage(this, Helper.addServerPrefix(order.getBouquetItem().getImageUrl()), imageViewBouquet);

            Helper.drawSizeOnImage(order.getSizeIndex(), imageViewBouquet);

            Shop shop = order.getShop();

            if (shop != null) {
                textViewShopAddress.setText(shop.getName());
                textViewShopPhone.setText(shop.getPhone());
            }
        }

    }

    @Override
    public void onBackPressed() {
        return;
    }

    @Override
    protected int getCoordinatorViewId() {
        return R.id.a_pd_coordinator_root;
    }
}
