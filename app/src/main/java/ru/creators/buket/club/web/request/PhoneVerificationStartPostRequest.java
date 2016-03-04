package ru.creators.buket.club.web.request;

import android.net.Uri;

import com.google.api.client.http.HttpRequest;

import ru.creators.buket.club.consts.Rest;
import ru.creators.buket.club.web.response.DefaultResponse;

/**
 * Created by mifkamaz on 04/03/16.
 */
public class PhoneVerificationStartPostRequest extends BaseRequest<DefaultResponse>{

    private String accessToken;
    private String telephone;

    public PhoneVerificationStartPostRequest(String accessToken, String telephone) {
        super(DefaultResponse.class);
        this.accessToken = accessToken;
        this.telephone = telephone;
    }

    @Override
    protected Uri.Builder addRestAddress(Uri.Builder uriBuilder) {
        return uriBuilder;
    }

    @Override
    public DefaultResponse loadDataFromNetwork() throws Exception {

        HttpRequest request = getPostHttpRequest(null);

        request.getUrl().put(Rest.ACCESS_TOKEN, accessToken);
        request.getUrl().put(Rest.PHONE, telephone);

        return new DefaultResponse();
//        return getResponse(request.execute(), DefaultResponse.class, new DefaultResponse());
    }
}
