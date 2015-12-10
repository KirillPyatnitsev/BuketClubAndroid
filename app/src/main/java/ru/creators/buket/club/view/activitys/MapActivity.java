package ru.creators.buket.club.view.activitys;

import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.transitionseverywhere.TransitionManager;

import java.io.IOException;
import java.util.List;

import ru.creators.buket.club.R;

public class MapActivity extends BaseActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private LatLng latLng;
    private MarkerOptions markerOptions;

    private View viewActonBarFilter;
    private ImageView imageBack;
    private ImageView imageOpenFilter;
    private ImageView imageCloseFilter;
    private ImageView imageActionBarBackground;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        assignView();
        initView();
        assignListener();

        GeocoderTask geocoderTask = new GeocoderTask();

        geocoderTask.execute("Рогова 6");
    }

    @Override
    protected int getCoordinatorViewId() {
        return R.id.a_m_coordinator_root;
    }

    private void assignView(){
        imageBack = getViewById(R.id.i_ab_image_back);
        imageOpenFilter = getViewById(R.id.i_ab_image_settings_open);
        imageCloseFilter = getViewById(R.id.i_ab_image_settings_close);
        imageActionBarBackground = getViewById(R.id.a_m_image_action_bar_background);
        viewActonBarFilter = getViewById(R.id.a_m_view_filter);
    }

    private void assignListener(){
        imageOpenFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFilter();
            }
        });

        imageCloseFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeFilter();
            }
        });

        imageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void initView(){
        imageBack.setVisibility(View.VISIBLE);
        imageOpenFilter.setVisibility(View.VISIBLE);
    }

    private void openFilter(){
        TransitionManager.beginDelayedTransition(getCoordinatorLayout());
        showBlur();
        RelativeLayout.LayoutParams backgroundImageLayoutParams =
                new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        backgroundImageLayoutParams.setMargins(
                getResources().getDimensionPixelOffset(R.dimen.margin_left_right_action_bar),
                getResources().getDimensionPixelOffset(R.dimen.margin_top_action_bar_a_b_open),
                getResources().getDimensionPixelOffset(R.dimen.margin_left_right_action_bar),
                0
        );

        imageActionBarBackground.setLayoutParams(backgroundImageLayoutParams);

        viewActonBarFilter.setVisibility(View.VISIBLE);

        imageOpenFilter.setVisibility(View.GONE);
        imageCloseFilter.setVisibility(View.VISIBLE);
    }

    private void closeFilter(){
        TransitionManager.beginDelayedTransition(getCoordinatorLayout());
        hideBlur();
        RelativeLayout.LayoutParams backgroundImageLayoutParams =
                new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        backgroundImageLayoutParams.setMargins(
                getResources().getDimensionPixelOffset(R.dimen.margin_left_right_action_bar),
                getResources().getDimensionPixelOffset(R.dimen.margin_top_action_bar_a_b_close),
                getResources().getDimensionPixelOffset(R.dimen.margin_left_right_action_bar),
                0
        );

        imageActionBarBackground.setLayoutParams(backgroundImageLayoutParams);

        viewActonBarFilter.setVisibility(View.GONE);

        imageOpenFilter.setVisibility(View.VISIBLE);
        imageCloseFilter.setVisibility(View.GONE);
    }

    @Override
    protected int getContentContainerId() {
        return R.id.a_m_relative_content_container;
    }

    @Override
    protected int getImageBlurId() {
        return R.id.a_m_blur_image;
    }

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

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

    private class GeocoderTask extends AsyncTask<String, Void, List >{

        protected List addresses;

        @Override
        protected List doInBackground(String... locationName) {
// Creating an instance of Geocoder class

            Geocoder geocoder = new Geocoder(getBaseContext());

            List

                    addresses = null;
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
            if(addresses==null || addresses.size()==0){

                Toast.makeText(getBaseContext(), "No Location found", Toast.LENGTH_SHORT).show();

            }

// Clears all the existing markers on the map

//            mMap.clear();

// Adding Markers on Google Map for each matching address

            for(int i=0;i<addresses.size();i++){

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

                if(i==0)

                    mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));

            }

        }

    }
}
