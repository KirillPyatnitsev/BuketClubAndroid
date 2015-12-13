package ru.creators.buket.club.web.request;

import android.net.Uri;

import com.google.api.client.http.HttpRequest;

import ru.creators.buket.club.consts.Rest;
import ru.creators.buket.club.model.Order;
import ru.creators.buket.club.web.response.OrderResponse;
import ru.creators.buket.club.web.response.OrdersResponse;

/**
 * Created by mifkamaz on 27/11/15.
 */
public class OrderCreateRequest extends BaseRequest<OrderResponse> {

    private String accessToken;
    private Order order;

    public OrderCreateRequest(String accessToken, Order order) {
        super(OrderResponse.class);
        this.accessToken = accessToken;
        this.order = order;
    }


    @Override
    protected Uri.Builder addRestAddress(Uri.Builder uriBuilder) {
        uriBuilder.appendPath(Rest.ORDERS);
        return uriBuilder;
    }

    @Override
    public OrderResponse loadDataFromNetwork() throws Exception {
        HttpRequest request = getPostHttpRequest(getHttpContentFromJsonString(toJson(new OrderResponse(order))));

        request.getUrl().put(Rest.ACCESS_TOKEN, accessToken);

        return (OrderResponse) getResponse(request.execute(), OrderResponse.class);
    }
}
