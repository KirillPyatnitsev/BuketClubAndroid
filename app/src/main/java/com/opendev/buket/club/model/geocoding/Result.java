
package com.opendev.buket.club.model.geocoding;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;


public class Result {

    @SerializedName("address_components")
    public List<AddressComponent> addressComponents = new ArrayList<AddressComponent>();
    @SerializedName("formatted_address")
    public String formattedAddress;
    @SerializedName("geometry")
    public Geometry geometry;
    @SerializedName("partial_match")
    public Boolean partialMatch;
    @SerializedName("place_id")
    public String placeId;
    @SerializedName("types")
    public List<String> types = new ArrayList<String>();

    public Geometry getGeometry() {
        return geometry;
    }

    public void setGeometry(Geometry geometry) {
        this.geometry = geometry;
    }
}
