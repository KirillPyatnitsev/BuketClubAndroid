package ru.creators.buket.club.web.request;

import android.net.Uri;

import com.google.api.client.http.HttpRequest;

import ru.creators.buket.club.consts.Rest;
import ru.creators.buket.club.web.response.ShopResponse;

/**
 * Created by mifkamaz on 27/11/15.
 */
public class ShopGetRequest extends BaseRequest<ShopResponse> {

    private String accessToken;
    private int id;

    public ShopGetRequest(String accessToken, int id) {
        super(ShopResponse.class);
        this.accessToken = accessToken;
        this.id = id;
    }

    @Override
    protected Uri.Builder addRestAddress(Uri.Builder uriBuilder) {
        uriBuilder.appendPath(Rest.SHOPS);
        uriBuilder.appendPath(Integer.toString(id));
        return uriBuilder;
    }

    @Override
    public ShopResponse loadDataFromNetwork() throws Exception {
        HttpRequest request = getGetHttpRequest();

        request.getUrl().put(Rest.ACCESS_TOKEN, accessToken);

        return (ShopResponse) getResponse(request.execute(), ShopResponse.class);
    }
}
