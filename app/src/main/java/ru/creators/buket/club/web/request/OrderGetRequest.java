package ru.creators.buket.club.web.request;

import android.net.Uri;

import com.google.api.client.http.HttpRequest;

import ru.creators.buket.club.consts.Rest;
import ru.creators.buket.club.web.response.OrderResponse;

/**
 * Created by mifkamaz on 27/11/15.
 */
public class OrderGetRequest extends BaseRequest<OrderResponse> {

    private int id;

    public OrderGetRequest(String accessToken, int id) {
        super(OrderResponse.class, accessToken);
        this.id = id;
    }

    @Override
    public OrderResponse loadDataFromNetwork() throws Exception {
        Uri.Builder uriBuilder = buildUri();
        uriBuilder.appendPath(Rest.ORDERS);
        uriBuilder.appendPath(Integer.toString(id));
        HttpRequest request = makeGetRequest(uriBuilder);
        return executeRequest(request, OrderResponse.class);
    }
}
