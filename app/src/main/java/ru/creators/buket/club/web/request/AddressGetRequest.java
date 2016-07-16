package ru.creators.buket.club.web.request;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;

import com.octo.android.robospice.request.SpiceRequest;

import java.util.List;
import java.util.Locale;

/**
 * Created by mifkamaz on 19/02/16.
 */
public class AddressGetRequest extends SpiceRequest<String> {
    private double latitude;
    private double longitude;
    private Context context;

    public AddressGetRequest(double latitude, double longitude, Context context) {
        super(String.class);
        this.latitude = latitude;
        this.longitude = longitude;
        this.context = context;
    }

    @Override
    public String loadDataFromNetwork() throws Exception {
        String ret = null;
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
        if(addresses != null && addresses.isEmpty()) {
            Address address = addresses.get(0);
            StringBuilder sb = new StringBuilder();
            String[] parts = {address.getThoroughfare(), address.getAdminArea(), address.getPostalCode(), address.getCountryName()};
            for(String part: parts) {
                if(sb.length() > 0) {
                    sb.append(", ");
                }
                sb.append(part);
            }
        }
        return ret;
    }
}
