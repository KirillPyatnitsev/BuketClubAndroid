
package com.opendev.buket.club.model.geocoding;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;


public class AddressComponent {

    @SerializedName("long_name")
    public String longName;
    @SerializedName("short_name")
    public String shortName;
    @SerializedName("types")
    public List<String> types = new ArrayList<String>();

}
