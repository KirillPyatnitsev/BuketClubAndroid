package com.opendev.buket.club.view.activity;

import android.content.Intent;
import android.net.Uri;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import org.codehaus.jackson.map.util.ISO8601Utils;

import java.text.Format;
import java.text.SimpleDateFormat;

import com.opendev.buket.club.BuildConfig;
import com.opendev.buket.club.DataController;
import com.opendev.buket.club.R;
import com.opendev.buket.club.consts.ServerConfig;
import com.opendev.buket.club.model.Order;
import com.opendev.buket.club.model.Shop;
import com.opendev.buket.club.tools.Helper;
import com.opendev.buket.club.web.WebMethods;
import com.opendev.buket.club.web.response.OrderResponse;

public class OrderDetailsActivity extends BaseActivity {

    private static final String TAG = "OrderDetailsActiv";

    private ImageView imageBack;

    private TextView textBouquetName;
    private TextView textBouquetCost;
    private TextView textDetails;
    //private TextView textAddress;
    //private TextView textPhone;
    //private TextView textDeliveryTime;
    //private TextView textPayType;
    //private TextView textDeliveryType;
    //private TextView textShopPhone;
    private TextView textOrderStatus;

    private ImageView imageBouquet;
    private ImageView imageMap;
//    private ImageView imageArtistIcon;

    //    private LinearLayout linearPickup;
    private LinearLayout linearOnMap;

    private Order order;

    @Override
    protected void onCreateInternal() {
        setContentView(R.layout.activity_bouquet_delivery_status_detalis);
        assignView();
        assignListener();
        showOrder();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        showOrder();
    }

    private void showOrder() {
        Order order = DataController.getInstance().getOrder();
        if (order == null) {
            Log.e(TAG, "Intent called, but order is null!");
            this.finish();
        } else {
            Log.d(TAG, "Order: " + order.toString());
            fillView(order);
            updateOrder();
        }
    }

    @Override
    public void pushMessageReceived(String message) {
        super.pushMessageReceived(message);
        updateOrder();
    }

    @Override
    protected int getCoordinatorViewId() {
        return R.id.a_bdsd_coordinator_root;
    }

    private void assignView() {
        textBouquetName = getViewById(R.id.a_bdsd_text_bouquet_name);
        textBouquetCost = getViewById(R.id.a_bdsd_text_bouquet_cost);
        textDetails = getViewById(R.id.a_bdsd_text_details);
        //textAddress = getViewById(R.id.a_bdsd_text_address);
        //textPhone = getViewById(R.id.a_bdsd_text_phone);
        //textDeliveryTime = getViewById(R.id.a_bdsd_text_delivery_time);
        //textPayType = getViewById(R.id.a_bdsd_text_pay_type);
        //textDeliveryType = getViewById(R.id.a_bdsd_text_delivery_type);
        //textShopPhone = getViewById(R.id.a_bdsd_text_shop_telephone);
        textOrderStatus = getViewById(R.id.a_bdsd_text_order_status);

        imageBack = getViewById(R.id.i_ab_image_back);
        imageBack.setVisibility(View.VISIBLE);
        imageBack.setPadding(13, 0, 0, 0);

        imageBouquet = getViewById(R.id.a_bdsd_image_bouquet);
        imageMap = getViewById(R.id.a_bd_image_map);
//        imageArtistIcon = getViewById(R.id.a_bdsd_image_artist_icon);
        linearOnMap = getViewById(R.id.a_bd_linear_map);
//        linearPickup = getViewById(R.id.a_bdsd_linear_pickup);
    }

    private void assignListener() {
        imageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        final View.OnClickListener gotoMap = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(v.getContext(), "Go to the Map", Toast.LENGTH_LONG).show();
                mapIntent(order.getShop().getAddressLat(), order.getShop().getAddressLng());
            }
        };
        imageMap.setOnClickListener(gotoMap);
        linearOnMap.setOnClickListener(gotoMap);
    }

    private void fillView(Order order) {
        this.order = order;

        if (order.getBouquetItem() != null) {
            int size = this.getWindowWidth();
            Helper.loadImage(this, order.getBouquetItem().getImageUrl()).resize(size, (int) (size * 1.25))
                    .centerCrop()
                    .into(imageBouquet);
        }

        final String strPrice = Helper.intToPriceString(order.getPrice(), "");
        final String format = getString(R.string.default_buket_rub);
        textBouquetCost.setText(String.format(format, strPrice));
        textBouquetName.setText(order.getBouquetItem().getBouquetNameBySize(order.getSizeIndex()));

        final StringBuilder details = new StringBuilder();

        final String payType = getString(order.getPaymentTypeDesk());
        details.append(Html.escapeHtml(payType));

        final int deliveryResId = order.getDeliveryTypeResId();
        final String deliveryTime = order.getTimeDelivery();
        String strTime;
        if (deliveryTime == null) {
            strTime = getString(R.string.text_time_soon_standalone);
        } else {
            Format formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            strTime = formatter.format(ISO8601Utils.parse(deliveryTime));
        }

        final String deliveryType = getString(deliveryResId);
        String deliveryHtml = Html.escapeHtml(deliveryType + " " + strTime);
        final Shop shop = order.getShop();
        if(deliveryResId == Order.DELIVERY_TYPE_ADDRESS_DESK_RES_ID) {
            final String address = "" + order.getAddress();
            deliveryHtml += "<br>по адресу: <b>" + Html.escapeHtml(address) + "</b>";
        }
        details.append("<br>" + deliveryHtml);

        if(shop != null) {
            String nameAndAddress = shop.getName();
            if (shop.getAddressText() != null) {
                nameAndAddress += ", " + shop.getAddressText();
            }
            if(!Helper.isEmpty(shop.getPhone())) {
                nameAndAddress += ", " + shop.getPhone();
            }
            details.append("<br>" + Html.escapeHtml(nameAndAddress));
        }

        final String detailsHtml = details.toString();
        textDetails.setText(Html.fromHtml(detailsHtml));

        forViews(linearOnMap)
                .setVisibility(shop == null ? View.GONE : View.VISIBLE);

        //WebMethods.getInstance().loadImage(this, Helper.addServerPrefix(order.getShop().getImageUrl()), imageArtistIcon);
        textOrderStatus.setText(getString(order.getStatusDescRes()));
    }

    private void updateOrder() {
        if (!(ServerConfig.USE_FAKE_DEBUG_DATA && BuildConfig.DEBUG)) {
            loadOrder(order.getId());
        }
    }

    private void loadOrder(int orderId) {
        WebMethods.getInstance().orderGetRequest(orderId,
                new RequestListener<OrderResponse>() {
                    @Override
                    public void onRequestFailure(SpiceException spiceException) {

                    }

                    @Override
                    public void onRequestSuccess(OrderResponse orderResponse) {
                        Order order = orderResponse.getOrder();
                        if (order != null) {
                            fillView(order);
                        }
                    }
                });
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, OrdersActivity.class));
    }

    public void mapIntent(float latitude, float longitude) {
        String label = "ABC Label";
        String uriBegin = "geo:" + latitude + "," + longitude;
        String query = latitude + "," + longitude + "(" + label + ")";
        String encodedQuery = Uri.encode(query);
        String uriString = uriBegin + "?q=" + encodedQuery + "&z=16";
        Uri uri = Uri.parse(uriString);
        Intent intent = new Intent(android.content.Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }
}
