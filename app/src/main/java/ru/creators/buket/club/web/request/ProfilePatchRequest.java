package ru.creators.buket.club.web.request;

import android.net.Uri;

import com.google.api.client.http.HttpRequest;

import ru.creators.buket.club.consts.Rest;
import ru.creators.buket.club.model.Profile;
import ru.creators.buket.club.web.model.ProfileContent;
import ru.creators.buket.club.web.response.DefaultResponse;

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
