package ru.creators.buket.club.web.request;

import android.net.Uri;

import com.google.api.client.http.HttpRequest;

import ru.creators.buket.club.consts.Rest;
import ru.creators.buket.club.web.response.DefaultResponse;
import ru.creators.buket.club.web.response.ProfileResponse;

/**
 * Created by mifkamaz on 27/11/15.
 */
public class GenerateTypePriceRequest extends BaseRequest<DefaultResponse> {

    private String accessToken;

    public GenerateTypePriceRequest(String accessToken) {
        super(DefaultResponse.class);
        this.accessToken = accessToken;
    }

    @Override
    protected Uri getUri() {
        return super.getUri();
    }

    @Override
    protected Uri.Builder addRestAddress(Uri.Builder uriBuilder) {
        uriBuilder.appendPath(Rest.PROFILE);
        uriBuilder.appendPath(Rest.GENERATE_TYPE_PRICE);
        return uriBuilder;
    }

    @Override
    public DefaultResponse loadDataFromNetwork() throws Exception {
        HttpRequest request = getPostHttpRequest(null);

        request.getUrl().put(Rest.ACCESS_TOKEN, accessToken);

        return getResponse(request.execute(), DefaultResponse.class);
    }
}
