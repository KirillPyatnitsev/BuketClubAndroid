package ru.creators.buket.club.web.request;

import android.net.Uri;

import com.google.api.client.http.HttpRequest;

import ru.creators.buket.club.consts.Rest;
import ru.creators.buket.club.web.response.DefaultResponse;

/**
 * Created by mifkamaz on 27/11/15.
 */
public class SessionDeleteRequest extends BaseRequest<DefaultResponse> {

    private String accessToken;

    public SessionDeleteRequest(String accessToken) {
        super(DefaultResponse.class);
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
        HttpRequest request = getDeleteHttpRequest();

        request.getUrl().put(Rest.ACCESS_TOKEN, accessToken);

        return getResponse(request.execute(), DefaultResponse.class);
    }
}
