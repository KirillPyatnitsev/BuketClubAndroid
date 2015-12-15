package ru.creators.buket.club.web.response;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import ru.creators.buket.club.consts.Fields;
import ru.creators.buket.club.model.Order;

/**
 * Created by mifkamaz on 13/12/15.
 */
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class OrderResponse extends DefaultResponse {

    @JsonProperty(Fields.ORDER)
    private Order order;

    public OrderResponse(Order order) {
        this.order = order;
    }

    public OrderResponse() {
    }

    public Order getOrder() {
        return order;
    }
}
