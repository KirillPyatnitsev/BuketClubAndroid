package ru.creators.buket.club.web.request;

import android.net.Uri;

import com.google.api.client.http.HttpRequest;

import ru.creators.buket.club.consts.Rest;
import ru.creators.buket.club.web.response.PriceRangeResponse;

/**
 * Created by mifkamaz on 27/11/15.
 */
public class PriceRangeGetRequest extends BaseRequest<PriceRangeResponse> {

    public PriceRangeGetRequest(String accessToken) {
        super(PriceRangeResponse.class, accessToken);
    }

    @Override
    public PriceRangeResponse loadDataFromNetwork() throws Exception {
        Uri.Builder uriBuilder = buildUri();
        uriBuilder.appendPath(Rest.PRICE_RANGE);
        uriBuilder.appendPath(Rest.BOUQUET_ITEMS);
        HttpRequest request = makeGetRequest(uriBuilder);
        return executeRequest(request, PriceRangeResponse.class);
    }
}
