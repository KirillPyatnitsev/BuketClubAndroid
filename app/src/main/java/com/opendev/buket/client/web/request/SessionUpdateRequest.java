package com.opendev.buket.client.web.request;

import android.net.Uri;

import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpResponse;

import com.opendev.buket.client.consts.Rest;
import com.opendev.buket.client.web.response.DefaultResponse;

/**
 * Created by mifkamaz on 27/11/15.
 */
public class SessionUpdateRequest extends BaseRequest<DefaultResponse> {

    private String deviceToken;

    public SessionUpdateRequest(String deviceToken) {
        super(DefaultResponse.class);
        this.deviceToken = deviceToken;
    }

    @Override
    public DefaultResponse loadDataFromNetwork() throws Exception {
        Uri.Builder uriBuilder = buildUri();
        uriBuilder.appendPath(Rest.SESSIONS);
        HttpRequest request = makePatchRequest(uriBuilder, null);
        request.getUrl().put(Rest.DEVICE_TOKEN, deviceToken);
        return executeRequest(request);
    }
}
