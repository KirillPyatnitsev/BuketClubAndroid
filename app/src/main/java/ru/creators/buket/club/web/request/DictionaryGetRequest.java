package ru.creators.buket.club.web.request;

import android.net.Uri;

import com.google.api.client.http.HttpRequest;

import ru.creators.buket.club.consts.Rest;
import ru.creators.buket.club.web.response.DictionaryResponse;

/**
 * Created by mifkamaz on 27/11/15.
 */
public class DictionaryGetRequest extends BaseRequest<DictionaryResponse> {

    private String dictionaryType;

    public DictionaryGetRequest(String accessToken, String dictionaryType) {
        super(DictionaryResponse.class, accessToken);
        this.dictionaryType = dictionaryType;
    }

    @Override
    public DictionaryResponse loadDataFromNetwork() throws Exception {
        Uri.Builder uriBuilder = buildUri();
        uriBuilder.appendPath(dictionaryType);
        HttpRequest request = makeGetRequest(uriBuilder);
        return executeRequest(request, DictionaryResponse.class, new DictionaryResponse());
    }
}
