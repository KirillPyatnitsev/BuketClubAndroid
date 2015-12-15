package ru.creators.buket.club.web.request;

import android.net.Uri;

import com.google.api.client.http.HttpRequest;

import ru.creators.buket.club.consts.Rest;
import ru.creators.buket.club.web.response.BouquetsResponse;

/**
 * Created by mifkamaz on 27/11/15.
 */
public class BouquetsGetRequest extends BaseRequest<BouquetsResponse> {

    private String accessToken;
    private int flowerTypeId = -1;
    private int flowerColorId = -1;
    private int dayEventId = -1;
    private int minPrice = -1;
    private int maxPrice = -1;
    private int page = -1;
    private int perPage = -1;

    public BouquetsGetRequest(String accessToken, int floverTypeId, int floverColorId, int dayEventId, int minPrice, int maxPrice, int page, int perPage) {
        super(BouquetsResponse.class);
        this.accessToken = accessToken;
        this.flowerTypeId = floverTypeId;
        this.flowerColorId = floverColorId;
        this.dayEventId = dayEventId;
        this.minPrice = minPrice;
        this.maxPrice = maxPrice;
        this.page = page;
        this.perPage = perPage;
    }

    @Override
    protected Uri.Builder addRestAddress(Uri.Builder uriBuilder) {
        uriBuilder.appendPath(Rest.BOUQUET_ITEMS);
        return uriBuilder;
    }

    @Override
    public BouquetsResponse loadDataFromNetwork() throws Exception {
        HttpRequest request = getGetHttpRequest();

        request.getUrl().put(Rest.ACCESS_TOKEN, accessToken);

        if (flowerTypeId != -1){
            request.getUrl().put(Rest.FLOWER_TYPE_ID, flowerTypeId);
        }

        if (flowerColorId != -1){
            request.getUrl().put(Rest.FLOWER_COLOR_ID, flowerColorId);
        }

        if (dayEventId != -1){
            request.getUrl().put(Rest.DAY_EVENT_ID, dayEventId);
        }

        if (minPrice != -1){
            request.getUrl().put(Rest.MIN_PRICE, minPrice);
        }

        if (maxPrice != -1){
            request.getUrl().put(Rest.MAX_PRICE, maxPrice);
        }

        if (page != -1){
            request.getUrl().put(Rest.PAGE, page);
        }

        if (perPage != -1){
            request.getUrl().put(Rest.PER_PAGE, perPage);
        }

        return (BouquetsResponse) getResponse(request.execute(), BouquetsResponse.class, new BouquetsResponse());
    }
}
