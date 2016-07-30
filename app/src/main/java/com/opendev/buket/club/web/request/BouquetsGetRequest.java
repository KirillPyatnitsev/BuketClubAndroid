package com.opendev.buket.club.web.request;

import android.net.Uri;

import com.google.api.client.http.HttpRequest;

import com.opendev.buket.club.consts.Rest;
import com.opendev.buket.club.web.response.BouquetsResponse;

/**
 * Created by mifkamaz on 27/11/15.
 */
public class BouquetsGetRequest extends BaseRequest<BouquetsResponse> {

    private int flowerTypeId = -1;
    private int flowerColorId = -1;
    private int dayEventId = -1;
    private int minPrice = -1;
    private int maxPrice = -1;
    private int page = -1;
    private int perPage = -1;

    public BouquetsGetRequest(int flowerTypeId, int flowerColorId, int dayEventId, int minPrice, int maxPrice, int page, int perPage) {
        super(BouquetsResponse.class);
        this.flowerTypeId = flowerTypeId;
        this.flowerColorId = flowerColorId;
        this.dayEventId = dayEventId;
        this.minPrice = minPrice;
        this.maxPrice = maxPrice;
        this.page = page;
        this.perPage = perPage;
    }

    @Override
    public BouquetsResponse loadDataFromNetwork() throws Exception {
        Uri.Builder uriBuilder = buildUri();
        uriBuilder.appendPath(Rest.BOUQUET_ITEMS);
        HttpRequest request = makeGetRequest(uriBuilder);

        if (flowerTypeId != -1) {
            request.getUrl().put(Rest.FLOWER_TYPE_ID, flowerTypeId);
        }

        if (flowerColorId != -1) {
            request.getUrl().put(Rest.FLOWER_COLOR_ID, flowerColorId);
        }

        if (dayEventId != -1) {
            request.getUrl().put(Rest.DAY_EVENT_ID, dayEventId);
        }

        if (minPrice != -1) {
            request.getUrl().put(Rest.MIN_PRICE, minPrice);
        }

        if (maxPrice != -1) {
            request.getUrl().put(Rest.MAX_PRICE, maxPrice);
        }

        if (page != -1) {
            request.getUrl().put(Rest.PAGE, page);
        }

        if (perPage != -1) {
            request.getUrl().put(Rest.PER_PAGE, perPage);
        }

        return executeRequest(request);
    }
}
