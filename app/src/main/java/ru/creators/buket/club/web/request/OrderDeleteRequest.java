package ru.creators.buket.club.web.request;

import android.net.Uri;

import com.google.api.client.http.HttpRequest;

import ru.creators.buket.club.consts.Rest;
import ru.creators.buket.club.web.response.DefaultResponse;

/**
 * Created by mifkamaz on 27/11/15.
 */
public class OrderDeleteRequest extends BaseRequest<DefaultResponse> {

    private int orderId;

    public OrderDeleteRequest(String accessToken, int orderId) {
        super(DefaultResponse.class, accessToken);
        this.orderId = orderId;
    }

    @Override
    public DefaultResponse loadDataFromNetwork() throws Exception {
        Uri.Builder uriBuilder = buildUri();
        uriBuilder.appendPath(Rest.ORDERS);
        uriBuilder.appendPath(Integer.toString(orderId));
        HttpRequest request = makeDeleteRequest(uriBuilder);
        return executeRequest(request, DefaultResponse.class);
    }

}
