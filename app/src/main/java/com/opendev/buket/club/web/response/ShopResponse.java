package com.opendev.buket.club.web.response;

import org.codehaus.jackson.annotate.JsonProperty;

import com.opendev.buket.club.consts.Fields;
import com.opendev.buket.club.model.Shop;

/**
 * Created by mifkamaz on 13/12/15.
 */
public class ShopResponse extends DefaultResponse {

    @JsonProperty(Fields.SHOP)
    private Shop shop;

    public Shop getShop() {
        return shop;
    }
}
