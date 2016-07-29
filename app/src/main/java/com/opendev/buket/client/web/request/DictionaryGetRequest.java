package com.opendev.buket.client.web.request;

import android.net.Uri;

import com.google.api.client.http.HttpRequest;

import com.opendev.buket.client.consts.Rest;
import com.opendev.buket.client.web.response.DictionaryResponse;

/**
 * Created by mifkamaz on 27/11/15.
 */
public class DictionaryGetRequest extends BaseRequest<DictionaryResponse> {

    private String dictionaryType;

    public DictionaryGetRequest(String dictionaryType) {
        super(DictionaryResponse.class);
        this.dictionaryType = dictionaryType;
    }

    @Override
    public DictionaryResponse loadDataFromNetwork() throws Exception {
        Uri.Builder uriBuilder = buildUri();
        uriBuilder.appendPath(dictionaryType);
        HttpRequest request = makeGetRequest(uriBuilder);
        return executeRequest(request);
    }
}
