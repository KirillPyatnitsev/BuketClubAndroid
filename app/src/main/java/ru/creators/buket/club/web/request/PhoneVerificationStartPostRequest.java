package ru.creators.buket.club.web.request;

import android.net.Uri;

import com.google.api.client.http.HttpRequest;

import ru.creators.buket.club.consts.Rest;
import ru.creators.buket.club.web.response.PhoneCodeResponse;

/**
 * Created by mifkamaz on 04/03/16.
 */
public class PhoneVerificationStartPostRequest extends BaseRequest<PhoneCodeResponse> {

    private String accessToken;
    private String telephone;

    public PhoneVerificationStartPostRequest(String accessToken, String telephone) {
        super(PhoneCodeResponse.class);
        this.accessToken = accessToken;
        this.telephone = telephone;
    }

    @Override
    protected Uri.Builder addRestAddress(Uri.Builder uriBuilder) {
        uriBuilder.appendPath(Rest.PROFILE);
        uriBuilder.appendPath(Rest.SEND_CODE);
        return uriBuilder;
    }

    @Override
    public PhoneCodeResponse loadDataFromNetwork() throws Exception {

        HttpRequest request = getPostHttpRequest(null);

        request.getUrl().put(Rest.ACCESS_TOKEN, accessToken);
        request.getUrl().put(Rest.PHONE, telephone);

        return (PhoneCodeResponse) getResponse(request.execute(), PhoneCodeResponse.class, new PhoneCodeResponse());
    }
}
