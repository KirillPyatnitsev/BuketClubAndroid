package com.opendev.buket.club.model;

import org.codehaus.jackson.annotate.JsonProperty;

import com.opendev.buket.club.consts.Fields;

/**
 * Created by mifkamaz on 16/12/15.
 */
public class UserPayment {

    public static final int COST_FIXED = 1;
    public static final int COST_FLOATING = 0;

    public static final String COST_FIXED_DESC = "fix_price";
    public static final String COST_FLOATING_DESC = "flexible_price";

    @JsonProperty(Fields.ID)
    private int id;

    @JsonProperty(Fields.TYPE_PRICE)
    private String typePrice;

    @JsonProperty(Fields.TYPE_PRICE_INDEX)
    private int typePriceIndex;
}
