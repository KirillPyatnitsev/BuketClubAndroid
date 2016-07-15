package ru.creators.buket.club.web.request;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;

import java.util.List;
import java.util.Locale;

/**
 * Created by mifkamaz on 19/02/16.
 */
public class AddressGetRequest extends BaseRequest<String> {
    private double latitude;
    private double longitude;
    private Context context;

    public AddressGetRequest(double latitude, double longitude, Context context) {
        super(String.class, null);
        this.latitude = latitude;
        this.longitude = longitude;
        this.context = context;
    }

    @Override
    public String loadDataFromNetwork() throws Exception {
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(context, Locale.getDefault());

        addresses = geocoder.getFromLocation(latitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5

        Address address = addresses.get(0);

        String ret = "";

        ret += (ret.isEmpty() ? "" : ", ") + (address.getThoroughfare() != null ? address.getThoroughfare() : "");
        ret += (ret.isEmpty() ? "" : ", ") + (address.getAdminArea() != null ? address.getAdminArea() : "");
        ret += (ret.isEmpty() ? "" : ", ") + (address.getPostalCode() != null ? address.getPostalCode() : "");
        ret += (ret.isEmpty() ? "" : ", ") + (address.getCountryName() != null ? address.getCountryName() : "");

        return ret;
    }
}
