package ru.creators.buket.club.web.request;

import android.net.Uri;

import com.google.api.client.http.HttpRequest;

import ru.creators.buket.club.consts.Rest;
import ru.creators.buket.club.web.response.DictonaryResponse;
import ru.creators.buket.club.web.response.SessionResponse;

/**
 * Created by mifkamaz on 27/11/15.
 */
public class DictonaryGetRequest extends BaseRequest<DictonaryResponse> {

    private String accessToken;
    private String dictonaryType;

    public DictonaryGetRequest(String accessToken, String dictonaryType) {
        super(DictonaryResponse.class);
        this.dictonaryType = dictonaryType;
        this.accessToken = accessToken;
    }

    @Override
    protected Uri getUri() {
        return super.getUri();
    }

    @Override
    protected Uri.Builder addRestAddress(Uri.Builder uriBuilder) {
        uriBuilder.appendPath(dictonaryType);
        return uriBuilder;
    }

    @Override
    public DictonaryResponse loadDataFromNetwork() throws Exception {
        HttpRequest request = getGetHttpRequest();

        request.getUrl().put(Rest.ACCESS_TOKEN, accessToken);

        return (DictonaryResponse) getResponse(request.execute(), DictonaryResponse.class);
    }
}
