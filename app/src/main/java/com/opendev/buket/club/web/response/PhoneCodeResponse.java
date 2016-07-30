package com.opendev.buket.club.web.response;

import org.codehaus.jackson.annotate.JsonProperty;

import com.opendev.buket.club.model.PhoneVerification;

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
