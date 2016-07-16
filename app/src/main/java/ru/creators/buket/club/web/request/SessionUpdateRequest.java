package ru.creators.buket.club.web.request;

import android.net.Uri;

import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpResponse;

import ru.creators.buket.club.consts.Rest;
import ru.creators.buket.club.web.response.DefaultResponse;

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
