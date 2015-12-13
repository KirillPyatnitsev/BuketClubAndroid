package ru.creators.buket.club.web.request;

import android.net.Uri;

import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpResponse;

import ru.creators.buket.club.consts.Rest;
import ru.creators.buket.club.web.request.BaseRequest;
import ru.creators.buket.club.web.response.DefaultResponse;

/**
 * Created by mifkamaz on 27/11/15.
 */
public class SessionUpdateRequest extends BaseRequest<DefaultResponse> {

    private String deviceToken;
    private String accessToken;

    public SessionUpdateRequest(String accessToken, String deviceToken) {
        super(DefaultResponse.class);
        this.deviceToken = deviceToken;
        this.accessToken = accessToken;
    }

    @Override
    protected Uri getUri() {
        return super.getUri();
    }

    @Override
    protected Uri.Builder addRestAddress(Uri.Builder uriBuilder) {
        uriBuilder.appendPath(Rest.SESSIONS);
        return uriBuilder;
    }

    @Override
    public DefaultResponse loadDataFromNetwork() throws Exception {
        HttpRequest request = getPathHttpRequest(null);

        request.getUrl().put(Rest.ACCESS_TOKEN, accessToken);
        request.getUrl().put(Rest.DEVICE_TOKEN, deviceToken);

        HttpResponse response = request.execute();

        return getResponse(response, DefaultResponse.class);
    }
}
