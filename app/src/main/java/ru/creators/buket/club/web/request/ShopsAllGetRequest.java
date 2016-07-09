package ru.creators.buket.club.web.request;

import android.net.Uri;

import com.google.api.client.http.HttpRequest;

import ru.creators.buket.club.consts.Rest;
import ru.creators.buket.club.web.response.ShopListResponse;

/**
 * Created by mifkamaz on 27/11/15.
 */
public class ShopsAllGetRequest extends BaseRequest<ShopListResponse> {

    private String accessToken;
    private int page;
    private int perPage;

    public ShopsAllGetRequest(String accessToken, int page, int perPage) {
        super(ShopListResponse.class);
        this.accessToken = accessToken;
        this.page = page;
        this.perPage = perPage;
    }

    @Override
    protected Uri.Builder addRestAddress(Uri.Builder uriBuilder) {
        uriBuilder.appendPath(Rest.SHOPS);
        return uriBuilder;
    }

    @Override
    public ShopListResponse loadDataFromNetwork() throws Exception {
        HttpRequest request = getGetHttpRequest();

        request.getUrl().put(Rest.ACCESS_TOKEN, accessToken);
        request.getUrl().put(Rest.PAGE, page);
        request.getUrl().put(Rest.PER_PAGE, perPage);

        return (ShopListResponse) getResponse(request.execute(), ShopListResponse.class);
    }
}
