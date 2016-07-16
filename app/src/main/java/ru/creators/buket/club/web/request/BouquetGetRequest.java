package ru.creators.buket.club.web.request;

import android.net.Uri;

import com.google.api.client.http.HttpRequest;

import ru.creators.buket.club.consts.Rest;
import ru.creators.buket.club.web.response.BouquetResponse;

/**
 * Created by mifkamaz on 27/11/15.
 */
public class BouquetGetRequest extends BaseRequest<BouquetResponse> {

    private int bouquetId;

    public BouquetGetRequest(int bouquetId) {
        super(BouquetResponse.class);
        this.bouquetId = bouquetId;
    }

    @Override
    public BouquetResponse loadDataFromNetwork() throws Exception {
        Uri.Builder uriBuilder = this.buildUri();
        uriBuilder.appendPath(Rest.BOUQUET_ITEMS);
        uriBuilder.appendPath(String.valueOf(bouquetId));
        HttpRequest request = makeGetRequest(uriBuilder);
        return executeRequest(request);
    }
}
