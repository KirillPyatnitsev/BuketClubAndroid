package com.opendev.buket.club.web.request;

import android.net.Uri;

import com.google.api.client.http.HttpRequest;

import com.opendev.buket.club.consts.Rest;
import com.opendev.buket.club.web.response.ProfileResponse;

/**
 * Created by mifkamaz on 27/11/15.
 */
public class ProfileGetRequest extends BaseRequest<ProfileResponse> {

    public ProfileGetRequest() {
        super(ProfileResponse.class);
    }

    @Override
    public ProfileResponse loadDataFromNetwork() throws Exception {
        Uri.Builder uriBuilder = buildUri();
        uriBuilder.appendPath(Rest.PROFILE);
        HttpRequest request = makeGetRequest(uriBuilder);
        return executeRequest(request);
    }
}
