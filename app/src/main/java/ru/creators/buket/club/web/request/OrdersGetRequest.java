package ru.creators.buket.club.web.request;

import android.net.Uri;

import com.google.api.client.http.HttpRequest;

import ru.creators.buket.club.consts.Rest;
import ru.creators.buket.club.web.response.OrdersResponse;

/**
 * Created by mifkamaz on 27/11/15.
 */
public class OrdersGetRequest extends BaseRequest<OrdersResponse> {

    private int page;
    private int perPage;

    public OrdersGetRequest(int page, int perPage) {
        super(OrdersResponse.class);
        this.page = page;
        this.perPage = perPage;
    }

    @Override
    public OrdersResponse loadDataFromNetwork() throws Exception {
        Uri.Builder uriBuilder = buildUri();
        uriBuilder.appendPath(Rest.PROFILE);
        uriBuilder.appendPath(Rest.ORDERS);
        HttpRequest request = makeGetRequest(uriBuilder);

        request.getUrl().put(Rest.PAGE, page);
        request.getUrl().put(Rest.PER_PAGE, perPage);

        return executeRequest(request);
    }
}
