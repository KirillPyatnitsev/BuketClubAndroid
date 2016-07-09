package ru.creators.buket.club.web.request;

import android.net.Uri;

import com.google.api.client.http.HttpRequest;

import ru.creators.buket.club.consts.Rest;
import ru.creators.buket.club.web.response.DictionaryResponse;

/**
 * Created by mifkamaz on 27/11/15.
 */
public class DictionaryGetRequest extends BaseRequest<DictionaryResponse> {

    private String accessToken;
    private String dictionaryType;

    public DictionaryGetRequest(String accessToken, String dictionaryType) {
        super(DictionaryResponse.class);
        this.dictionaryType = dictionaryType;
        this.accessToken = accessToken;
    }

    @Override
    protected Uri getUri() {
        return super.getUri();
    }

    @Override
    protected Uri.Builder addRestAddress(Uri.Builder uriBuilder) {
        uriBuilder.appendPath(dictionaryType);
        return uriBuilder;
    }

    @Override
    public DictionaryResponse loadDataFromNetwork() throws Exception {
        HttpRequest request = getGetHttpRequest();

        request.getUrl().put(Rest.ACCESS_TOKEN, accessToken);

        return (DictionaryResponse) getResponse(request.execute(), DictionaryResponse.class);
    }
}
