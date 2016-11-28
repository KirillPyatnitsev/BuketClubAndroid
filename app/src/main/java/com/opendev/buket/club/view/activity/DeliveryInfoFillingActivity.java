package com.opendev.buket.club.view.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.PorterDuff;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.text.InputFilter;
import android.text.InputType;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.github.jjobes.slidedatetimepicker.SlideDateTimeListener;
import com.github.jjobes.slidedatetimepicker.SlideDateTimePicker;
import com.github.pinball83.maskededittext.MaskedEditText;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;
import com.opendev.buket.club.AlphaApiInterface;
import com.opendev.buket.club.DataController;
import com.opendev.buket.club.R;
import com.opendev.buket.club.consts.ServerConfig;
import com.opendev.buket.club.model.Order;
import com.opendev.buket.club.model.Profile;
import com.opendev.buket.club.model.lists.ListString;
import com.opendev.buket.club.tools.Helper;
import com.opendev.buket.club.tools.PreferenceCache;
import com.opendev.buket.club.web.WebMethods;
import com.opendev.buket.club.web.response.AlphaPayResponse;
import com.opendev.buket.club.web.response.OrderResponse;
import com.opendev.buket.club.web.response.PhoneCodeResponse;
import com.seatgeek.placesautocomplete.OnPlaceSelectedListener;
import com.seatgeek.placesautocomplete.PlacesAutocompleteTextView;
import com.seatgeek.placesautocomplete.model.Place;
import com.transitionseverywhere.TransitionManager;

import org.codehaus.jackson.map.util.ISO8601Utils;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

import okhttp3.ConnectionSpec;
import okhttp3.OkHttpClient;
import okhttp3.TlsVersion;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;


public class DeliveryInfoFillingActivity extends BaseActivity implements
        GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks, TextView.OnEditorActionListener {

    private static final String TAG = ServerConfig.TAG_PREFIX + "DlvInfoFillAct";

//    private ImageView imageBack;
//    private ImageView imageLogo;

    private Spinner spinnerDeliveryTime;
    private Spinner spinnerDeliveryType;
    private MaskedEditText editPhoneNumber;
    private EditText editName;
    private EditText editComment;
    private PlacesAutocompleteTextView editAddress;
   // private TextView textAddress;
    private RelativeLayout relativeAddressContainer;
    private ImageButton buttonMyLocation;

    private Button buttonNext;

    private Date currentDate;

    private ArrayList<String> deliveryTime = new ArrayList<>();
    private ArrayList<String> shippingTypes = new ArrayList<>();

    private ArrayAdapter<String> deliveryTimeAdapter;
    private ArrayAdapter<String> shippingTypeAdapter;

    Format formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private boolean reselection = false;

    private Place currentPlace = null;

    private String currentShippingType = Order.DELIVERY_TYPE_PICKUP;

    private GoogleApiClient mGoogleApiClient;

    private Location lastLocation;
    private String lastAddress;
    private Place lastPlace;
    private boolean lastLocationSelected = false;

    private String lastUserSelectedAddress;

    private Toolbar toolbar;
    private TextView deliveryTypeTitle;
    private TextView deliveryTimeTitle;

    private LocationManager mLocationManager;
    private long LOCATION_REFRESH_TIME = 10000;
    private float LOCATION_REFRESH_DISTANCE = 10;

    @Override
    protected void onCreateInternal() {
        setContentView(R.layout.activity_delivery_info_filling);

        assignView();
        initView();
        assignListener();

        final Profile profile = DataController.getInstance().getProfile();
        if (profile != null && profile.getPhone() != null
                && !profile.getPhone().isEmpty()) {
            String phone = profile.getPhone().replaceAll("[^\\d.]", "");
            phone = phone.substring(1);
            editPhoneNumber.setMaskedText(phone);
        }

        if (profile != null && profile.getFillName() != null
                && !profile.getFillName().isEmpty()){
            editName.setText(profile.getFillName());
        }

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Places.GEO_DATA_API)
                .enableAutoManage(this, 0, this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    @Override
    protected int getCoordinatorViewId() {
        return R.id.a_dif_coordinator_root;
    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    public void onBackPressed() {
        goToBackActivity();
    }

    private final void goToBackActivity() {
        startActivity(new Intent(this, BucketDetalisActivity.class));
        Helper.adjustTransition(DeliveryInfoFillingActivity.this);
        finish();
    }

    @Override
    public void onConnected(Bundle bundle) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            lastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            if (lastLocation != null)
                addressGetRequest(lastLocation.getLatitude(), lastLocation.getLongitude());
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    private void assignView() {
        //imageBack = getViewById(R.id.i_ab_image_back);
        //imageLogo = getViewById(R.id.i_ab_image_icon);

        toolbar = getViewById(R.id.delivery_toolbar);

        spinnerDeliveryTime = getViewById(R.id.a_dif_spinner_delivery_type_time);
        spinnerDeliveryType = getViewById(R.id.a_dif_spinner_delivery_type_place);
        editPhoneNumber = getViewById(R.id.a_dif_edit_phone);
        editName = getViewById(R.id.delivery_name_input);
        editComment = getViewById(R.id.a_dif_edit_comment);
        editAddress = getViewById(R.id.a_dif_edit_delivery_address);
       // textAddress = getViewById(R.id.a_dif_text_delivery_address);
        buttonNext = getViewById(R.id.a_dif_button_next);
        deliveryTimeTitle = getViewById(R.id.a_dif_spinner_delivery_type_time_text);
        deliveryTypeTitle = getViewById(R.id.delivery_type_title);


        relativeAddressContainer = getViewById(R.id.a_dif_relative_address_container);
        buttonMyLocation = getViewById(R.id.a_dif_button_my_location);
    }

    private void assignListener() {
//        imageBack.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                goToBackActivity();
//            }
//        });

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToBackActivity();
            }
        });

        buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean deliveryPickup = currentShippingType.equals(Order.DELIVERY_TYPE_PICKUP);

                boolean myLocation = lastLocationSelected && lastUserSelectedAddress.equals(editAddress.getText().toString());

                boolean editLocation = currentPlace != null && lastUserSelectedAddress.equals(editAddress.getText().toString());

                boolean dataIsDone = addDataToOrder();

                if (dataIsDone) {
                    if (deliveryPickup || myLocation || editLocation) {
                        goToNextActivity();
                    } else {
                        showSnackBar(R.string.delivery_info_address_error);
                    }
                } else {
                    showSnackBar(getString(R.string.delivery_info_error));
                }
            }
        });

        spinnerDeliveryType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                TransitionManager.beginDelayedTransition(getCoordinatorLayout());
                switch (position) {
                    case 0:
                        currentShippingType = Order.DELIVERY_TYPE_PICKUP;
                        relativeAddressContainer.setVisibility(View.GONE);
             //           textAddress.setVisibility(View.GONE);
                        break;
                    case 1:
                        currentShippingType = Order.DELIVERY_TYPE_ADDRESS;
                        relativeAddressContainer.setVisibility(View.VISIBLE);
                  //      textAddress.setVisibility(View.VISIBLE);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        editAddress.setOnPlaceSelectedListener(new OnPlaceSelectedListener() {
            @Override
            public void onPlaceSelected(Place place) {
                lastLocationSelected = false;
                lastUserSelectedAddress = editAddress.getText().toString();
                currentPlace = place;
                Places.GeoDataApi.getPlaceById(mGoogleApiClient, currentPlace.place_id)
                        .setResultCallback(new ResultCallback<PlaceBuffer>() {
                            @Override
                            public void onResult(PlaceBuffer places) {
                                if (places.getStatus().isSuccess()) {
                                    DataController.getInstance().getOrder().setAddressLat(places.get(0).getLatLng().latitude);
                                    DataController.getInstance().getOrder().setAddressLng(places.get(0).getLatLng().longitude);
                                }
                                places.release();
                            }
                        });
            }
        });

        spinnerDeliveryTime.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    currentDate = null;
                } else {
                    if (reselection) {
                        reselection = false;
                    } else {
                        new SlideDateTimePicker.Builder(getSupportFragmentManager())
                                .setListener(slideDateTimeListener)
                                .setInitialDate(new Date())
                                .setIs24HourTime(true)
                                .setMinDate(new Date())
                                .build()
                                .show();
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        buttonMyLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentPlace = null;
                editAddress.setText(lastAddress);
                lastUserSelectedAddress = lastAddress;
                lastLocationSelected = true;
                DataController.getInstance().getOrder().setAddressLat(lastLocation.getLatitude());
                DataController.getInstance().getOrder().setAddressLng(lastLocation.getLongitude());
            }
        });

        editComment.setOnEditorActionListener(this);
        editComment.setImeActionLabel("Ok", EditorInfo.IME_ACTION_DONE);



    }

    @Override
    protected void allProcessDone() {

    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        boolean handled = false;
        if (actionId == EditorInfo.IME_ACTION_DONE) {
            // обрабатываем нажатие кнопки

            return handled;
        }
        return handled;
    }

    private SlideDateTimeListener slideDateTimeListener = new SlideDateTimeListener() {

        @Override
        public void onDateTimeSet(Date date) {
            currentDate = date;
            deliveryTime.set(1, formatter.format(currentDate));
            deliveryTimeAdapter.notifyDataSetChanged();
//            spinnerDeliveryTime.setAdapter(deliveryTimeAdapter);
//            spinnerDeliveryTime.setSelection(1);
        }

        @Override
        public void onDateTimeCancel() {

        }
    };

    private void initView() {
//        imageBack.setVisibility(View.VISIBLE);
//        imageLogo.setVisibility(View.INVISIBLE);


        setSupportActionBar(toolbar);
        setTitle("Доставка");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        toolbar.setNavigationIcon(ContextCompat.getDrawable(this, R.drawable.ic_arrow_back));

        deliveryTime.add(getString(R.string.text_time_soon));

        if (DataController.getInstance().getOrder().getTimeDelivery() != null) {
            currentDate = ISO8601Utils.parse(DataController.getInstance().getOrder().getTimeDelivery());
            deliveryTime.add(formatter.format(currentDate));
        } else {
            currentDate = null;
            deliveryTime.add("Выбрать время");
        }

        deliveryTimeAdapter = new ArrayAdapter<>(this, R.layout.list_item_spiner, R.id.li_s_text, deliveryTime);
        deliveryTimeAdapter.setDropDownViewResource(R.layout.list_item_spiner2);

        spinnerDeliveryTime.setAdapter(deliveryTimeAdapter);

        spinnerDeliveryTime.setSelection(currentDate == null ? 0 : 1);

        shippingTypes = new ArrayList<>();

        shippingTypes.add(getString(R.string.pickup));
        shippingTypes.add(getString(R.string.delivery));

        String phone = DataController.getInstance().getPhone();
        String name = DataController.getInstance().getPhone();

        //editPhoneNumber.setMaskedText(phone);
        //editName.setText(name);

        shippingTypeAdapter = new ArrayAdapter<>(this, R.layout.list_item_spiner, R.id.li_s_text, shippingTypes);
        shippingTypeAdapter.setDropDownViewResource(R.layout.list_item_spiner2);
        spinnerDeliveryType.setAdapter(shippingTypeAdapter);
        spinnerDeliveryType.setSelection(1);


        editAddress.getBackground().setColorFilter(ContextCompat.getColor(this, R.color.black),
                PorterDuff.Mode.SRC_ATOP);



        /*TransitionManager.beginDelayedTransition(getCoordinatorLayout());
        deliveryTypeTitle.setVisibility(View.VISIBLE);
        spinnerDeliveryType.setVisibility(View.VISIBLE);
        deliveryTimeTitle.setVisibility(View.VISIBLE);
        spinnerDeliveryTime.setVisibility(View.VISIBLE);*/



    }

    private boolean addDataToOrder() {
        DataController.getInstance().getOrder().setRecipientPhone(editPhoneNumber.getText().toString());
        DataController.getInstance().getOrder().setAddress(lastUserSelectedAddress);
        DataController.getInstance().getOrder().setComment(editComment.getText().toString());
        DataController.getInstance().getOrder().setTimeDelivery(
                currentDate != null ? ISO8601Utils.format(currentDate) : null
        );
        DataController.getInstance().getOrder().setShippingType(currentShippingType);
        DataController.getInstance().getOrder().setShopId(null);
        DataController.getInstance().getOrder().setShop(null);
        return (!editPhoneNumber.getText().toString().isEmpty()
                && editPhoneNumber.getText().toString().replaceAll("[^\\d.]", "").length() == 11
                && (!editAddress.getText().toString().isEmpty() || currentShippingType.equals(Order.DELIVERY_TYPE_PICKUP)));

    }

    private void goToNextActivity() {
        switch (DataController.getInstance().getSession().getAppMode()) {
            case Profile.TYPE_PRICE_FIX:
                /*startActivity(new Intent(this, PaymentTypeActivity.class));*/
                //alphaPayRequest("buketfinder-api", "buketfinder", String.valueOf(8), "10000", "http://google.com", "http://yandex.ru");


                SSLContext sc = null;
                try {
                    sc = SSLContext.getInstance("TLSv1.2");
                    sc.init(null, null, null);
                } catch (Exception e) {
                    e.printStackTrace();
                }


                ConnectionSpec cs = new ConnectionSpec.Builder(ConnectionSpec.MODERN_TLS)
                        .tlsVersions(TlsVersion.TLS_1_2)
                        .build();

                List specs = new ArrayList<>();
                specs.add(cs);
                specs.add(ConnectionSpec.COMPATIBLE_TLS);
                specs.add(ConnectionSpec.CLEARTEXT);

                OkHttpClient client1;




                    client1 = new OkHttpClient.Builder()
                            .sslSocketFactory(new Tls12SocketFactory(sc.getSocketFactory()))
                            .connectionSpecs(specs)
                            .followRedirects(true)
                            .followSslRedirects(true)
                            .build();


                Retrofit client;
                if (Build.VERSION.SDK_INT >= 16 && Build.VERSION.SDK_INT < 22) {
                    client = new Retrofit.Builder()
                            .baseUrl("https://test.paymentgate.ru/testpayment/rest/")
                            .addConverterFactory(ScalarsConverterFactory.create())
                            .addConverterFactory(GsonConverterFactory.create())
                            .client(client1)
                            .build();
                } else {
                    client = new Retrofit.Builder()
                            .baseUrl("https://test.paymentgate.ru/testpayment/rest/")
                            .addConverterFactory(ScalarsConverterFactory.create())
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();
                }


                AlphaApiInterface service = client.create(AlphaApiInterface.class);
                Random rand = new Random();
                Call<AlphaPayResponse> call = service.pay("buketfinder-api", "buketfinder", String.valueOf(rand.nextInt(100) + 350),
                        "100000", "http://5.101.120.246/uxsystem/return_url.html", "http://5.101.120.246/uxsystem/fail_url.html", "MOBILE");
                startLoading();
                call.enqueue(new Callback<AlphaPayResponse>() {
                    @Override
                    public void onResponse(Call<AlphaPayResponse> call, Response<AlphaPayResponse> response) {
                        Log.d("AlphaDebug", response.body().toString());
                        if (response.isSuccessful()){
                            if (response.body().getErrorCode() == null){
                                stopLoading();
                                Intent intent = new Intent(getBaseContext(), AlphaPayActivity.class);
                                intent.putExtra("url", response.body().getFormUrl());
                                startActivity(intent);
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<AlphaPayResponse> call, Throwable t) {
                        Log.d("AlphaDebugErr", t.getLocalizedMessage());
                        stopLoading();
                    }
                });




                break;
            case Profile.TYPE_PRICE_FLEXIBLE:
                startActivity(new Intent(this, ChoseShopActivity.class));
                break;
        }
    }




    public class Tls12SocketFactory extends SSLSocketFactory {
        private final String[] TLS_V12_ONLY = {"TLSv1.2"};

        final SSLSocketFactory delegate;

        public Tls12SocketFactory(SSLSocketFactory base) {
            this.delegate = base;
        }

        @Override
        public String[] getDefaultCipherSuites() {
            return delegate.getDefaultCipherSuites();
        }

        @Override
        public String[] getSupportedCipherSuites() {
            return delegate.getSupportedCipherSuites();
        }

        @Override
        public Socket createSocket(Socket s, String host, int port, boolean autoClose) throws IOException {
            return patch(delegate.createSocket(s, host, port, autoClose));
        }

        @Override
        public Socket createSocket(String host, int port) throws IOException, UnknownHostException {
            return patch(delegate.createSocket(host, port));
        }

        @Override
        public Socket createSocket(String host, int port, InetAddress localHost, int localPort) throws IOException, UnknownHostException {
            return patch(delegate.createSocket(host, port, localHost, localPort));
        }

        @Override
        public Socket createSocket(InetAddress host, int port) throws IOException {
            return patch(delegate.createSocket(host, port));
        }

        @Override
        public Socket createSocket(InetAddress address, int port, InetAddress localAddress, int localPort) throws IOException {
            return patch(delegate.createSocket(address, port, localAddress, localPort));
        }

        private Socket patch(Socket s) {
            if (s instanceof SSLSocket) {
                ((SSLSocket) s).setEnabledProtocols(TLS_V12_ONLY);
            }
            return s;
        }
    }



    private void alphaPayRequest(String username, String password, String orderNumber, String amount, String returnUrl, String failUrl){
        WebMethods.getInstance().alphaPayRequest(username, password, orderNumber, amount, returnUrl, failUrl, new RequestListener<AlphaPayResponse>() {
            @Override
            public void onRequestFailure(SpiceException spiceException) {
                Log.d("AlphaDebugErr", spiceException.getLocalizedMessage());
            }

            @Override
            public void onRequestSuccess(AlphaPayResponse alphaPayResponse) {
                Log.d("AlphaDebug", alphaPayResponse.getFormUrl());
            }
        });
    }

    private void addressGetRequest(double latitude, double longitude) {
        WebMethods.getInstance().addressGetRequest(latitude, longitude, this, new RequestListener<String>() {
            @Override
            public void onRequestFailure(SpiceException spiceException) {

            }

            @Override
            public void onRequestSuccess(String s) {
                lastAddress = s;
                buttonMyLocation.setVisibility(View.VISIBLE);
            }
        });
    }

    private void phoneVerification(String phone) {
//        ListString listPhone = PreferenceCache.getObject(this, PreferenceCache.SAVED_PHONES, ListString.class);
//
//        if (listPhone != null && listPhone.contains(phone)){
//            goToNextActivity();
//        }else{
//            phoneVerificationStartPostRequest(phone);
//        }

       // phoneVerificationStartPostRequest(phone);

    }

    private void phoneVerificationStartPostRequest(final String phone) {
        WebMethods.getInstance().phoneVerificationStartPostRequest(phone,
                new RequestListener<PhoneCodeResponse>() {
                    @Override
                    public void onRequestFailure(SpiceException spiceException) {
                        showSnackBar(R.string.phone_verification_error);
                    }

                    @Override
                    public void onRequestSuccess(PhoneCodeResponse phoneCodeResponse) {
                        if (phoneCodeResponse.getPhoneVerification() == null) {
                          //  showEnterCodeDialog(phone);
                        } else {
                            sendOrder(phone, phoneCodeResponse.getPhoneVerification().getCode(), null, null);
                        }
                    }
                });
    }

    private void savePhone(String phone) {
        ListString listPhone = PreferenceCache.getObject(this, PreferenceCache.SAVED_PHONES, ListString.class);
        if (listPhone == null) {
            listPhone = new ListString();
        }
        listPhone.add(phone);
        PreferenceCache.putObject(this, PreferenceCache.SAVED_PHONES, listPhone);
    }

    private void showEnterCodeDialog(final String phone) {
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        int maxLength = 4;
        input.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxLength)});

        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle(R.string.code_write_title)
                .setMessage(R.string.code_write_second)
                .setView(input)
                .setPositiveButton(R.string.text_done, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setNegativeButton(getString(R.string.cancel), null);

        final AlertDialog alertDialog = builder.create();

        alertDialog.show();

        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code = input.getText().toString();
                if (code.length() == 4) {
                    sendOrder(phone, code, alertDialog, input);
                } else {
                    showSnackBar(R.string.code_is_not_write);
                }
            }
        });

    }

    private void sendOrder(final String phone, final String code, final AlertDialog alertDialog, final EditText input) {
        startLoading();

        final Order order = DataController.getInstance().getOrder();
        order.setCode(code);
        final Order serverOrder = order.getOrderForServer();
        Log.d(TAG, "Sending new order: " + serverOrder);

        WebMethods.getInstance().sendOrder(serverOrder,
                new RequestListener<OrderResponse>() {
                    @Override
                    public void onRequestFailure(SpiceException spiceException) {
                        if (input != null) {
                            input.setText("");
                        }
                        showToast("Код неверен");
                        stopLoading();
                    }

                    @Override
                    public void onRequestSuccess(OrderResponse orderResponse) {
                        stopLoading();
                        if (alertDialog != null) {
                            alertDialog.dismiss();
                        }
                        orderResponse.getOrder().setBouquetItemId(orderResponse.getOrder().getBouquetItem().getId());
                        DataController.getInstance().setOrder(orderResponse.getOrder());
                        savePhone(phone);
                    }
                });
    }



    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }




}
