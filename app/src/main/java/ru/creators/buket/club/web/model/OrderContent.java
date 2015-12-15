package ru.creators.buket.club.web.model;

import org.codehaus.jackson.annotate.JsonProperty;

import ru.creators.buket.club.consts.Fields;
import ru.creators.buket.club.model.Order;

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
