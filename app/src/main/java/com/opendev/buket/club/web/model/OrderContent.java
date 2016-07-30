package com.opendev.buket.club.web.model;

import org.codehaus.jackson.annotate.JsonProperty;

import com.opendev.buket.club.consts.Fields;
import com.opendev.buket.club.model.Order;

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
