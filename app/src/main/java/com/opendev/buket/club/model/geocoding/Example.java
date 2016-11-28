
package com.opendev.buket.club.model.geocoding;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;


public class Example {

    @SerializedName("results")
    public List<Result> results = new ArrayList<Result>();
    @SerializedName("status")
    public String status;

}
