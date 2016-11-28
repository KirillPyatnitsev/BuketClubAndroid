package com.opendev.buket.club.web.request;

import android.net.Uri;

import com.google.api.client.http.HttpRequest;
import com.opendev.buket.club.consts.Rest;
import com.opendev.buket.club.web.response.SessionResponse;

/**
 * Created by mifkamaz on 27/11/15.
 */
public class SessionCreateRequest extends BaseRequest<SessionResponse> {

    private String udid;
    private String deviceToken;
    private int projectId;

    public SessionCreateRequest(String udid, String deviceToken, int projectId) {
        super(SessionResponse.class);
        this.udid = udid;
        this.deviceToken = deviceToken;
        this.projectId = projectId;
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
        request.getUrl().put(Rest.PROJECT_ID, projectId);
        return executeRequest(request);
    }
}
