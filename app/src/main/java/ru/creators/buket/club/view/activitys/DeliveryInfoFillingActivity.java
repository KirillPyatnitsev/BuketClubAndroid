package ru.creators.buket.club.view.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.jjobes.slidedatetimepicker.SlideDateTimeListener;
import com.github.jjobes.slidedatetimepicker.SlideDateTimePicker;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.drive.Drive;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
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

public class DeliveryInfoFillingActivity extends BaseActivity implements
        GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks{

    private ImageView imageBack;
    private ImageView imageLogo;

    private MaterialSpinner spinnerDeliveryTime;
    private MaterialSpinner spinnerDeliveryType;
    private EditText editRecipientName;
    private EditText editPhoneNumber;
    private EditText editComment;
    private PlacesAutocompleteTextView editAddress;
    private TextView textAddress;

    private Button buttonNext;

    private Date currentDate;

    private ArrayList<String> deliveryTime = new ArrayList<>();
    private ArrayList<String> shippingTypes = new ArrayList<>();

    private ArrayAdapter<String> delivetyTimeAdapter;
    private ArrayAdapter<String> shippingTypeAdapter;

    Format formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private boolean reseliction = false;

    private Place currentPlace = null;

    private String currentShippingType = Order.DELIVERY_TYPE_PICKUP;

    private GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_info_filling);

        assignView();
        initView();
        assignListener();


        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Places.GEO_DATA_API)
                .enableAutoManage(this, 0, this)
                .addConnectionCallbacks(this)
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
        editRecipientName = getViewById(R.id.a_dif_edit_recipient);
        editPhoneNumber = getViewById(R.id.a_dif_edit_phone);
        editComment = getViewById(R.id.a_dif_edit_comment);
        editAddress = getViewById(R.id.a_dif_edit_delivery_address);
        textAddress = getViewById(R.id.a_dif_text_delivery_address);

        buttonNext = getViewById(R.id.a_dif_button_next);
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
                if (currentShippingType.equals(Order.DELIVERY_TYPE_ADDRESS) && currentPlace == null)
                    showSnackBar(getString(R.string.delivery_info_address_error));
                else if (addDataToOrder())
                    goToNextActivity();
                else
                    showSnackBar(getString(R.string.delivery_info_error));

            }
        });

        spinnerDeliveryType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                TransitionManager.beginDelayedTransition(getCoordinatorLayout());
                switch (position) {
                    case 0:
                        currentShippingType = Order.DELIVERY_TYPE_PICKUP;
                        editAddress.setVisibility(View.GONE);
                        textAddress.setVisibility(View.GONE);
                        break;
                    case 1:
                        currentShippingType = Order.DELIVERY_TYPE_ADDRESS;
                        editAddress.setVisibility(View.VISIBLE);
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
                    if (!reseliction) {
                        new SlideDateTimePicker.Builder(getSupportFragmentManager())
                                .setListener(slideDateTimeListener)
                                .setInitialDate(new Date())
                                .setIs24HourTime(true)
                                .setMinDate(new Date())
                                .setIndicatorColor(getResources().getColor(R.color.yellow))
                                .build()
                                .show();
                    } else {
                        reseliction = false;
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private SlideDateTimeListener slideDateTimeListener = new SlideDateTimeListener() {

        @Override
        public void onDateTimeSet(Date date) {
            reseliction = true;
            currentDate = date;
            deliveryTime.set(1, formatter.format(currentDate));
            delivetyTimeAdapter.notifyDataSetChanged();
//            spinnerDeliveryTime.setAdapter(delivetyTimeAdapter);
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

//        editAddress.setText(DataController.getInstance().getOrder().getAddress());

        delivetyTimeAdapter = new ArrayAdapter<>(this,  R.layout.list_item_spiner, R.id.li_s_text, deliveryTime);

        spinnerDeliveryTime.setAdapter(delivetyTimeAdapter);

        spinnerDeliveryTime.setSelection(currentDate == null ? 0 : 1);

        shippingTypes = new ArrayList<>();

        shippingTypes.add(getString(R.string.pickup));
        shippingTypes.add(getString(R.string.delivery));

        shippingTypeAdapter = new ArrayAdapter<String>(this,  R.layout.list_item_spiner, R.id.li_s_text, shippingTypes);
        spinnerDeliveryType.setAdapter(shippingTypeAdapter);
    }

    private boolean addDataToOrder() {
        DataController.getInstance().getOrder().setRecipientPhone(editPhoneNumber.getText().toString());
        DataController.getInstance().getOrder().setRecipientName(editRecipientName.getText().toString());
        DataController.getInstance().getOrder().setAddress(editAddress.getText().toString());
        DataController.getInstance().getOrder().setComment(editComment.getText().toString());
        DataController.getInstance().getOrder().setTimeDelivery(
                currentDate != null ? ISO8601Utils.format(currentDate) : null
        );
        DataController.getInstance().getOrder().setShippingType(currentShippingType);
        DataController.getInstance().getOrder().setShopId(null);
        DataController.getInstance().getOrder().setShop(null);
        return (!editPhoneNumber.getText().toString().isEmpty()
                && editPhoneNumber.getText().toString().replaceAll("[^\\d.]", "").length() == 11
                && !editRecipientName.getText().toString().isEmpty()
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
}
