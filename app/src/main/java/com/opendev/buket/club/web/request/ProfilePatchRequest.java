package com.opendev.buket.club.web.request;

import android.net.Uri;

import com.google.api.client.http.HttpRequest;

import com.opendev.buket.club.consts.Rest;
import com.opendev.buket.club.model.Profile;
import com.opendev.buket.club.web.model.ProfileContent;
import com.opendev.buket.club.web.response.DefaultResponse;

/**
 * Created by mifkamaz on 17/03/16.
 */
public class ProfilePatchRequest extends BaseRequest<DefaultResponse> {

    private final Profile profile;

    public ProfilePatchRequest(Profile profile) {
        super(DefaultResponse.class);
        this.profile = profile;
    }

    @Override
    public DefaultResponse loadDataFromNetwork() throws Exception {
        Uri.Builder uriBuilder = buildUri();
        uriBuilder.appendPath(Rest.PROFILE);
        HttpRequest request = makePatchRequest(uriBuilder, new ProfileContent(profile));
        return executeRequest(request);
    }
}
