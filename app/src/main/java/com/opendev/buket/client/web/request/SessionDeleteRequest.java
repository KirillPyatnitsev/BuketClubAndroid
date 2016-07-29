package com.opendev.buket.client.web.request;

import android.net.Uri;

import com.google.api.client.http.HttpRequest;

import com.opendev.buket.client.consts.Rest;
import com.opendev.buket.client.web.response.DefaultResponse;

/**
 * Created by mifkamaz on 27/11/15.
 */
public class SessionDeleteRequest extends BaseRequest<DefaultResponse> {

    public SessionDeleteRequest() {
        super(DefaultResponse.class);
    }

    @Override
    public DefaultResponse loadDataFromNetwork() throws Exception {
        Uri.Builder uriBuilder = buildUri();
        uriBuilder.appendPath(Rest.SESSIONS);
        HttpRequest request = makeDeleteRequest(uriBuilder);
        return executeRequest(request);
    }
}
