package ru.creators.buket.club.web.request;

import android.net.Uri;

import com.google.api.client.http.HttpRequest;

import ru.creators.buket.club.consts.Rest;
import ru.creators.buket.club.web.response.PriceRangeResponse;

/**
 * Created by mifkamaz on 27/11/15.
 */
public class PriceRangeGetRequest extends BaseRequest<PriceRangeResponse> {

    public PriceRangeGetRequest() {
        super(PriceRangeResponse.class);
    }

    @Override
    public PriceRangeResponse loadDataFromNetwork() throws Exception {
        Uri.Builder uriBuilder = buildUri();
        uriBuilder.appendPath(Rest.BOUQUET_ITEMS);
        uriBuilder.appendPath(Rest.PRICE_RANGE);
        HttpRequest request = makeGetRequest(uriBuilder);
        return executeRequest(request);
    }
}
