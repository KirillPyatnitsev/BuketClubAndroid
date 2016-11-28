
package com.opendev.buket.club.model.geocoding;

import com.google.gson.annotations.SerializedName;


public class Geometry {

    @SerializedName("bounds")
    public Bounds bounds;
    @SerializedName("location")
    public Location location;
    @SerializedName("location_type")
    public String locationType;
    @SerializedName("viewport")
    public Viewport viewport;

    public Viewport getViewport() {
        return viewport;
    }

    public void setViewport(Viewport viewport) {
        this.viewport = viewport;
    }
}
