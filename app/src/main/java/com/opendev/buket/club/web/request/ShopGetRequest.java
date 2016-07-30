package com.opendev.buket.club.web.request;

import android.net.Uri;

import com.google.api.client.http.HttpRequest;

import com.opendev.buket.club.consts.Rest;
import com.opendev.buket.club.web.response.ShopResponse;

/**
 * Created by mifkamaz on 27/11/15.
 */
public class ShopGetRequest extends BaseRequest<ShopResponse> {

    private int id;

    public ShopGetRequest(int id) {
        super(ShopResponse.class);
        this.id = id;
    }

    @Override
    public ShopResponse loadDataFromNetwork() throws Exception {
        Uri.Builder uriBuilder = buildUri();
        uriBuilder.appendPath(Rest.SHOPS);
        uriBuilder.appendPath(Integer.toString(id));
        HttpRequest request = makeGetRequest(uriBuilder);
        return executeRequest(request);
    }
}
