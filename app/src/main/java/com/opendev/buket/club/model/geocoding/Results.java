package com.opendev.buket.club.model.geocoding;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Danis on 19.11.2016.
 */
public class Results {
    @SerializedName("results")
    List<Result> results;

    public List<Result> getResults() {
        return results;
    }

    public void setResults(List<Result> results) {
        this.results = results;
    }
}
