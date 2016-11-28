
package com.opendev.buket.club.model.geocoding;

import com.google.gson.annotations.SerializedName;


public class Viewport {

    @SerializedName("northeast")
    public Northeast_ northeast;
    @SerializedName("southwest")
    public Southwest_ southwest;

}
