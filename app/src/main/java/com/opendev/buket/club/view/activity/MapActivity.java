package com.opendev.buket.club.view.activity;

import android.Manifest;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.opendev.buket.club.AppException;
import com.opendev.buket.club.DataController;
import com.opendev.buket.club.R;
import com.opendev.buket.club.model.Order;
import com.transitionseverywhere.TransitionManager;

import java.io.IOException;
import java.util.List;

public class MapActivity extends BaseActivity implements OnMapReadyCallback, GoogleMap.OnMapClickListener, GoogleMap.OnMarkerClickListener {

    private GoogleMap mMap;
    private LatLng latLng;
    private MarkerOptions markerOptions;

    private static final String[] INITIAL_PERMS={
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.READ_CONTACTS
    };

    private static final String[] LOCATION_PERMS={
            Manifest.permission.ACCESS_FINE_LOCATION
    };
    private static final int INITIAL_REQUEST=1337;
    private static final int CAMERA_REQUEST=INITIAL_REQUEST+1;
    private static final int CONTACTS_REQUEST=INITIAL_REQUEST+2;
    private static final int LOCATION_REQUEST=INITIAL_REQUEST+3;

//    private View viewActonBarFilter;
//    private ImageView imageBack;
//    private ImageView imageOpenFilter;
//    private ImageView imageCloseFilter;
//    private ImageView imageActionBarBackground;
//
//    private EditText editAddress;
    private String address = null;
//
//    private Button buttonNext;
//
//    private TextView textActionBar;
//
    private LatLng currentLatLng = null;
    private GeocoderTask geocoderTask = new GeocoderTask();

    @Override
    protected void onCreateInternal() {
        setContentView(R.layout.activity_map);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        assignView();


       // initView();
       // assignListener();
    }

    @Override
    protected int getCoordinatorViewId() {
        return R.id.a_m_coordinator_root;
    }

    private void assignView() {
//        imageBack = getViewById(R.id.i_ab_image_back);
//        imageOpenFilter = getViewById(R.id.i_ab_image_settings_open);
//        imageCloseFilter = getViewById(R.id.i_ab_image_settings_close);
//        imageActionBarBackground = getViewById(R.id.a_m_image_action_bar_background);
//        viewActonBarFilter = getViewById(R.id.a_m_view_filter);
//        textActionBar = getViewById(R.id.a_m_text_action_bar_title);
//        buttonNext = getViewById(R.id.a_m_button_next);
//        editAddress = getViewById(R.id.i_mf_edit_address);
    }

//    private void assignListener() {
//        imageOpenFilter.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                openFilter();
//                editAddress.setText(address);
//            }
//        });
//
//        imageCloseFilter.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                closeFilter();
//                String newAddress = editAddress.getText().toString();
//                if (newAddress != null && !newAddress.isEmpty() && !newAddress.equals(address)) {
//                    address = newAddress;
//                    geocoderTask.execute(address);
//                }
//            }
//        });
//
//        imageBack.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                onBackPressed();
//            }
//        });
//
//        buttonNext.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (address != null) {
//                    appendOrderInfo();
//                    goToDeliveryInfoFillingAct();
//                } else {
//                    showSnackBar("Сначала выберите адрес доставки");
//                }
//            }
//        });
//    }

//    private void initView() {
////        imageBack.setVisibility(View.VISIBLE);
////        imageOpenFilter.setVisibility(View.VISIBLE);
//    }

   @Override
    public void onMapClick(LatLng latLng) {
        mMap.clear();
        address = getAddressFromLatLng(latLng);
//        textActionBar.setText(address);
        currentLatLng = latLng;
        MarkerOptions options = new MarkerOptions().position(latLng);
        options.title(address);
        options.icon(BitmapDescriptorFactory.defaultMarker());
        mMap.addMarker(options);
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        marker.showInfoWindow();
//        address = getAddressFromLatLng(marker.getPosition());
//        textActionBar.setText(address);
//        currentLatLng = marker.getPosition();
        return true;
    }

    private String getAddressFromLatLng(LatLng latLng) {
        Geocoder geocoder = new Geocoder(this);
        String address = "";
        try {
            List<Address> addresses = geocoder
                    .getFromLocation(latLng.latitude, latLng.longitude, 1);
            Address addr = addresses.get(0);
            if (addr.getAddressLine(0) != null) {
                address = addr.getAddressLine(0);
            } else if (addr.getThoroughfare() != null) {
                address = addr.getThoroughfare();
            } else {
                address = addr.getAdminArea();
            }
        } catch (IOException e) {
        }
        return address;
    }

    // TODO: Combine openFilter/closeFilter into single method toggleFilter(on/off)
    private void openFilter() {
        TransitionManager.beginDelayedTransition(getCoordinatorLayout());
        showBlur();
        RelativeLayout.LayoutParams backgroundImageLayoutParams =
                new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        backgroundImageLayoutParams.setMargins(getResources().getDimensionPixelOffset(R.dimen.margin_left_right_action_bar), getResources().getDimensionPixelOffset(R.dimen.margin_top_action_bar_a_b_open), getResources().getDimensionPixelOffset(R.dimen.margin_left_right_action_bar), 0);

//        imageActionBarBackground.setLayoutParams(backgroundImageLayoutParams);
//
//        viewActonBarFilter.setVisibility(View.VISIBLE);
//        imageOpenFilter.setVisibility(View.GONE);
//        imageCloseFilter.setVisibility(View.VISIBLE);
    }

    private void closeFilter() {
        TransitionManager.beginDelayedTransition(getCoordinatorLayout());
        hideBlur();
        RelativeLayout.LayoutParams backgroundImageLayoutParams =
                new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        backgroundImageLayoutParams.setMargins(getResources().getDimensionPixelOffset(R.dimen.margin_left_right_action_bar), getResources().getDimensionPixelOffset(R.dimen.margin_top_action_bar_a_m_close), getResources().getDimensionPixelOffset(R.dimen.margin_left_right_action_bar), 0);

//        imageActionBarBackground.setLayoutParams(backgroundImageLayoutParams);
//
//        viewActonBarFilter.setVisibility(View.GONE);
//        imageOpenFilter.setVisibility(View.VISIBLE);
//        imageCloseFilter.setVisibility(View.GONE);
    }

   // @Override
    //protected final int getContentContainerId() {
    //    return R.id.a_m_relative_content_container;
   // }

   // @Override
   // protected final int getImageBlurId() {
  //      return R.id.a_m_blur_image;
  //  }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;



        Order order = DataController.getInstance().getOrder();
        mMap.setMyLocationEnabled(true);
        mMap.setOnMapClickListener(this);
        mMap.setOnMarkerClickListener(this);
        if (order.getAddressLat() == null) {
            Log.d("qweqwe", "qwwwww");
        }
        LatLng moscow = new LatLng(order.getAddressLat(), order.getAddressLng());
        // Add a marker in Sydney and move the camera
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(moscow, 17));
        mMap.addMarker(new MarkerOptions().position(moscow));
    }

    private void appendOrderInfo() {
        Order order = DataController.getInstance().getOrder();
        if(order == null) {
            throw new AppException("Order does not exist");
        } else {
            order.setAddress(address);
            order.setAddressLat(currentLatLng.latitude);
            order.setAddressLat(currentLatLng.latitude);
        }
    }

    private void goToDeliveryInfoFillingAct() {
        startActivity(new Intent(this, DeliveryInfoFillingActivity.class));
    }



    private class GeocoderTask extends AsyncTask<String, Void, List> {

        @Override
        protected List doInBackground(String... locationName) {
            // Creating an instance of Geocoder class
            Geocoder geocoder = new Geocoder(getBaseContext());
            List addresses = null;
            try {
                // Getting a maximum of 3 Address that matches the input text
                addresses = geocoder.getFromLocationName(locationName[0], 3);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return addresses;
        }

        @Override
        protected void onPostExecute(List addresses) {
            if (addresses == null || addresses.size() == 0) {
                Toast.makeText(getBaseContext(), "No Location found", Toast.LENGTH_SHORT).show();
            }
            // Clears all the existing markers on the map
            //mMap.clear();
            // Adding Markers on Google Map for each matching address
            for (int i = 0; i < addresses.size(); i++) {
                Address address = (Address) addresses.get(i);
                // Creating an instance of GeoPoint, to display in Google Map
                latLng = new LatLng(address.getLatitude(), address.getLongitude());
                String addressText = String.format("%s, %s",
                address.getMaxAddressLineIndex() > 0 ? address.getAddressLine(0) : "",
                address.getCountryName());
                Log.d("logging", "address:" + addressText);
                markerOptions = new MarkerOptions();
                markerOptions.position(latLng);
                markerOptions.title(addressText);
                mMap.addMarker(markerOptions);
                // Locate the first location
                if (i == 0) {
                    mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
                }
            }

        }

    }
}
