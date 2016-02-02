package ru.creators.buket.club.view.activitys;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

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
import ru.creators.buket.club.web.response.OrderResponse;
import ru.creators.buket.club.web.response.ShopListResponse;

public class ChoseShopActivity extends BaseActivity implements
        OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

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


    private List<Marker> listMarkerAnsweredShops;
    private List<Marker> listMarkerNotAnsweredShops;

    private String MARKER_BID_PRICE;
    private String MARKER_STORE;

    private Pagination paginationShopGetList;

    private GoogleApiClient mGoogleApiClient;

    private Location mLastLocation;

    private Comparator<AnswerFlex> selectedAnswersComporator = AnswerFlex.COMPARATOR_SORT_BY_PRICE;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
            sendOrder();
        }
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
    public void onConnected(Bundle connectionHint) {
        if (ActivityCompat
                .checkSelfPermission(this,
                        Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);


        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(this, Locale.getDefault());

        try {
            addresses = geocoder.getFromLocation(mLastLocation.getLatitude(), mLastLocation.getLongitude(), 1);
            DataController.getInstance().getOrder().setAddress(addresses.get(0).getAddressLine(0));
            DataController.getInstance().getOrder().setAddressLat(mLastLocation.getLatitude());
            DataController.getInstance().getOrder().setAddressLng(mLastLocation.getLongitude());
        } catch (IOException e) {
            e.printStackTrace();
        }
        sendOrder();
        updateMapMarkers();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    private void initMap() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.a_cs_map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            if (DataController.getInstance().getOrder().getShippingType().equals(Order.DELIVERY_TYPE_PICKUP))
                googleMap.setMyLocationEnabled(true);

            // Getting LocationManager object from System Service LOCATION_SERVICE
            LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

            // Creating a criteria object to retrieve provider
            Criteria criteria = new Criteria();

            // Getting the name of the best provider
            String provider = locationManager.getBestProvider(criteria, true);

            // Getting Current Location
            Location location = locationManager.getLastKnownLocation(provider);

            if (location != null) {
                // Getting latitude of the current location
                double latitude = location.getLatitude();

                // Getting longitude of the current location
                double longitude = location.getLongitude();

                // Creating a LatLng object for the current location
                LatLng latLng = new LatLng(latitude, longitude);
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10.0f));
            }
        }

        googleMap.setOnInfoWindowClickListener(this);
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

    private void updateMapMarkers() {
        googleMap.clear();
        listMarkerNotAnsweredShops.clear();
        listMarkerAnsweredShops.clear();
        if (listAnswerFlex != null && listShopAll != null
                && (mLastLocation != null || DataController.getInstance().getOrder().getShippingType().equals(Order.DELIVERY_TYPE_ADDRESS))) {

            double lat;
            double lng;

            if (DataController.getInstance().getOrder().getShippingType().equals(Order.DELIVERY_TYPE_ADDRESS)) {
                googleMap.addMarker(new MarkerOptions()
                        .position(new LatLng(DataController.getInstance().getOrder().getAddressLat(),
                                DataController.getInstance().getOrder().getAddressLng()))
                        .title("Место доставки заказа.")
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.finish_ico))
                        .snippet(DataController.getInstance().getOrder().getAddress()));
                lat = DataController.getInstance().getOrder().getAddressLat();
                lng = DataController.getInstance().getOrder().getAddressLng();
            } else {
                lat = mLastLocation.getLatitude();
                lng = mLastLocation.getLongitude();
            }


            for (AnswerFlex answerFlex : listAnswerFlex) {
                listShopAll.removeByShopId(answerFlex.getShop().getId());
                answerFlex.setDistance(Helper.distFrom((float) lat, (float) lng
                        , answerFlex.getShop().getAddressLat(), answerFlex.getShop().getAddressLng()));
                listMarkerAnsweredShops.add(googleMap.addMarker(new MarkerOptions()
                        .position(new LatLng(answerFlex.getShop().getAddressLat(), answerFlex.getShop().getAddressLng()))
                        .title(MARKER_BID_PRICE + " " + Helper.getStringWithCostPrefix(answerFlex.getPrice(), this) + ", " +
                                distToString(answerFlex.getDistance()))
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.pin))
                        .snippet(MARKER_STORE + " " + answerFlex.getShop().getName())));
            }

            listAnswerFlexAdapter.notifyDataSetChanged();

            for (Shop shop : listShopAll) {
                if (shop != null)
                    listMarkerNotAnsweredShops.add(googleMap.addMarker(new MarkerOptions()
                            .position(new LatLng(shop.getAddressLat(), shop.getAddressLng()))
                            .title(MARKER_STORE + " " + shop.getName())
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.pin_gray))
                            .snippet("Расстояние: " + distToString(Helper.distFrom((float) lat, (float) lng
                                    , shop.getAddressLat(), shop.getAddressLng())))));
            }
        }
    }

    private void assignView() {
        imageBouquet = getViewById(R.id.a_cs_image_bouquet);
        imageBack = getViewById(R.id.i_ab_image_back);
        listView = getViewById(R.id.a_cs_list_view_artists);
        relativeContainerMap = getViewById(R.id.a_cs_relative_container_map);
        imageSettingsOpen = getViewById(R.id.i_ab_image_settings_open);
        imageSettingsOpen.setImageResource(R.drawable.searchmap_ico);
        imageSettingsClose = getViewById(R.id.i_ab_image_settings_close);

        textShopNotFound = getViewById(R.id.a_cs_text_shop_not_found);

        swipeRefreshLayout = getViewById(R.id.a_cs_swipe_refresh);

        textSort = getViewById(R.id.a_o_text_action_bar_second);
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

        imageSettingsOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                relativeContainerMap.setVisibility(View.VISIBLE);
                swipeRefreshLayout.setVisibility(View.GONE);
                imageSettingsClose.setVisibility(View.VISIBLE);
                imageSettingsOpen.setVisibility(View.GONE);
                textShopNotFound.setVisibility(View.GONE);
                imageBouquet.setVisibility(View.GONE);
                textSort.setVisibility(View.GONE);
            }
        });

        imageSettingsClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                relativeContainerMap.setVisibility(View.GONE);
                swipeRefreshLayout.setVisibility(View.VISIBLE);
                imageSettingsClose.setVisibility(View.GONE);
                imageSettingsOpen.setVisibility(View.VISIBLE);
                if (listAnswerFlex.size() != 0) {
                    textShopNotFound.setVisibility(View.GONE);
                    textSort.setVisibility(View.VISIBLE);
                } else {
                    textShopNotFound.setVisibility(View.VISIBLE);
                    textSort.setVisibility(View.GONE);
                }
                imageBouquet.setVisibility(View.VISIBLE);
            }
        });

        textSort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSortDialog();
            }
        });
    }

    private void initView() {
        imageBack.setVisibility(View.VISIBLE);
        imageSettingsOpen.setVisibility(View.VISIBLE);

        WebMethods.getInstance().loadImage(this, Helper.addServerPrefix(order.getBouquetItem().getImageUrl()), imageBouquet);

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
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                removeOrderRequest(order.getId());
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
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

    private void sendOrder() {
        startLoading(false);

        WebMethods.getInstance().sendOrder(DataController.getInstance().getSession().getAccessToken(),
                order.getOrderForServer(),
                new RequestListener<OrderResponse>() {
                    @Override
                    public void onRequestFailure(SpiceException spiceException) {
                        showSnackBar("Ошибка создания заказа");
                        stopLoading();
                    }

                    @Override
                    public void onRequestSuccess(OrderResponse orderResponse) {
                        order = orderResponse.getOrder();
                        order.setBouquetItemId(order.getBouquetItem().getId());
                        initFaye();
                        stopLoading();
                    }
                });
    }

    private void updateArtistsList() {
        if (!swipeRefreshLayout.isRefreshing())
            startLoading(false);
        WebMethods.getInstance().getFlexAnswers(
                DataController.getInstance().getSession().getAccessToken(), order.getId(), new RequestListener<ListAnswerFlexResponse>() {
                    @Override
                    public void onRequestFailure(SpiceException spiceException) {
                        if (!swipeRefreshLayout.isRefreshing())
                            stopLoading();
                        else
                            swipeRefreshLayout.setRefreshing(false);
                    }

                    @Override
                    public void onRequestSuccess(ListAnswerFlexResponse listAnswerFlexResponse) {
                        if (!swipeRefreshLayout.isRefreshing())
                            stopLoading();
                        else
                            swipeRefreshLayout.setRefreshing(false);
                        listAnswerFlex.clear();
                        listAnswerFlex.addAll(listAnswerFlexResponse.getListAnswerFlex());
                        Collections.sort(listAnswerFlex, selectedAnswersComporator);
                        listAnswerFlexAdapter.notifyDataSetChanged();
                        if (listAnswerFlex.size() != 0) {
                            textShopNotFound.setVisibility(View.GONE);
                            textSort.setVisibility(View.VISIBLE);
                        }
                        updateMapMarkers();
                    }
                });
    }

    private void removeOrderRequest(int orderId) {
        startLoading();
        WebMethods.getInstance().removeOrderRequest(DataController.getInstance().getSession().getAccessToken(), orderId, new RequestListener<DefaultResponse>() {
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
        WebMethods.getInstance().listShopGetRequest(DataController.getInstance().getSession().getAccessToken(),
                page, perPage, new RequestListener<ShopListResponse>() {
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
                        if (which == 0) {
                            selectedAnswersComporator = AnswerFlex.COMPARATOR_SORT_BY_PRICE;
                        } else {
                            selectedAnswersComporator = AnswerFlex.COMPARATOR_SORT_BY_DIST;
                        }
                        Collections.sort(listAnswerFlex, selectedAnswersComporator);
                        listAnswerFlexAdapter.notifyDataSetChanged();
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
