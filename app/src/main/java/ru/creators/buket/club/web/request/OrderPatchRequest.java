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

    private final Order order;
    private final int orderId;

    public OrderPatchRequest(Order order, int orderId) {
        super(DefaultResponse.class);
        this.order = order;
        this.orderId = orderId;
    }

    @Override
    public DefaultResponse loadDataFromNetwork() throws Exception {
        Uri.Builder uriBuilder = buildUri();
        uriBuilder.appendPath(Rest.ORDERS);
        uriBuilder.appendPath(Integer.toString(orderId));
        HttpRequest request = makePatchRequest(uriBuilder, new OrderContent(order));
        return executeRequest(request);
    }

}
