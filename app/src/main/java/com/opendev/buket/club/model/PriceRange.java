package com.opendev.buket.club.model;

import org.codehaus.jackson.annotate.JsonProperty;

import com.opendev.buket.club.consts.Fields;

/**
 * Created by mifkamaz on 13/12/15.
 */
public class PriceRange {

    @JsonProperty(Fields.MIN_PRICE)
    private int minPrice;

    @JsonProperty(Fields.MAX_PRICE)
    private int maxPrice;

    public int getMinPrice() {
        return minPrice;
    }

    public int getMaxPrice() {
        return maxPrice;
    }
}
