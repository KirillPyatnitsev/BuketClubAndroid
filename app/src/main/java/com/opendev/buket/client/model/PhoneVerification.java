package com.opendev.buket.client.model;

import org.codehaus.jackson.annotate.JsonProperty;

/**
 * Created by mifkamaz on 16/03/16.
 */
public class PhoneVerification {

    @JsonProperty("code")
    private String code;

    public String getCode() {
        return code;
    }
}
