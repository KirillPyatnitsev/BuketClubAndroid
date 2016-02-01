package ru.creators.buket.club.view.activitys;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
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
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import java.util.ArrayList;
import java.util.List;

import ru.creators.buket.club.DataController;
import ru.creators.buket.club.R;
import ru.creators.buket.club.consts.ServerConfig;
import ru.creators.buket.club.model.AnswerFlex;
import ru.creators.buket.club.model.Order;
import ru.creators.buket.club.model.lists.ListAnswerFlex;
import ru.creators.buket.club.tools.Helper;
import ru.creators.buket.club.view.adapters.ListAnswerFlexAdapter;
import ru.creators.buket.club.web.WebMethods;
import ru.creators.buket.club.web.response.DefaultResponse;
import ru.creators.buket.club.web.response.ListAnswerFlexResponse;
import ru.creators.buket.club.web.response.OrderResponse;

public class ChoseShopActivity extends BaseActivity implements OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener {

    private ListAnswerFlex listAnswerFlex;
    private ListAnswerFlexAdapter listAnswerFlexAdapter;

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

    private List<Marker> listMarker;

    private String MARKER_BID_PRICE;
    private String MARKER_STORE;

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
        listMarker = new ArrayList<>();


        sendOrder();
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
        choseShop(listMarker.indexOf(marker));
    }

    @Override
    protected int getCoordinatorViewId() {
        return R.id.a_cs_coordinator_root;
    }

    private void showShops(ListAnswerFlex listAnswerFlex) {
        googleMap.clear();
        listMarker.clear();
        for (AnswerFlex answerFlex : listAnswerFlex) {
            listMarker.add(googleMap.addMarker(new MarkerOptions()
                    .position(new LatLng(answerFlex.getShop().getAddressLat(), answerFlex.getShop().getAddressLng()))
                    .title(MARKER_BID_PRICE + " " + Helper.getStringWithCostPrefix(answerFlex.getPrice(), this))
                    .snippet(MARKER_STORE + " " + answerFlex.getShop().getName())));
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
                } else {
                    textShopNotFound.setVisibility(View.VISIBLE);
                }
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
                        listAnswerFlexAdapter.notifyDataSetChanged();
                        if (listAnswerFlex.size() != 0) {
                            textShopNotFound.setVisibility(View.GONE);
                        }
                        showShops(listAnswerFlex);
                    }
                });
    }

    private void removeOrderRequest(int orderId){
        startLoading();
        WebMethods.getInstance().removeOrderRequest(DataController.getInstance().getSession().getAccessToken(), orderId, new RequestListener<DefaultResponse>() {
            @Override
            public void onRequestFailure(SpiceException spiceException) {
                stopLoading();
                onBackPressed();
            }

            @Override
            public void onRequestSuccess(DefaultResponse defaultResponse) {
                stopLoading();
                onBackPressed();
            }
        });
    }
}
