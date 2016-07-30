package com.opendev.buket.club.web.response;

import org.codehaus.jackson.annotate.JsonProperty;

import com.opendev.buket.club.consts.Fields;
import com.opendev.buket.club.model.lists.ListOrder;

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
