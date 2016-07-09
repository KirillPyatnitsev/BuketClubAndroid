package ru.creators.buket.club.web.request;

import android.net.Uri;

import com.google.api.client.http.HttpRequest;

import ru.creators.buket.club.consts.Rest;
import ru.creators.buket.club.web.response.DefaultResponse;

/**
 * Created by mifkamaz on 27/11/15.
 */
public class OrderDeleteRequest extends BaseRequest<DefaultResponse> {

    private String accessToken;
    private int orderId;

    public OrderDeleteRequest(String accessToken, int orderId) {
        super(DefaultResponse.class);
        this.accessToken = accessToken;
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
        HttpRequest request = getDeleteHttpRequest();

        request.getUrl().put(Rest.ACCESS_TOKEN, accessToken);

        return getResponse(request.execute(), DefaultResponse.class);
    }

}
