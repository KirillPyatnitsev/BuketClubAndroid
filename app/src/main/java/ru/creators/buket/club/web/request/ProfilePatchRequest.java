package ru.creators.buket.club.web.request;

import android.net.Uri;

import com.google.api.client.http.HttpRequest;

import ru.creators.buket.club.consts.Rest;
import ru.creators.buket.club.consts.ServerConfig;
import ru.creators.buket.club.model.Profile;
import ru.creators.buket.club.web.model.ProfileContent;
import ru.creators.buket.club.web.response.DefaultResponse;

/**
 * Created by mifkamaz on 17/03/16.
 */
public class ProfilePatchRequest extends BaseRequest<DefaultResponse>{

    private Profile profile;
    private String accessToken;

    public ProfilePatchRequest(String accessToken, Profile profile) {
        super(DefaultResponse.class);
        this.profile = profile;
        this.accessToken = accessToken;
    }

    @Override
    protected Uri.Builder addRestAddress(Uri.Builder uriBuilder) {
        uriBuilder.appendPath(Rest.PROFILE);
        return uriBuilder;
    }

    @Override
    public DefaultResponse loadDataFromNetwork() throws Exception {

        HttpRequest request = getPathHttpRequest(toJson(new ProfileContent(profile)));

        request.getUrl().put(Rest.ACCESS_TOKEN, accessToken);

        return getResponse(request.execute(), DefaultResponse.class, new DefaultResponse());
    }
}
