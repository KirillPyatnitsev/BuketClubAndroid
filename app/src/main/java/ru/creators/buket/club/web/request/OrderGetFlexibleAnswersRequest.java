package ru.creators.buket.club.web.request;

import android.net.Uri;

import com.google.api.client.http.HttpRequest;

import ru.creators.buket.club.consts.Fields;
import ru.creators.buket.club.consts.Rest;
import ru.creators.buket.club.web.response.ListAnswerFlexResponse;

/**
 * Created by mifkamaz on 27/11/15.
 */
public class OrderGetFlexibleAnswersRequest extends BaseRequest<ListAnswerFlexResponse> {

    private String accessToken;
    private int orderId;

    public OrderGetFlexibleAnswersRequest(String accessToken, int orderId) {
        super(ListAnswerFlexResponse.class);
        this.accessToken = accessToken;
        this.orderId = orderId;
    }

    @Override
    protected Uri.Builder addRestAddress(Uri.Builder uriBuilder) {
        uriBuilder.appendPath(Rest.ORDERS);
        uriBuilder.appendPath(Integer.toString(orderId));
        uriBuilder.appendPath(Fields.ANSWERS);
        return uriBuilder;
    }

    @Override
    public ListAnswerFlexResponse loadDataFromNetwork() throws Exception {
        HttpRequest request = getGetHttpRequest();

        request.getUrl().put(Rest.ACCESS_TOKEN, accessToken);

        return (ListAnswerFlexResponse) getResponse(request.execute(), ListAnswerFlexResponse.class, new ListAnswerFlexResponse());
    }
}
