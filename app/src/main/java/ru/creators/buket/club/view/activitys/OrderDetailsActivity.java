package ru.creators.buket.club.view.activitys;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
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
import ru.creators.buket.club.tools.Helper;
import ru.creators.buket.club.web.WebMethods;
import ru.creators.buket.club.web.response.OrderResponse;

public class OrderDetailsActivity extends BaseActivity {

    private ImageView imageBack;

    private TextView textBouquetName;
    private TextView textBouquetCost;
    private TextView textAddress;
    private TextView textDeliveryTime;
    private TextView textPayType;
    private TextView textShopPhone;
    private TextView textOrderStatus;

    private ImageView imageBouquet;
    private ImageView imageArtistIcon;

    private LinearLayout linearPickup;
    private LinearLayout linearOnMap;

    private Order order = DataController.getInstance().getOrder();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bouquet_delivery_status_detalis);
        if (order!=null) {
            assingView();
            assignListener();
            initView();
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

    private void assingView(){
        textBouquetName = getViewById(R.id.a_bdsd_text_bouquet_name);
        textBouquetCost = getViewById(R.id.a_bdsd_text_bouquet_cost);
        textAddress = getViewById(R.id.a_bdsd_text_address);
        textDeliveryTime = getViewById(R.id.a_bdsd_text_delivery_time);
        textPayType = getViewById(R.id.a_bdsd_text_pay_type);
        textShopPhone = getViewById(R.id.a_bdsd_text_shop_telephone);
        textOrderStatus = getViewById(R.id.a_bdsd_text_order_status);

        imageBack = getViewById(R.id.i_ab_image_back);

        imageBouquet = getViewById(R.id.a_bdsd_image_bouquet);
        imageArtistIcon = getViewById(R.id.a_bdsd_image_artist_icon);
        linearOnMap = getViewById(R.id.a_bdsd_linear_on_map);
        linearPickup = getViewById(R.id.a_bdsd_linear_pickup);
    }

    private void assignListener(){
        imageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        linearOnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mapIntent(order.getShop().getAddressLat(), order.getShop().getAddressLng());
            }
        });
    }

    private void initView(){
        fillView(order);
    }

    private void fillView(Order order){
        if (order.getBouquetItem()!=null)
            WebMethods.getInstance().loadImage(this, Helper.addServerPrefix(order.getBouquetItem().getImageUrl()), imageBouquet);
        if (order.getShop()!=null)
            WebMethods.getInstance().loadImage(this, Helper.addServerPrefix(order.getShop().getImageUrl()), imageArtistIcon);

        imageBack.setVisibility(View.VISIBLE);

        textBouquetCost.setText(Helper.intToPriceString(order.getPrice()));
        textBouquetName.setText(order.getBouquetItem().getBouquetNameBySize(order.getSizeIndex()));


        if (order.getShippingType().equals(Order.DELIVERY_TYPE_ADDRESS))
            textAddress.setText(order.getAddress());
        else{
            textAddress.setVisibility(View.GONE);
            linearPickup.setVisibility(View.VISIBLE);

            if (order.getShop()!=null)
                linearOnMap.setVisibility(View.VISIBLE);
        }

        Format formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        if (order.getTimeDelivery() != null){
            textDeliveryTime.setText(formatter.format(ISO8601Utils.parse(order.getTimeDelivery())));
        }else{
            textDeliveryTime.setText(getString(R.string.text_time_soon));
        }

        textPayType.setText(getString(order.getPaymentTypeDesk()));

        if (order.getShop()!= null && order.getShop().getPhone() != null) {
            textShopPhone.setText(order.getShop().getPhone());
        }else{
            textShopPhone.setText("Неизвестно");
        }

        textOrderStatus.setText(getString(order.getStatusDescRes()));
    }

    private void updateOrder(){
        WebMethods.getInstance().orderGetRequest(DataController.getInstance().getSession().getAccessToken(),
                order.getId(), new RequestListener<OrderResponse>() {
                    @Override
                    public void onRequestFailure(SpiceException spiceException) {

                    }

                    @Override
                    public void onRequestSuccess(OrderResponse orderResponse) {
                        if (orderResponse.getOrder()!=null){
                            order = orderResponse.getOrder();
                            fillView(order);
                        }
                    }
                });
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, OrdersActivity.class));
    }

    public void mapIntent(float latitude, float longitude){
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
