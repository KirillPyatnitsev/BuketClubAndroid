package ru.creators.buket.club.web.request;

import android.net.Uri;

import com.google.api.client.http.HttpRequest;

import ru.creators.buket.club.consts.Rest;
import ru.creators.buket.club.web.response.OrdersResponse;

/**
 * Created by mifkamaz on 27/11/15.
 */
public class OrdersGetRequest extends BaseRequest<OrdersResponse> {

    private String accessToken;

    public OrdersGetRequest(String accessToken) {
        super(OrdersResponse.class);
        this.accessToken = accessToken;
    }

    @Override
    protected Uri.Builder addRestAddress(Uri.Builder uriBuilder) {
        uriBuilder.appendPath(Rest.PROFILE);
        uriBuilder.appendPath(Rest.ORDERS);
        return uriBuilder;
    }

    @Override
    public OrdersResponse loadDataFromNetwork() throws Exception {
        HttpRequest request = getGetHttpRequest();

        request.getUrl().put(Rest.ACCESS_TOKEN, accessToken);

        return (OrdersResponse) getResponse(request.execute(), OrdersResponse.class);
    }
}
