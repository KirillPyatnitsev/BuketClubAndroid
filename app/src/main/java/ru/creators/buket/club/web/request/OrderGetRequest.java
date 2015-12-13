package ru.creators.buket.club.web.request;

import android.net.Uri;

import com.google.api.client.http.HttpRequest;

import ru.creators.buket.club.consts.Rest;
import ru.creators.buket.club.web.response.OrderResponse;
import ru.creators.buket.club.web.response.OrdersResponse;

/**
 * Created by mifkamaz on 27/11/15.
 */
public class OrderGetRequest extends BaseRequest<OrderResponse> {

    private String accessToken;
    private int id;

    public OrderGetRequest(String accessToken, int id) {
        super(OrderResponse.class);
        this.accessToken = accessToken;
        this.id = id;
    }

    @Override
    protected Uri.Builder addRestAddress(Uri.Builder uriBuilder) {
        uriBuilder.appendPath(Rest.ORDERS);
        uriBuilder.appendPath(Integer.toString(id));
        return uriBuilder;
    }

    @Override
    public OrderResponse loadDataFromNetwork() throws Exception {
        HttpRequest request = getGetHttpRequest();

        request.getUrl().put(Rest.ACCESS_TOKEN, accessToken);

        return (OrderResponse) getResponse(request.execute(), OrderResponse.class);
    }
}
