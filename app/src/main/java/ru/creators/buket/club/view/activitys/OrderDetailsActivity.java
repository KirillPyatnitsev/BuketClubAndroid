package ru.creators.buket.club.view.activitys;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.flurry.android.FlurryAgent;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import org.codehaus.jackson.map.util.ISO8601Utils;

import java.text.Format;
import java.text.SimpleDateFormat;

import ru.creators.buket.club.DataController;
import ru.creators.buket.club.R;
import ru.creators.buket.club.model.Order;
import ru.creators.buket.club.model.Shop;
import ru.creators.buket.club.tools.Helper;
import ru.creators.buket.club.web.WebMethods;
import ru.creators.buket.club.web.response.OrderResponse;

public class OrderDetailsActivity extends BaseActivity {

    private static final String TAG = "OrderDetailsActiv";

    private ImageView imageBack;

    private TextView textBouquetName;
    private TextView textBouquetCost;
    private TextView textAddress;
    private TextView textPhone;
    private TextView textDeliveryTime;
    private TextView textDeliveryTimePrefix;
    private TextView textPayType;
    private TextView textDeliveryType;
//    private TextView textShopPhone;
//    private TextView textOrderStatus;

    private ImageView imageBouquet;
    private ImageView imageMap;
//    private ImageView imageArtistIcon;

    //    private LinearLayout linearPickup;
    private LinearLayout linearOnMap;

    private Order order;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bouquet_delivery_status_detalis);

        Order order = DataController.getInstance().getOrder();

        if (order != null) {
            Log.d(TAG, "Order: " + order.toString());
            Log.d(TAG, "access_token: " + DataController.getInstance().getSession().getAccessToken());
            assignView();
            assignListener();
            fillView(order);
            updateOrder();
        } else {
            showSnackBar(R.string.oops_error);
            startActivity(new Intent(this, OrdersActivity.class));
        }
        FlurryAgent.logEvent("OrderDetailsActivity");
    }

    @Override
    protected int getCoordinatorViewId() {
        return R.id.a_bdsd_coordinator_root;
    }

    private void assignView() {
        textBouquetName = getViewById(R.id.a_bdsd_text_bouquet_name);
        textBouquetCost = getViewById(R.id.a_bdsd_text_bouquet_cost);
        textAddress = getViewById(R.id.a_bdsd_text_address);
        textPhone = getViewById(R.id.a_bdsd_text_phone);
        textDeliveryTime = getViewById(R.id.a_bdsd_text_delivery_time);
        textDeliveryTimePrefix = getViewById(R.id.a_bdsd_text_delivery_time_prefix);
        textPayType = getViewById(R.id.a_bdsd_text_pay_type);
        textDeliveryType = getViewById(R.id.a_bdsd_text_delivery_type);
//        textShopPhone = getViewById(R.id.a_bdsd_text_shop_telephone);
//        textOrderStatus = getViewById(R.id.a_bdsd_text_order_status);

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

        imageMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(v.getContext(), "Go to the Map", Toast.LENGTH_LONG).show();
                mapIntent(order.getShop().getAddressLat(), order.getShop().getAddressLng());
            }
        });
    }

    private void fillView(Order order) {
        this.order = order;

        if (order.getBouquetItem() != null) {
            WebMethods.getInstance().loadImage(this, Helper.addServerPrefix(order.getBouquetItem().getImageUrl()), imageBouquet);
        }

        String strPrice = Helper.intToPriceString(order.getPrice(), "");
        String format = getString(R.string.default_buket_rub);
        textBouquetCost.setText(String.format(format, strPrice));
        textBouquetName.setText(order.getBouquetItem().getBouquetNameBySize(order.getSizeIndex()));

        int deliveryResId = order.getDeliveryTypeResId();
        textDeliveryType.setText(deliveryResId);
        //linearPickup.setVisibility(View.VISIBLE);

        Shop shop = order.getShop();
        forViews(linearOnMap, textAddress, textPhone)
                .setVisibility(shop == null ? View.INVISIBLE : View.VISIBLE);
        textAddress.setText(shop == null ? "--" : shop.getName());
        textPhone.setText(shop == null ? "--" : shop.getPhone());
        //WebMethods.getInstance().loadImage(this, Helper.addServerPrefix(order.getShop().getImageUrl()), imageArtistIcon);

        String deliveryTime = order.getTimeDelivery();
        textDeliveryTimePrefix.setVisibility(deliveryTime == null ? View.INVISIBLE : View.VISIBLE);
        String strTime;
        if (deliveryTime == null) {
            strTime = getString(R.string.text_time_soon_standalone);
        } else {
            Format formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            strTime = formatter.format(ISO8601Utils.parse(deliveryTime));
        }
        textDeliveryTime.setText(strTime);

        textPayType.setText(getString(order.getPaymentTypeDesk()));

//        if (order.getShop()!= null && order.getShop().getPhone() != null) {
//            textShopPhone.setText(order.getShop().getPhone());
//        }else{
//            textShopPhone.setText("Неизвестно");
//        }
//
//        textOrderStatus.setText(getString(order.getStatusDescRes()));
    }

    public void updateOrder() {
        loadOrder(order.getId());
    }

    private void loadOrder(int orderId) {
        WebMethods.getInstance().orderGetRequest(DataController.getInstance().getSession().getAccessToken(),
                orderId, new RequestListener<OrderResponse>() {
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
