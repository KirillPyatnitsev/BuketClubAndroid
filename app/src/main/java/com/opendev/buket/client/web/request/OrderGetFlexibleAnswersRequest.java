package com.opendev.buket.client.web.request;

import android.net.Uri;

import com.google.api.client.http.HttpRequest;

import com.opendev.buket.client.consts.Fields;
import com.opendev.buket.client.consts.Rest;
import com.opendev.buket.client.web.response.ListAnswerFlexResponse;

/**
 * Created by mifkamaz on 27/11/15.
 */
public class OrderGetFlexibleAnswersRequest extends BaseRequest<ListAnswerFlexResponse> {

    private int orderId;

    public OrderGetFlexibleAnswersRequest(int orderId) {
        super(ListAnswerFlexResponse.class);
        this.orderId = orderId;
    }

    @Override
    public ListAnswerFlexResponse loadDataFromNetwork() throws Exception {
        Uri.Builder uriBuilder = buildUri();
        uriBuilder.appendPath(Rest.ORDERS);
        uriBuilder.appendPath(Integer.toString(orderId));
        uriBuilder.appendPath(Fields.ANSWERS);
        HttpRequest request = makeGetRequest(uriBuilder);
        return executeRequest(request);
    }
}
