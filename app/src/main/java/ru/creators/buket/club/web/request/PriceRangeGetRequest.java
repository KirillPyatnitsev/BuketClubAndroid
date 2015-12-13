package ru.creators.buket.club.web.request;

import android.net.Uri;

import com.google.api.client.http.HttpRequest;

import ru.creators.buket.club.consts.Rest;
import ru.creators.buket.club.model.PriceRange;
import ru.creators.buket.club.web.response.OrdersResponse;
import ru.creators.buket.club.web.response.PriceRangeResponse;

/**
 * Created by mifkamaz on 27/11/15.
 */
public class PriceRangeGetRequest extends BaseRequest<PriceRangeResponse> {

    private String accessToken;

    public PriceRangeGetRequest(String accessToken) {
        super(PriceRangeResponse.class);
        this.accessToken = accessToken;
    }

    @Override
    protected Uri.Builder addRestAddress(Uri.Builder uriBuilder) {
        uriBuilder.appendPath(Rest.BOUQUET_ITEMS);
        uriBuilder.appendPath(Rest.PRICE_RANGE);
        return uriBuilder;
    }

    @Override
    public PriceRangeResponse loadDataFromNetwork() throws Exception {
        HttpRequest request = getGetHttpRequest();

        request.getUrl().put(Rest.ACCESS_TOKEN, accessToken);

        return (PriceRangeResponse) getResponse(request.execute(), PriceRangeResponse.class);
    }
}
