package com.opendev.buket.club.web.request;

import android.net.Uri;

import com.google.api.client.http.HttpRequest;
import com.opendev.buket.club.consts.Rest;
import com.opendev.buket.club.web.response.AlphaPayResponse;

/**
 * Created by Danis on 16.11.2016.
 */
public class AlphaPayRequest extends BaseRequest<AlphaPayResponse> {

    String username;
    String password;
    String orderNumber;
    String amount;
    String returnUrl;
    String failUrl;

    public AlphaPayRequest(String username, String password, String orderNumber, String amount, String returnUrl, String failUrl) {
        super(AlphaPayResponse.class);
        this.username = username;
        this.password = password;
        this.orderNumber = orderNumber;
        this.amount = amount;
        this.returnUrl = returnUrl;
        this.failUrl = failUrl;
    }

    @Override
    public AlphaPayResponse loadDataFromNetwork() throws Exception {
        Uri.Builder uriBuilder = buildUri2();
        uriBuilder.appendPath(Rest.REGISTER_DO);
        HttpRequest request = makeGetRequest(uriBuilder);

        request.getUrl().put(Rest.USERNAME, username);
        request.getUrl().put(Rest.PASSWORD, password);
        request.getUrl().put(Rest.ORDER_NUMBER, orderNumber);
        request.getUrl().put(Rest.AMOUNT, amount);
        request.getUrl().put(Rest.RETURN_URL, returnUrl);
        request.getUrl().put(Rest.FAIL_URL, failUrl);

        return executeRequest(request);
    }
}
