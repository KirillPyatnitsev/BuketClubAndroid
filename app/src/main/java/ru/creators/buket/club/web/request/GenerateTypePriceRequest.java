package ru.creators.buket.club.web.request;

import android.net.Uri;

import com.google.api.client.http.HttpRequest;

import ru.creators.buket.club.consts.Rest;
import ru.creators.buket.club.web.response.DefaultResponse;

/**
 * Created by mifkamaz on 27/11/15.
 */
public class GenerateTypePriceRequest extends BaseRequest<DefaultResponse> {

    public GenerateTypePriceRequest(String accessToken) {
        super(DefaultResponse.class, accessToken);
    }

    @Override
    public DefaultResponse loadDataFromNetwork() throws Exception {
        Uri.Builder uriBuilder = buildUri();
        uriBuilder.appendPath(Rest.PROFILE);
        uriBuilder.appendPath(Rest.GENERATE_TYPE_PRICE);
        HttpRequest request = makePostRequest(uriBuilder, null);
        return executeRequest(request, DefaultResponse.class);
    }
}
