package ru.creators.buket.club.web.request;

import android.net.Uri;

import com.google.api.client.http.HttpRequest;

import ru.creators.buket.club.consts.Rest;
import ru.creators.buket.club.web.response.DictonaryResponse;
import ru.creators.buket.club.web.response.ProfileResponse;

/**
 * Created by mifkamaz on 27/11/15.
 */
public class ProfileGetRequest extends BaseRequest<ProfileResponse> {

    private String accessToken;

    public ProfileGetRequest(String accessToken) {
        super(ProfileResponse.class);
        this.accessToken = accessToken;
    }

    @Override
    protected Uri getUri() {
        return super.getUri();
    }

    @Override
    protected Uri.Builder addRestAddress(Uri.Builder uriBuilder) {
        uriBuilder.appendPath(Rest.PROFILE);
        return uriBuilder;
    }

    @Override
    public ProfileResponse loadDataFromNetwork() throws Exception {
        HttpRequest request = getGetHttpRequest();

        request.getUrl().put(Rest.ACCESS_TOKEN, accessToken);

        return (ProfileResponse) getResponse(request.execute(), ProfileResponse.class);
    }
}
