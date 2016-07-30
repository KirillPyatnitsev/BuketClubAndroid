package com.opendev.buket.club.web.response;

import org.codehaus.jackson.annotate.JsonProperty;

import com.opendev.buket.club.consts.Fields;
import com.opendev.buket.club.model.Profile;

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
