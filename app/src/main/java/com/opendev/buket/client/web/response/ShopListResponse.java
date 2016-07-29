package com.opendev.buket.client.web.response;

import org.codehaus.jackson.annotate.JsonProperty;

import com.opendev.buket.client.consts.Fields;
import com.opendev.buket.client.model.lists.ListShop;

/**
 * Created by mifkamaz on 13/12/15.
 */
public class ShopListResponse extends DefaultResponse {

    @JsonProperty(Fields.SHOPS)
    private ListShop listShop;

    public ListShop getListShop() {
        return listShop;
    }
}
