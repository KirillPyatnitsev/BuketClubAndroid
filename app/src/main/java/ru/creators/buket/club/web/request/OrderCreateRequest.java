package ru.creators.buket.club.web.request;

import android.net.Uri;

import com.google.api.client.http.HttpRequest;

import ru.creators.buket.club.consts.Rest;
import ru.creators.buket.club.model.Order;
import ru.creators.buket.club.web.model.OrderContent;
import ru.creators.buket.club.web.response.OrderResponse;

/**
 * Created by mifkamaz on 27/11/15.
 */
public class OrderCreateRequest extends BaseRequest<OrderResponse> {

    private Order order;

    public OrderCreateRequest(String accessToken, Order order) {
        super(OrderResponse.class, accessToken);
        this.order = order;
    }

    @Override
    public OrderResponse loadDataFromNetwork() throws Exception {
        Uri.Builder uriBuilder = buildUri();
        uriBuilder.appendPath(Rest.ORDERS);
        HttpRequest request = makePostRequest(uriBuilder, new OrderContent(order));
        return executeRequest(request, OrderResponse.class);
    }
}
