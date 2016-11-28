
package com.opendev.buket.club.model.geocoding;

import com.google.gson.annotations.SerializedName;


public class Bounds {

    @SerializedName("northeast")
    public Northeast northeast;
    @SerializedName("southwest")
    public Southwest southwest;

}
