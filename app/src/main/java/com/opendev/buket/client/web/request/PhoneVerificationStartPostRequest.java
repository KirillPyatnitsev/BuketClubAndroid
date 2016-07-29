package com.opendev.buket.client.web.request;

import android.net.Uri;

import com.google.api.client.http.HttpRequest;

import com.opendev.buket.client.consts.Rest;
import com.opendev.buket.client.web.response.PhoneCodeResponse;

/**
 * Created by mifkamaz on 04/03/16.
 */
public class PhoneVerificationStartPostRequest extends BaseRequest<PhoneCodeResponse> {

    private String telephone;

    public PhoneVerificationStartPostRequest(String telephone) {
        super(PhoneCodeResponse.class);
        this.telephone = telephone;
    }

    @Override
    public PhoneCodeResponse loadDataFromNetwork() throws Exception {
        Uri.Builder uriBuilder = buildUri();
        uriBuilder.appendPath(Rest.PROFILE);
        uriBuilder.appendPath(Rest.SEND_CODE);
        HttpRequest request = makePostRequest(uriBuilder, null);
        request.getUrl().put(Rest.PHONE, telephone);
        return executeRequest(request);
    }
}
