package ru.creators.buket.club.web.request;

import android.net.Uri;

import com.google.api.client.http.HttpRequest;

import ru.creators.buket.club.consts.Rest;
import ru.creators.buket.club.model.Order;
import ru.creators.buket.club.web.model.OrderContent;
import ru.creators.buket.club.web.response.DefaultResponse;

/**
 * Created by mifkamaz on 27/11/15.
 */
public class OrderPatchRequest extends BaseRequest<DefaultResponse> {

    private final String accessToken;
    private final Order order;
    private final int orderId;

    public OrderPatchRequest(String accessToken, Order order, int orderId) {
        super(DefaultResponse.class);
        this.accessToken = accessToken;
        this.order = order;
        this.orderId = orderId;
    }

    @Override
    protected Uri.Builder addRestAddress(Uri.Builder uriBuilder) {
        uriBuilder.appendPath(Rest.ORDERS);
        uriBuilder.appendPath(Integer.toString(orderId));
        return uriBuilder;
    }

    @Override
    public DefaultResponse loadDataFromNetwork() throws Exception {
        HttpRequest request = getPathHttpRequest(new OrderContent(order));
        request.getUrl().put(Rest.ACCESS_TOKEN, accessToken);
        return getResponse(request.execute(), DefaultResponse.class);
    }

}
