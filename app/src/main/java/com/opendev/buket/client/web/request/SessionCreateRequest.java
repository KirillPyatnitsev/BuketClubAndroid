package com.opendev.buket.client.web.request;

import android.net.Uri;

import com.google.api.client.http.HttpRequest;

import com.opendev.buket.client.consts.Rest;
import com.opendev.buket.client.web.response.SessionResponse;

/**
 * Created by mifkamaz on 27/11/15.
 */
public class SessionCreateRequest extends BaseRequest<SessionResponse> {

    private String udid;
    private String deviceToken;

    public SessionCreateRequest(String udid, String deviceToken) {
        super(SessionResponse.class);
        this.udid = udid;
        this.deviceToken = deviceToken;
    }

    @Override
    public SessionResponse loadDataFromNetwork() throws Exception {
        Uri.Builder uriBuilder = buildUri();
        uriBuilder.appendPath(Rest.SESSIONS);
        HttpRequest request = makePostRequest(uriBuilder, null);
        request.getUrl().put(Rest.UUID, udid);
        if (deviceToken != null) {
            request.getUrl().put(Rest.DEVICE_TOKEN, deviceToken);
        }
        request.getUrl().put(Rest.DEVICE_TYPE, Rest.DEVICE_TYPE_ANDROID);
        return executeRequest(request);
    }
}
