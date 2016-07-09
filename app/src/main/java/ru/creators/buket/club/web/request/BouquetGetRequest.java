package ru.creators.buket.club.web.request;

import android.net.Uri;

import com.google.api.client.http.HttpRequest;

import ru.creators.buket.club.consts.Rest;
import ru.creators.buket.club.web.response.BouquetResponse;

/**
 * Created by mifkamaz on 27/11/15.
 */
public class BouquetGetRequest extends BaseRequest<BouquetResponse> {

    private String accessToken;
    private int bouquetId;

    public BouquetGetRequest(String accessToken, int bouquetId) {
        super(BouquetResponse.class);
        this.accessToken = accessToken;
        this.bouquetId = bouquetId;
    }

    @Override
    protected Uri.Builder addRestAddress(Uri.Builder uriBuilder) {
        uriBuilder.appendPath(Rest.BOUQUET_ITEMS);
        uriBuilder.appendPath(String.valueOf(bouquetId));
        return uriBuilder;
    }

    @Override
    public BouquetResponse loadDataFromNetwork() throws Exception {
        HttpRequest request = getGetHttpRequest();

        request.getUrl().put(Rest.ACCESS_TOKEN, accessToken);

        return (BouquetResponse) getResponse(request.execute(), BouquetResponse.class);
    }
}
