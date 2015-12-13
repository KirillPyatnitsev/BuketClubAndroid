package ru.creators.buket.club.web.response;

import org.codehaus.jackson.annotate.JsonProperty;

import ru.creators.buket.club.consts.Fields;
import ru.creators.buket.club.model.PriceRange;

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
