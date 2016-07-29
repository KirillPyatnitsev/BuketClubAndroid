package com.opendev.buket.client.web.response;

import org.codehaus.jackson.annotate.JsonProperty;

import com.opendev.buket.client.consts.Fields;
import com.opendev.buket.client.model.lists.ListOrder;

/**
 * Created by mifkamaz on 13/12/15.
 */
public class OrdersResponse extends DefaultResponse {

    @JsonProperty(Fields.ORDERS)
    private ListOrder listOrder;

    public ListOrder getListOrder() {
        return listOrder;
    }
}
