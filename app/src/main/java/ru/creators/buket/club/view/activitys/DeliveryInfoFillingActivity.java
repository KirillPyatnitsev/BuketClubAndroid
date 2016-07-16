package ru.creators.buket.club.view.activitys;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.InputFilter;
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.jjobes.slidedatetimepicker.SlideDateTimeListener;
import com.github.jjobes.slidedatetimepicker.SlideDateTimePicker;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;
import com.seatgeek.placesautocomplete.OnPlaceSelectedListener;
import com.seatgeek.placesautocomplete.PlacesAutocompleteTextView;
import com.seatgeek.placesautocomplete.model.Place;
import com.transitionseverywhere.TransitionManager;

import org.codehaus.jackson.map.util.ISO8601Utils;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import fr.ganfra.materialspinner.MaterialSpinner;
import ru.creators.buket.club.DataController;
import ru.creators.buket.club.R;
import ru.creators.buket.club.model.Order;
import ru.creators.buket.club.model.Profile;
import ru.creators.buket.club.model.lists.ListString;
import ru.creators.buket.club.tools.PreferenceCache;
import ru.creators.buket.club.web.WebMethods;
import ru.creators.buket.club.web.response.OrderResponse;
import ru.creators.buket.club.web.response.PhoneCodeResponse;

public class DeliveryInfoFillingActivity extends BaseActivity implements
        GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks {

    private ImageView imageBack;
    private ImageView imageLogo;

    private MaterialSpinner spinnerDeliveryTime;
    private MaterialSpinner spinnerDeliveryType;
    private EditText editPhoneNumber;
    private EditText editComment;
    private PlacesAutocompleteTextView editAddress;
    private TextView textAddress;
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

    private LocationManager mLocationManager;
    private long LOCATION_REFRESH_TIME = 10000;
    private float LOCATION_REFRESH_DISTANCE = 10;

    @Override
    protected void onCreateInternal() {
        setContentView(R.layout.activity_delivery_info_filling);

        assignView();
        initView();
        assignListener();

        if (DataController.getInstance().getProfile() != null && DataController.getInstance().getProfile().getPhone() != null
                && !DataController.getInstance().getProfile().getPhone().isEmpty()) {
            String phone = DataController.getInstance().getProfile().getPhone().replaceAll("[^\\d.]", "");

            phone = phone.substring(1);

            editPhoneNumber.setText(phone);
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
        imageBack = getViewById(R.id.i_ab_image_back);
        imageLogo = getViewById(R.id.i_ab_image_icon);

        spinnerDeliveryTime = getViewById(R.id.a_dif_spinner_delivery_type_time);
        spinnerDeliveryType = getViewById(R.id.a_dif_spinner_delivery_type_place);
        editPhoneNumber = getViewById(R.id.a_dif_edit_phone);
        editComment = getViewById(R.id.a_dif_edit_comment);
        editAddress = getViewById(R.id.a_dif_edit_delivery_address);
        textAddress = getViewById(R.id.a_dif_text_delivery_address);
        buttonNext = getViewById(R.id.a_dif_button_next);

        relativeAddressContainer = getViewById(R.id.a_dif_relative_address_container);
        buttonMyLocation = getViewById(R.id.a_dif_button_my_location);
    }

    private void assignListener() {
        imageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
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
                        sendOrder();
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
                        textAddress.setVisibility(View.GONE);
                        break;
                    case 1:
                        currentShippingType = Order.DELIVERY_TYPE_ADDRESS;
                        relativeAddressContainer.setVisibility(View.VISIBLE);
                        textAddress.setVisibility(View.VISIBLE);
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
                                .setIndicatorColor(getResources().getColor(R.color.yellow))
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
    }

    private SlideDateTimeListener slideDateTimeListener = new SlideDateTimeListener() {

        @Override
        public void onDateTimeSet(Date date) {
            reselection = true;
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
        imageBack.setVisibility(View.VISIBLE);
        imageLogo.setVisibility(View.INVISIBLE);

        deliveryTime.add(getString(R.string.text_time_soon));

        if (DataController.getInstance().getOrder().getTimeDelivery() != null) {
            currentDate = ISO8601Utils.parse(DataController.getInstance().getOrder().getTimeDelivery());
            deliveryTime.add(formatter.format(currentDate));
        } else {
            currentDate = null;
            deliveryTime.add("Выбрать время");
        }

        deliveryTimeAdapter = new ArrayAdapter<>(this, R.layout.list_item_spiner, R.id.li_s_text, deliveryTime);

        spinnerDeliveryTime.setAdapter(deliveryTimeAdapter);

        spinnerDeliveryTime.setSelection(currentDate == null ? 0 : 1);

        shippingTypes = new ArrayList<>();

        shippingTypes.add(getString(R.string.pickup));
        shippingTypes.add(getString(R.string.delivery));

        shippingTypeAdapter = new ArrayAdapter<>(this, R.layout.list_item_spiner, R.id.li_s_text, shippingTypes);
        spinnerDeliveryType.setAdapter(shippingTypeAdapter);
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
                startActivity(new Intent(this, PaymentTypeActivity.class));
                break;
            case Profile.TYPE_PRICE_FLEXIBLE:
                startActivity(new Intent(this, ChoseShopActivity.class));
                break;
        }
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

        phoneVerificationStartPostRequest(phone);

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
                            showEnterCodeDialog(phone);
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
        startLoading(false);

        DataController.getInstance().getOrder().setCode(code);

        WebMethods.getInstance().sendOrder(DataController.getInstance().getOrder().getOrderForServer(),
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
                        goToNextActivity();
                    }
                });
    }

    private void sendOrder() {
        startLoading(false);

        WebMethods.getInstance().sendOrder(DataController.getInstance().getOrder().getOrderForServer(),
                new RequestListener<OrderResponse>() {
                    @Override
                    public void onRequestFailure(SpiceException spiceException) {
                        stopLoading();
                    }

                    @Override
                    public void onRequestSuccess(OrderResponse orderResponse) {
                        stopLoading();
                        orderResponse.getOrder().setBouquetItemId(orderResponse.getOrder().getBouquetItem().getId());
                        DataController.getInstance().setOrder(orderResponse.getOrder());
                        goToNextActivity();
                    }
                });
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }
}
