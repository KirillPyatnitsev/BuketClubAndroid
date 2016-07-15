package ru.creators.buket.club.web.request;

import android.net.Uri;

import com.google.api.client.http.HttpRequest;

import ru.creators.buket.club.consts.Rest;
import ru.creators.buket.club.web.response.ShopListResponse;

/**
 * Created by mifkamaz on 27/11/15.
 */
public class ShopsAllGetRequest extends BaseRequest<ShopListResponse> {

    private int page;
    private int perPage;

    public ShopsAllGetRequest(String accessToken, int page, int perPage) {
        super(ShopListResponse.class, accessToken);
        this.page = page;
        this.perPage = perPage;
    }

    @Override
    public ShopListResponse loadDataFromNetwork() throws Exception {
        Uri.Builder uriBuilder = buildUri();
        uriBuilder.appendPath(Rest.SHOPS);
        HttpRequest request = makeGetRequest(uriBuilder);
        request.getUrl().put(Rest.PAGE, page);
        request.getUrl().put(Rest.PER_PAGE, perPage);
        return executeRequest(request, ShopListResponse.class);
    }
}
