package com.opendev.buket.club.web.request;

import android.net.Uri;

import com.google.api.client.http.HttpRequest;

import com.opendev.buket.club.consts.Rest;
import com.opendev.buket.club.web.response.DefaultResponse;

/**
 * Created by mifkamaz on 27/11/15.
 */
public class OrderDeleteRequest extends BaseRequest<DefaultResponse> {

    private int orderId;

    public OrderDeleteRequest(int orderId) {
        super(DefaultResponse.class);
        this.orderId = orderId;
    }

    @Override
    public DefaultResponse loadDataFromNetwork() throws Exception {
        Uri.Builder uriBuilder = buildUri();
        uriBuilder.appendPath(Rest.ORDERS);
        uriBuilder.appendPath(Integer.toString(orderId));
        HttpRequest request = makeDeleteRequest(uriBuilder);
        return executeRequest(request);
    }

}
