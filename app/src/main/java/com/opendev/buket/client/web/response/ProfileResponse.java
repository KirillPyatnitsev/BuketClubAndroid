package com.opendev.buket.client.web.response;

import org.codehaus.jackson.annotate.JsonProperty;

import com.opendev.buket.client.consts.Fields;
import com.opendev.buket.client.model.Profile;

/**
 * Created by mifkamaz on 12/12/15.
 */
public class ProfileResponse extends DefaultResponse {

    @JsonProperty(Fields.PROFILE)
    private Profile profile;

    public Profile getProfile() {
        return profile;
    }
}
