package ru.creators.buket.club.view.activitys;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.elirex.fayeclient.FayeClient;
import com.elirex.fayeclient.FayeClientListener;
import com.elirex.fayeclient.MetaMessage;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;
import com.transitionseverywhere.TransitionManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import ru.creators.buket.club.DataController;
import ru.creators.buket.club.R;
import ru.creators.buket.club.consts.ServerConfig;
import ru.creators.buket.club.model.AnswerFlex;
import ru.creators.buket.club.model.Order;
import ru.creators.buket.club.model.Pagination;
import ru.creators.buket.club.model.Shop;
import ru.creators.buket.club.model.lists.ListAnswerFlex;
import ru.creators.buket.club.model.lists.ListShop;
import ru.creators.buket.club.tools.Helper;
import ru.creators.buket.club.view.adapters.ListAnswerFlexAdapter;
import ru.creators.buket.club.web.WebMethods;
import ru.creators.buket.club.web.response.DefaultResponse;
import ru.creators.buket.club.web.response.ListAnswerFlexResponse;
import ru.creators.buket.club.web.response.ShopListResponse;

public class ChoseShopActivity extends BaseActivity implements
        OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private final LatLng MOSCOW_CENTER = new LatLng(55.75370903771494, 37.61981338262558);

    private ListAnswerFlex listAnswerFlex;
    private ListAnswerFlexAdapter listAnswerFlexAdapter;
    private ListShop listShopAll;

    private ListView listView;

    private ImageView imageBack;
    private ImageView imageSettingsOpen;
    private ImageView imageSettingsClose;
    private ImageView imageBouquet;


    private SwipeRefreshLayout swipeRefreshLayout;

    private Order order = DataController.getInstance().getOrder();

    private FayeClient fayeClientOrder;

    private RelativeLayout relativeContainerMap;
    private GoogleMap googleMap;

    private TextView textShopNotFound;
    private TextView textSort;
    private TextView textShowMap;
    private TextView textShowList;
    private TextView textActionBarTitle;
    private ImageView imageActionBarAnimation1;
    private ImageView imageActionBarAnimation2;
    private ImageView imageActionBar;

    private List<Marker> listMarkerAnsweredShops;
    private List<Marker> listMarkerNotAnsweredShops;

    private String MARKER_BID_PRICE;
    private String MARKER_STORE;

    private Pagination paginationShopGetList;

    private GoogleApiClient mGoogleApiClient;

    private Location mLastLocation;

    private Comparator<AnswerFlex> selectedAnswersComporator = AnswerFlex.COMPARATOR_SORT_BY_PRICE;

    private boolean mapIsNeverOpened = true;
    private boolean firstAnsver = true;

    private LatLng mapCenterLocation;

    private PulseScaleAnimation pulseScaleAnimation;

    @Override
    protected void onCreateInternal() {
        setContentView(R.layout.activity_chose_shop);

        MARKER_BID_PRICE = getString(R.string.marker_bid_price);
        MARKER_STORE = getString(R.string.marker_store);

        assignView();
        assignListener();
        initView();
        initMap();
        listMarkerAnsweredShops = new ArrayList<>();
        listMarkerNotAnsweredShops = new ArrayList<>();

        startLoadingShopList();

        if (DataController.getInstance().getOrder().getShippingType().equals(Order.DELIVERY_TYPE_PICKUP)) {
            if (mGoogleApiClient == null) {
                mGoogleApiClient = new GoogleApiClient.Builder(this)
                        .addConnectionCallbacks(this)
                        .addOnConnectionFailedListener(this)
                        .addApi(LocationServices.API)
                        .build();
            }
        } else {
            mapCenterLocation = new LatLng(DataController.getInstance().getOrder().getAddressLat(), DataController.getInstance().getOrder().getAddressLng());
        }

        initFaye();

        showMap();
        textShowList.setVisibility(View.GONE);
    }

    @Override
    public void pushMessageReceived(String message) {
        // Do not display anything
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mGoogleApiClient != null)
            mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        if (mGoogleApiClient != null)
            mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    protected void onPause() {
//        pulseScaleAnimation.stop();
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
//        startAnimation();
    }

    @Override
    public void onConnected(Bundle connectionHint) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

            if (mLastLocation != null) {
                DataController.getInstance().getOrder().setAddressLat(mLastLocation.getLatitude());
                DataController.getInstance().getOrder().setAddressLng(mLastLocation.getLongitude());

                if (DataController.getInstance().getOrder().getShippingType().equals(Order.DELIVERY_TYPE_PICKUP)) {
                    mapCenterLocation = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
                }

            } else {
                mapCenterLocation = MOSCOW_CENTER;
            }
        }

        if (googleMap != null) {
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mapCenterLocation, 20));
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        textShowMap.setVisibility(View.INVISIBLE);
    }

    private void initMap() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.a_cs_map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;

        googleMap.getUiSettings().setMyLocationButtonEnabled(false);

        if (DataController.getInstance().getOrder().getShippingType().equals(Order.DELIVERY_TYPE_PICKUP)
                && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
            googleMap.setMyLocationEnabled(true);

        googleMap.setOnInfoWindowClickListener(this);

        if (mapCenterLocation != null)
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mapCenterLocation, 20));
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        if (listMarkerAnsweredShops.contains(marker)) {
            choseShop(listMarkerAnsweredShops.indexOf(marker));
        }
    }

    @Override
    protected int getCoordinatorViewId() {
        return R.id.a_cs_coordinator_root;
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        startAnimation();
    }

    private void startAnimation() {

        if (pulseScaleAnimation != null) {
//            pulseScaleAnimation.stop();
        }

        int imageActionBarWidth = imageActionBar.getWidth();
        int imageActionBarHeight = imageActionBar.getHeight();
        float imageActionBarX = imageActionBar.getX();
        float imageActionBarY = imageActionBar.getY();

        imageActionBarAnimation1.getLayoutParams().width = imageActionBarWidth;
        imageActionBarAnimation1.getLayoutParams().height = imageActionBarHeight;
        imageActionBarAnimation1.setX(imageActionBarX);
        imageActionBarAnimation1.setY(imageActionBarY);

        imageActionBarAnimation2.getLayoutParams().width = imageActionBarWidth;
        imageActionBarAnimation2.getLayoutParams().height = imageActionBarHeight;
        imageActionBarAnimation2.setX(imageActionBarX);
        imageActionBarAnimation2.setY(imageActionBarY);

        imageActionBarAnimation1.setVisibility(View.VISIBLE);
        imageActionBarAnimation2.setVisibility(View.VISIBLE);

        imageActionBarAnimation1.requestLayout();
        imageActionBarAnimation2.requestLayout();

        pulseScaleAnimation = new PulseScaleAnimation(imageActionBarAnimation1, imageActionBarAnimation2, (float) (2), 1300);
        pulseScaleAnimation.start();
    }

    private void updateMapMarkers() {
        if (googleMap != null) {
            googleMap.clear();
            listMarkerNotAnsweredShops.clear();
            listMarkerAnsweredShops.clear();
            if (listAnswerFlex != null && listShopAll != null
                    && (mapCenterLocation != null || DataController.getInstance().getOrder().getShippingType().equals(Order.DELIVERY_TYPE_ADDRESS))) {

                double lat;
                double lng;

                if (DataController.getInstance().getOrder().getShippingType().equals(Order.DELIVERY_TYPE_ADDRESS)) {
                    googleMap.addMarker(new MarkerOptions()
                            .position(new LatLng(DataController.getInstance().getOrder().getAddressLat(),
                                    DataController.getInstance().getOrder().getAddressLng()))
                            .title("Место доставки заказа.")
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.finish_ico))
                            .snippet(DataController.getInstance().getOrder().getAddress()));
                }

                lat = mapCenterLocation.latitude;
                lng = mapCenterLocation.longitude;

                if (lat == 0f && lng == 0f) {
                    showSnackBar(R.string.gps_error);
                }

                for (AnswerFlex answerFlex : listAnswerFlex) {
                    listShopAll.removeByShopId(answerFlex.getShop().getId());
                    answerFlex.setDistance(Helper.distFrom((float) lat, (float) lng
                            , answerFlex.getShop().getAddressLat(), answerFlex.getShop().getAddressLng()));
                    listMarkerAnsweredShops.add(googleMap.addMarker(new MarkerOptions()
                            .position(new LatLng(answerFlex.getShop().getAddressLat(), answerFlex.getShop().getAddressLng()))
                            .title(MARKER_BID_PRICE + " " + Helper.intToPriceString(answerFlex.getPrice(), this) + ", " +
                                    distToString(answerFlex.getDistance()))
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_map_red))
                            .snippet(MARKER_STORE + " " + answerFlex.getShop().getName())));
                }

                listAnswerFlexAdapter.notifyDataSetChanged();

                for (Shop shop : listShopAll) {
                    if (shop != null)
                        listMarkerNotAnsweredShops.add(googleMap.addMarker(new MarkerOptions()
                                .position(new LatLng(shop.getAddressLat(), shop.getAddressLng()))
                                .title(MARKER_STORE + " " + shop.getName())
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_map_grey))
                                .snippet("Расстояние: " + distToString(Helper.distFrom((float) lat, (float) lng
                                        , shop.getAddressLat(), shop.getAddressLng())))));
                }
            }
        }
    }

    private void assignView() {
        imageBouquet = getViewById(R.id.a_cs_image_bouquet);
        imageBack = getViewById(R.id.i_ab_image_back);
        listView = getViewById(R.id.a_cs_list_view_artists);
        relativeContainerMap = getViewById(R.id.a_cs_relative_container_map);
        imageSettingsOpen = getViewById(R.id.i_ab_image_settings_open);
        imageSettingsClose = getViewById(R.id.i_ab_image_settings_close);

        textShopNotFound = getViewById(R.id.a_cs_text_shop_not_found);
        textActionBarTitle = getViewById(R.id.a_cs_text_action_bar_title);
        swipeRefreshLayout = getViewById(R.id.a_cs_swipe_refresh);

        textSort = getViewById(R.id.a_cs_text_action_bar_second);
        textShowMap = getViewById(R.id.a_cs_text_action_bar_second_show_map);
        textShowList = getViewById(R.id.a_cs_text_action_bar_second_show_list);
        imageActionBarAnimation1 = getViewById(R.id.a_cs_image_action_bar_background_animation_1);
        imageActionBarAnimation2 = getViewById(R.id.a_cs_image_action_bar_background_animation_2);
        imageActionBar = getViewById(R.id.a_cs_image_action_bar_background);

    }

    private void assignListener() {
        imageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showExitDialog();
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                choseShop(position);
            }
        });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                updateArtistsList();
            }
        });

        textShowMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMap();
            }
        });

        textShowList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showList();
            }
        });

        imageSettingsOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSortDialog();
            }
        });
    }

    private void showMap() {
        TransitionManager.beginDelayedTransition(getCoordinatorLayout());
        relativeContainerMap.setVisibility(View.VISIBLE);
        swipeRefreshLayout.setVisibility(View.GONE);
        textShowList.setVisibility(View.VISIBLE);
        textShowMap.setVisibility(View.GONE);
        textShopNotFound.setVisibility(View.GONE);
        imageBouquet.setVisibility(View.GONE);
        textSort.setVisibility(View.GONE);
        imageSettingsOpen.setVisibility(View.GONE);
        if (mapIsNeverOpened) {
            Timer timer = new Timer();
            TimerMapZoom timerMapZoom = new TimerMapZoom();

            timer.schedule(timerMapZoom, 1000);

            mapIsNeverOpened = false;
        }
    }

    private void showList() {
        TransitionManager.beginDelayedTransition(getCoordinatorLayout());
        relativeContainerMap.setVisibility(View.GONE);
        swipeRefreshLayout.setVisibility(View.VISIBLE);
        textShowList.setVisibility(View.GONE);
        textShowMap.setVisibility(View.VISIBLE);
        imageSettingsOpen.setVisibility(View.VISIBLE);
        if (listAnswerFlex.size() != 0) {
            textShopNotFound.setVisibility(View.GONE);
        } else {
            textShopNotFound.setVisibility(View.VISIBLE);
        }
        imageBouquet.setVisibility(View.VISIBLE);
    }

    private void initView() {
        imageBack.setVisibility(View.VISIBLE);

        if (order.getBouquetItem() != null) {
            int size = this.getWindowWidth();
            Helper.loadImage(this, order.getBouquetItem().getImageUrl()).resize(size, size)
                    .centerCrop().into(imageBouquet);
        }

        listAnswerFlex = new ListAnswerFlex();
        listAnswerFlexAdapter = new ListAnswerFlexAdapter(this, listAnswerFlex);
        listView.setAdapter(listAnswerFlexAdapter);
    }

    private void initFaye() {
        MetaMessage metaMessageFixOrder = new MetaMessage();
        fayeClientOrder = new FayeClient(ServerConfig.SERVER_FAYE, metaMessageFixOrder);
        fayeClientOrder.setListener(new FayeClientListener() {
            @Override
            public void onConnectedServer(FayeClient fc) {
                String channel = ServerConfig.SERVER_FAYE_ORDER;
                channel = channel.replace(ServerConfig.SERVER_FAYE_ORDER_REPLACEMENT, Integer.toString(order.getId()));
                fc.subscribeChannel(channel);
            }

            @Override
            public void onDisconnectedServer(FayeClient fc) {

            }

            @Override
            public void onReceivedMessage(FayeClient fc, String msg) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        updateArtistsList();
                    }
                });
            }
        });
        fayeClientOrder.connectServer();
    }

    private void showExitDialog() {
        pulseScaleAnimation.stop();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                removeOrderRequest(order.getId());
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if (firstAnsver)
                    startAnimation();
            }
        });

        builder.setMessage(R.string.remove_order_dialog_text);

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void choseShop(int shopPosition) {
        order.setShopId(Integer.toString(listAnswerFlex.get(shopPosition).getShop().getId()));
        order.setPrice(listAnswerFlex.get(shopPosition).getPrice());
        DataController.getInstance().setOrder(order);
        goToPaymentActivity();
    }

    private void goToPaymentActivity() {
        startActivity(new Intent(this, PaymentTypeActivity.class));
    }

    private void updateArtistsList() {
        if (!swipeRefreshLayout.isRefreshing()) {
            startLoading();
        }
        WebMethods.getInstance().getFlexAnswers(order.getId(),
                new RequestListener<ListAnswerFlexResponse>() {
                    @Override
                    public void onRequestFailure(SpiceException spiceException) {
                        if (!swipeRefreshLayout.isRefreshing()) {
                            stopLoading();
                        } else {
                            swipeRefreshLayout.setRefreshing(false);
                        }
                    }

                    @Override
                    public void onRequestSuccess(ListAnswerFlexResponse listAnswerFlexResponse) {
                        if (!swipeRefreshLayout.isRefreshing()) {
                            stopLoading();
                        } else {
                            swipeRefreshLayout.setRefreshing(false);
                        }
                        listAnswerFlex.clear();
                        listAnswerFlex.addAll(listAnswerFlexResponse.getListAnswerFlex());
                        Collections.sort(listAnswerFlex, selectedAnswersComporator);
                        listAnswerFlexAdapter.notifyDataSetChanged();
                        if (!listAnswerFlex.isEmpty()) {
                            textShopNotFound.setVisibility(View.GONE);
                            imageSettingsOpen.setVisibility(View.VISIBLE);

                            if (firstAnsver) {
                                showList();
                                firstAnsver = false;
                                pulseScaleAnimation.stop();
                                textActionBarTitle.setText(R.string.text_chose_artist_bouquet);
                            }

                        } else {
                            textShopNotFound.setVisibility(View.VISIBLE);
                            imageSettingsOpen.setVisibility(View.INVISIBLE);
                        }
                        updateMapMarkers();
                    }
                });
    }

    private void removeOrderRequest(int orderId) {
        startLoading();
        WebMethods.getInstance().removeOrderRequest(orderId, new RequestListener<DefaultResponse>() {
            @Override
            public void onRequestFailure(SpiceException spiceException) {
                stopLoading();
                finish();
            }

            @Override
            public void onRequestSuccess(DefaultResponse defaultResponse) {
                stopLoading();
                finish();
            }
        });
    }

    private void getShopListRequest(int page, int perPage) {
        startLoading();
        WebMethods.getInstance().listShopGetRequest(page, perPage,
                new RequestListener<ShopListResponse>() {
                    @Override
                    public void onRequestFailure(SpiceException spiceException) {
                        stopLoading();
                    }

                    @Override
                    public void onRequestSuccess(ShopListResponse shopListResponse) {
                        stopLoading();
                        loadAllShopList(shopListResponse.getListShop(), shopListResponse.getMeta().getPagination());
                    }
                });
    }

    private void startLoadingShopList() {
        if (listShopAll == null)
            listShopAll = new ListShop();
        else
            listShopAll.clear();

        getShopListRequest(Pagination.FIRST_PAGE, Pagination.PER_PAGE);
    }

    private void loadAllShopList(ListShop listShop, Pagination pagination) {
        listShopAll.addAll(listShop);
        if (pagination.getNextPage() != 0) {
            getShopListRequest(pagination.getNextPage(), Pagination.PER_PAGE);
        } else {
            updateMapMarkers();
        }
    }

    @Override
    public void onBackPressed() {
        showExitDialog();
    }

    private String distToString(float distMeters) {
        if (distMeters >= 1000) {
            return Integer.toString((int) distMeters / 1000) + " " + getString(R.string.kilometer);
        } else {
            return Integer.toString((int) distMeters) + " " + getString(R.string.meter);
        }
    }

    private void showSortDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.chose_sort_type)
                .setItems(R.array.sort_types, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                selectedAnswersComporator = AnswerFlex.COMPARATOR_SORT_BY_PRICE;
                                break;
                            case 1:
                                selectedAnswersComporator = AnswerFlex.COMPARATOR_SORT_BY_DIST;
                                break;
                            case 2:
                                selectedAnswersComporator = AnswerFlex.COMPARATOR_SORT_BY_RATE;
                                break;
                        }
                        if (which == 0) {
                        } else {
                        }
                        Collections.sort(listAnswerFlex, selectedAnswersComporator);
                        listAnswerFlexAdapter.notifyDataSetChanged();
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    class TimerMapZoom extends TimerTask {

        @Override
        public void run() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (mapCenterLocation != null)
                        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(mapCenterLocation, 10), 45000, null);
                }
            });
        }
    }

    public class PulseScaleAnimation {
        private View view1;
        private View view2;
        private float scale;
        private int duration;
        private Thread threadAnimation;
        private AnimatorSet pulseAnimationSet1;
        private AnimatorSet pulseAnimationSet2;

        private boolean stop = false;

        private int durationBetveenWave = 666;

        public PulseScaleAnimation(View view1, View view2, float _scale, int _duration) {
            this.view1 = view1;
            this.view2 = view2;
            scale = _scale;
            duration = _duration;
        }

        public void start() {

            stop = false;

            final Handler handler = new Handler();

            final float startX = view1.getX();
            final float startY = view1.getY();
            final float endX = view1.getX();
            final float endY = view1.getY();

            ObjectAnimator scaleXAnimation1 = ObjectAnimator.ofFloat(view1, View.SCALE_X, scale);
            ObjectAnimator scaleYAnimation1 = ObjectAnimator.ofFloat(view1, View.SCALE_Y, scale);
            ObjectAnimator xAnimation1 = ObjectAnimator.ofFloat(view1, View.X, startX, endX);
            ObjectAnimator yAnimation1 = ObjectAnimator.ofFloat(view1, View.Y, startY, endY);
            ObjectAnimator alpha1 = ObjectAnimator.ofFloat(view1, View.ALPHA, 1f, 0f);

            ObjectAnimator scaleXAnimation2 = ObjectAnimator.ofFloat(view2, View.SCALE_X, scale);
            ObjectAnimator scaleYAnimation2 = ObjectAnimator.ofFloat(view2, View.SCALE_Y, scale);
            ObjectAnimator xAnimation2 = ObjectAnimator.ofFloat(view2, View.X, startX, endX);
            ObjectAnimator yAnimation2 = ObjectAnimator.ofFloat(view2, View.Y, startY, endY);
            ObjectAnimator alpha2 = ObjectAnimator.ofFloat(view2, View.ALPHA, 1f, 0f);

            scaleXAnimation1.setDuration(duration);
            scaleYAnimation1.setDuration(duration);
            xAnimation1.setDuration(duration);
            yAnimation1.setDuration(duration);
            alpha1.setDuration(duration);


            scaleXAnimation2.setDuration(duration);
            scaleYAnimation2.setDuration(duration);
            xAnimation2.setDuration(duration);
            yAnimation2.setDuration(duration);
            alpha2.setDuration(duration);

            pulseAnimationSet1 = new AnimatorSet();
            pulseAnimationSet2 = new AnimatorSet();
            pulseAnimationSet1.setDuration(duration);
            pulseAnimationSet2.setDuration(duration);

            pulseAnimationSet1.play(scaleXAnimation1)
                    .with(scaleYAnimation1)
                    .with(xAnimation1)
                    .with(yAnimation1)
                    .with(alpha1);

            pulseAnimationSet2.play(scaleXAnimation2)
                    .with(scaleYAnimation2)
                    .with(xAnimation2)
                    .with(yAnimation2)
                    .with(alpha2);

            pulseAnimationSet2.setStartDelay(durationBetveenWave);

            pulseAnimationSet2.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    if (!stop) {
                        pulseAnimationSet1.start();
                        pulseAnimationSet2.start();
                    } else {
                        pulseAnimationSet1.end();
                        pulseAnimationSet2.end();
                        if (view1 != null) {
                            view1.clearAnimation();
                            view1.animate().cancel();
                            view1.setVisibility(View.GONE);
                            view1.requestLayout();
                        }

                        if (view2 != null) {
                            view2.clearAnimation();
                            view2.animate().cancel();
                            view2.setVisibility(View.GONE);
                            view2.requestLayout();
                        }
                    }
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });

            pulseAnimationSet1.start();
            pulseAnimationSet2.start();
        }

        public void stop() {
            stop = true;
//            if (pulseAnimationSet1 != null)
//                pulseAnimationSet1.cancel();
//            if (pulseAnimationSet2 != null)
//                pulseAnimationSet2.cancel();
//
//            if (view1 != null) {
//                view1.clearAnimation();
//                view1.animate().cancel();
//                view1.setVisibility(View.GONE);
//                view1.requestLayout();
//            }
//
//            if (view2 != null) {
//                view2.clearAnimation();
//                view2.animate().cancel();
//                view2.setVisibility(View.GONE);
//                view2.requestLayout();
//            }
        }
    }

}
