package com.opendev.buket.client.web.request;

import android.net.Uri;

import com.google.api.client.http.HttpRequest;

import com.opendev.buket.client.consts.Rest;
import com.opendev.buket.client.model.Order;
import com.opendev.buket.client.web.model.OrderContent;
import com.opendev.buket.client.web.response.OrderResponse;

/**
 * Created by mifkamaz on 27/11/15.
 */
public class OrderCreateRequest extends BaseRequest<OrderResponse> {

    private final Order order;

    public OrderCreateRequest(Order order) {
        super(OrderResponse.class);
        this.order = order;
    }

    @Override
    public OrderResponse loadDataFromNetwork() throws Exception {
        Uri.Builder uriBuilder = buildUri();
        uriBuilder.appendPath(Rest.ORDERS);
        HttpRequest request = makePostRequest(uriBuilder, new OrderContent(order));
        return executeRequest(request);
    }
}
