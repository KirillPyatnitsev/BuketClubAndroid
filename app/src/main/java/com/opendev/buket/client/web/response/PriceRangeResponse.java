package com.opendev.buket.client.web.response;

import org.codehaus.jackson.annotate.JsonProperty;

import com.opendev.buket.client.consts.Fields;
import com.opendev.buket.client.model.PriceRange;

/**
 * Created by mifkamaz on 13/12/15.
 */
public class PriceRangeResponse extends DefaultResponse {

    @JsonProperty(Fields.PRICE_RANGE)
    private PriceRange priceRange;

    public PriceRange getPriceRange() {
        return priceRange;
    }
}
