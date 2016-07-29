package com.opendev.buket.client.web.response;

import org.codehaus.jackson.annotate.JsonProperty;

import com.opendev.buket.client.model.PhoneVerification;

/**
 * Created by mifkamaz on 16/03/16.
 */
public class PhoneCodeResponse extends DefaultResponse {

    @JsonProperty("phone_verification")
    private PhoneVerification phoneVerification;

    public PhoneVerification getPhoneVerification() {
        return phoneVerification;
    }
}
