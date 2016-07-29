package com.opendev.buket.client.web.model;

import org.codehaus.jackson.annotate.JsonProperty;

import com.opendev.buket.client.consts.Fields;
import com.opendev.buket.client.model.Order;

/**
 * Created by mifkamaz on 14/12/15.
 */
public class OrderContent {
    @JsonProperty(Fields.ORDER)
    private Order order;

    public OrderContent(Order order) {
        this.order = order;
    }
}
