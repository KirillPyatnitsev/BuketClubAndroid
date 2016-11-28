package com.opendev.buket.club.view.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;
import com.opendev.buket.club.BuildConfig;
import com.opendev.buket.club.DataController;
import com.opendev.buket.club.GoogleApiInterface;
import com.opendev.buket.club.R;
import com.opendev.buket.club.consts.ServerConfig;
import com.opendev.buket.club.model.Bouquet;
import com.opendev.buket.club.model.Order;
import com.opendev.buket.club.model.Shop;
import com.opendev.buket.club.model.geocoding.Result;
import com.opendev.buket.club.model.geocoding.Results;
import com.opendev.buket.club.tools.Helper;
import com.opendev.buket.club.view.adapters.BouquetViewPagerAdapter;
import com.opendev.buket.club.web.WebMethods;
import com.opendev.buket.club.web.response.OrderResponse;

import org.codehaus.jackson.map.util.ISO8601Utils;

import java.io.IOException;
import java.sql.ResultSet;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class OrderDetailsActivity extends BaseActivity {

    private static final String TAG = "OrderDetailsActiv";

//    private ImageView imageBack;

//    private TextView textBouquetName;
//    private TextView textBouquetCost;
//    private TextView textDetails;
    //private TextView textAddress;
    //private TextView textPhone;
    //private TextView textDeliveryTime;
    //private TextView textPayType;
    //private TextView textDeliveryType;
    //private TextView textShopPhone;
//    private TextView textOrderStatus;

//    private ImageView imageBouquet;
//    private ImageView imageMap;
//    private ImageView imageArtistIcon;

    //    private LinearLayout linearPickup;
//    private LinearLayout linearOnMap;

    private static final String BUY_STRING_BEFORE = "Купить за: ";
    private static final String BUY_STRING_AFTER = " руб.";
    private static final int CENTER_ITEM_NUMBER = 0;
    private static final String[] INITIAL_PERMS={
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.CALL_PHONE
    };

    private static final String[] LOCATION_PERMS={
            Manifest.permission.ACCESS_FINE_LOCATION
    };
    private static final int INITIAL_REQUEST=1337;
    private static final int CAMERA_REQUEST=INITIAL_REQUEST+1;
    private static final int CALL_REQUEST=INITIAL_REQUEST+2;
    private static final int LOCATION_REQUEST=INITIAL_REQUEST+3;

    private int currentSize = Bouquet.SIZE_MEDIUM;

    private ViewPager bouquetViewPager;

    private TextView bouquetTitleText;
    private TextView bouquetCompText;
    private TextView bouquetRatingText;


    private TextView bouquetDescText;

    private Bouquet bouquet;
    private int imageSize;
    private int logoHeight;

  //  private TextView mSizeText;
  //  private TextView lSizeText;
 //   private TextView xlSizeText;

//    private TextView bouquetRatingSmallText;


    private Toolbar toolbar;
 //   private RelativeLayout sizeLayout;
    private RelativeLayout googleMapsLogoLayout;

    private String[] urls;
    private BouquetViewPagerAdapter bouquetViewPagerAdapter;

    private TextView[] dots;
    private LinearLayout dotsLayout;

    private TextView orderNumber;
    private TextView shopPhoneNumber;
    private TextView shopAddress;
    private TextView shopTitle;
    private TextView orderDeliveryTake;
    private TextView orderDeliveryDate;
    private TextView orderPrice;

    private ImageView googleLogo;

    private Order order;

    @Override
    protected void onCreateInternal() {
        setContentView(R.layout.activity_bouquet_delivery_status_detalis);
        assignView();
        initView();
        assignListener();
        //showOrder();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
     //   showOrder();
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
//        textBouquetName = getViewById(R.id.a_bdsd_text_bouquet_name);
//        textBouquetCost = getViewById(R.id.a_bdsd_text_bouquet_cost);
//        textDetails = getViewById(R.id.a_bdsd_text_details);
        //textAddress = getViewById(R.id.a_bdsd_text_address);
        //textPhone = getViewById(R.id.a_bdsd_text_phone);
        //textDeliveryTime = getViewById(R.id.a_bdsd_text_delivery_time);
        //textPayType = getViewById(R.id.a_bdsd_text_pay_type);
        //textDeliveryType = getViewById(R.id.a_bdsd_text_delivery_type);
        //textShopPhone = getViewById(R.id.a_bdsd_text_shop_telephone);
//        textOrderStatus = getViewById(R.id.a_bdsd_text_order_status);

//        imageBack = getViewById(R.id.i_ab_image_back);
//        imageBack.setVisibility(View.VISIBLE);
//        imageBack.setPadding(13, 0, 0, 0);
//
//        imageBouquet = getViewById(R.id.a_bdsd_image_bouquet);
//        imageMap = getViewById(R.id.a_bd_image_map);
//        imageArtistIcon = getViewById(R.id.a_bdsd_image_artist_icon);
//        linearOnMap = getViewById(R.id.a_bd_linear_map);
//        linearPickup = getViewById(R.id.a_bdsd_linear_pickup);

        bouquetViewPager = getViewById(R.id.order_details_image);


        bouquetTitleText = getViewById(R.id.order_details_bouquet_title);
        bouquetCompText = getViewById(R.id.order_details_comp);

        bouquetRatingText = getViewById(R.id.order_details_bar_text);

        bouquetDescText = getViewById(R.id.order_details_desc);

     //   mSizeText = getViewById(R.id.m_size_order_details_text);
      //  lSizeText = getViewById(R.id.l_size_order_details_text);
      //  xlSizeText = getViewById(R.id.xl_size_order_details_text);


        toolbar = getViewById(R.id.order_details_toolbar);

        googleLogo = getViewById(R.id.order_details_maps_logo);

     //   sizeLayout = getViewById(R.id.order_details_size_layout);

        orderNumber = getViewById(R.id.order_details_number);
        shopPhoneNumber = getViewById(R.id.order_details_delivery_number);
        shopAddress = getViewById(R.id.order_details_delivery_address);
        shopTitle = getViewById(R.id.order_details_delivery_title);


        dotsLayout = (LinearLayout) findViewById(R.id.order_details_dots);

        orderDeliveryTake = getViewById(R.id.order_details_delivery_take);
        orderDeliveryDate = getViewById(R.id.order_details_delivery_date);
        googleMapsLogoLayout = getViewById(R.id.order_details_maps_logo_layout);
        orderPrice = getViewById(R.id.order_details_price);
    }

    private void initView(){

        order = DataController.getInstance().getOrder();
        bouquet = order.getBouquetItem();
        urls = new String[3];
        refreshViewPager();
        setSupportActionBar(toolbar);
        setTitle("Букет");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        toolbar.setNavigationIcon(ContextCompat.getDrawable(this, R.drawable.ic_arrow_back));


        Helper.makeTextViewResizable(bouquetDescText, 10, "Читать дальше", true, 10);



        addBottomDots(CENTER_ITEM_NUMBER);


        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        imageSize = displaymetrics.widthPixels;
        bouquetViewPagerAdapter = new BouquetViewPagerAdapter(this, urls, imageSize);
        bouquetViewPager.setAdapter(bouquetViewPagerAdapter);




        ViewPager.OnPageChangeListener viewPagerPageChangeListener = new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                addBottomDots(position);

            }

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {

            }
        };

        bouquetViewPager.addOnPageChangeListener(viewPagerPageChangeListener);
        bouquetViewPager.setCurrentItem(CENTER_ITEM_NUMBER);


        if (order.getShop() != null) {
            orderNumber.setText(String.valueOf(order.getId()));
            String phone = order.getShop().getPhone();
            String phone2 = phone.substring(0, 2) + " (" + phone.substring(2, 5) + ") " +
                    phone.substring(5, 8) + "-" + phone.substring(8, 10) + "-" + phone.substring(10, 12);

            shopPhoneNumber.setText(phone2);
            shopAddress.setText(order.getShop().getAddressText());

            orderDeliveryTake.setVisibility(View.VISIBLE);
            orderDeliveryDate.setVisibility(View.VISIBLE);
        } else {
            orderNumber.setText(String.valueOf(order.getId()));
            shopPhoneNumber.setVisibility(View.GONE);
            shopAddress.setText("Поиск исполнителя...");
            shopTitle.setText("");
        }

        if (order.getDeliveryTypeResId() == R.string.delivery){
            shopTitle.setText("Адрес магазина:");
            orderDeliveryTake.setText("Товар будет доставлен:");
            orderDeliveryDate.setText("25.08.2016 с 8:00 до 19:00");
        } else {
            shopTitle.setText("Адрес самовывоза:");
            orderDeliveryTake.setText("Товар можно забрать:");
            orderDeliveryDate.setText("до 25.08.2016 с 8:00 до 19:00");
        }

        orderPrice.setText(order.getPrice() + " руб.");





    }



    private void refreshViewPager(){
        bouquetViewPager.setCurrentItem(CENTER_ITEM_NUMBER);
        int size = order.getSizeIndex();
        switch (size) {
            case Bouquet.SIZE_LITTLE:
                urls[0] = bouquet.getImageSmallUrls().get(0);
                urls[1] = bouquet.getImageSmallUrls().get(1);
                urls[2] = bouquet.getImageSmallUrls().get(2);
                break;
            case Bouquet.SIZE_MEDIUM:
                urls[0] = bouquet.getImageMediumUrls().get(0);
                urls[1] = bouquet.getImageMediumUrls().get(1);
                urls[2] = bouquet.getImageMediumUrls().get(2);
                break;
            case Bouquet.SIZE_GREAT:
                urls[0] = bouquet.getImageLargeUrls().get(0);
                urls[1] = bouquet.getImageLargeUrls().get(1);
                urls[2] = bouquet.getImageLargeUrls().get(2);
                break;
        }
    }

    private void addBottomDots(int currentPage) {
        dots = new TextView[urls.length];


        dotsLayout.removeAllViews();

        for (int i = 0; i < dots.length; i++) {
            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226; "));
            dots[i].setTextSize(20);
            dots[i].setTextColor(ContextCompat.getColor(this, R.color.color_dot_inactive));
            dotsLayout.addView(dots[i]);
        }

        if (dots.length > 0) {
            dots[currentPage].setTextColor(ContextCompat.getColor(this, R.color.color_dot_active));
        }
    }


    private void goToMapActivity() {
        Retrofit client = new Retrofit.Builder()
                .baseUrl(ServerConfig.SERVER_HOST_GOOGLE)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();



        GoogleApiInterface service = client.create(GoogleApiInterface.class);
        Call<Results> call = service.pay(order.getShop().getAddressText(), "AIzaSyCVbtTAYdl3bWdXt3XH6pG6o1XI4WseWBs");
        call.enqueue(new Callback<Results>() {
            @Override
            public void onResponse(Call<Results> call, Response<Results> response) {
                if (response.isSuccessful()){
                    order.setAddressLat(response.body().getResults().get(0)
                            .getGeometry()
                            .location
                            .lat);
                    order.setAddressLng(response.body().getResults().get(0)
                            .getGeometry()
                            .location
                            .lng);
                    DataController.getInstance().setOrder(order);

                    Helper.gotoActivity(OrderDetailsActivity.this, MapActivity.class);
                } else {
                    try {
                        Log.d("GoogleErr", response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<Results> call, Throwable t) {
                Log.d("GoogleErr", t.getLocalizedMessage());
            }
        });

    }

    private void assignListener() {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        googleLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (!canAccessLocation()) {
                        requestPermissions(INITIAL_PERMS, LOCATION_REQUEST);
                    } else {
                        goToMapActivity();
                    }
                } else {
                    goToMapActivity();

                }
            }
            });


        shopPhoneNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (!canAccessCall()) {
                        requestPermissions(INITIAL_PERMS, CALL_REQUEST);
                    } else {
                        Intent callIntent = new Intent(Intent.ACTION_CALL);
                        callIntent.setData(Uri.parse("tel:" + shopPhoneNumber.getText()));
                        startActivity(callIntent);
                    }
                } else {
                    Intent callIntent = new Intent(Intent.ACTION_CALL);
                    callIntent.setData(Uri.parse("tel:" + shopPhoneNumber.getText()));
                    startActivity(callIntent);

                }
            }
        });
//        imageBack.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                onBackPressed();
//            }
//        });

//        final View.OnClickListener gotoMap = new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //Toast.makeText(v.getContext(), "Go to the Map", Toast.LENGTH_LONG).show();
//                mapIntent(order.getShop().getAddressLat(), order.getShop().getAddressLng());
//            }
//        };
//        imageMap.setOnClickListener(gotoMap);
//        linearOnMap.setOnClickListener(gotoMap);

//        mSizeText.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                mSizeText.setSelected(true);
//                lSizeText.setSelected(false);
//                xlSizeText.setSelected(false);
//                refreshViewPager();
//                initBouquetBySize(Bouquet.SIZE_LITTLE);
//
//            }
//        });
//
//        lSizeText.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                mSizeText.setSelected(false);
//                lSizeText.setSelected(true);
//                xlSizeText.setSelected(false);
//                refreshViewPager();
//                initBouquetBySize(Bouquet.SIZE_MEDIUM);
//
//            }
//        });
//
//        xlSizeText.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                mSizeText.setSelected(false);
//                lSizeText.setSelected(false);
//                xlSizeText.setSelected(true);
//                refreshViewPager();
//                initBouquetBySize(Bouquet.SIZE_GREAT);
//
//            }
//        });



    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case LOCATION_REQUEST:
                if (canAccessLocation()) {
                    goToMapActivity();
                }
                break;
            case CALL_REQUEST:
                if (canAccessCall()){
                    Intent callIntent = new Intent(Intent.ACTION_CALL);
                    callIntent.setData(Uri.parse("tel:" + shopPhoneNumber.getText()));
                    startActivity(callIntent);
                }
                break;
        }
    }

    private void initBouquetBySize(int size){

        bouquetViewPager.setLayoutParams(new RelativeLayout.LayoutParams(imageSize, imageSize));
        /*Helper.loadImage(this, bouquet.getImageUrl())
                .placeholder(R.drawable.image_placeholder).fit().centerCrop()
                .into(bouquetImage);*/



        int a = toolbar.getHeight();
//        a = sizeLayout.getHeight();
        a = imageSize;


        bouquetTitleText.setText(bouquet.getBouquetNameBySize(size));





        int index = bouquet.getDescription().indexOf("|");
        if (index != -1) {
            String elements = bouquet.getDescription().substring(0, index);
            String desc = bouquet.getDescription().substring(index + 1);
            bouquetDescText.setText(desc);
            bouquetCompText.setText(elements);

        } else {
            bouquetDescText.setText(bouquet.getDescription());
            bouquetCompText.setText(bouquet.getDescription());
        }


        bouquetRatingText.setText("4.5");




    }

    private boolean canAccessLocation() {
        return(hasPermission(Manifest.permission.ACCESS_FINE_LOCATION));
    }

    private boolean canAccessCall() {
        return(hasPermission(Manifest.permission.CALL_PHONE));
    }

    private boolean hasPermission(String perm) {
        return(PackageManager.PERMISSION_GRANTED==checkSelfPermission(perm));
    }

    private void fillView(Order order) {
        this.order = order;

//        if (order.getBouquetItem() != null) {
//            int size = this.getWindowWidth();
//            Helper.loadImage(this, order.getBouquetItem().getImageUrl()).resize(size, (int) (size * 1.25))
//                    .centerCrop()
//                    .into(imageBouquet);
//        }

        final String strPrice = Helper.intToPriceString(order.getPrice(), "");
        final String format = getString(R.string.default_buket_rub);
//        textBouquetCost.setText(String.format(format, strPrice));
//        textBouquetName.setText(order.getBouquetItem().getBouquetNameBySize(order.getSizeIndex()));

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
//        textDetails.setText(Html.fromHtml(detailsHtml));

//        forViews(linearOnMap)
//                .setVisibility(shop == null ? View.GONE : View.VISIBLE);
//
        //WebMethods.getInstance().loadImage(this, Helper.addServerPrefix(order.getShop().getImageUrl()), imageArtistIcon);
//        textOrderStatus.setText(getString(order.getStatusDescRes()));
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
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        logoHeight = googleMapsLogoLayout.getMeasuredHeight();
        int size = order.getSizeIndex();
//        switch (size){
//            case Bouquet.SIZE_LITTLE:
//                mSizeText.setSelected(true);
//                break;
//            case Bouquet.SIZE_MEDIUM:
//                lSizeText.setSelected(true);
//                break;
//            case Bouquet.SIZE_GREAT:
//                xlSizeText.setSelected(true);
//                break;
//        }
        initBouquetBySize(size);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_details, menu);
        return super.onCreateOptionsMenu(menu);
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_share:
                DataController.getInstance().setBouquet(order.getBouquetItem());
                goToSocialActivity();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void goToSocialActivity() {
        startActivity(new Intent(this, SocialActivity.class));
    }
}
