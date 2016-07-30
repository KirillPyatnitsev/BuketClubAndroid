package com.opendev.buket.club.web.request;

import android.net.Uri;

import com.google.api.client.http.HttpRequest;

import com.opendev.buket.club.consts.Rest;
import com.opendev.buket.club.web.response.DefaultResponse;

/**
 * Created by mifkamaz on 27/11/15.
 */
public class GenerateTypePriceRequest extends BaseRequest<DefaultResponse> {

    public GenerateTypePriceRequest() {
        super(DefaultResponse.class);
    }

    @Override
    public DefaultResponse loadDataFromNetwork() throws Exception {
        Uri.Builder uriBuilder = buildUri();
        uriBuilder.appendPath(Rest.PROFILE);
        uriBuilder.appendPath(Rest.GENERATE_TYPE_PRICE);
        HttpRequest request = makePostRequest(uriBuilder, null);
        return executeRequest(request);
    }
}
