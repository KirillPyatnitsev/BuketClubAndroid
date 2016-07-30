package com.opendev.buket.club.web.model;

import org.codehaus.jackson.annotate.JsonProperty;

import com.opendev.buket.club.consts.Fields;
import com.opendev.buket.club.model.Profile;

/**
 * Created by mifkamaz on 17/03/16.
 */
public class ProfileContent {

    @JsonProperty(Fields.PROFILE)
    private Profile profile;

    public ProfileContent(Profile profile) {
        this.profile = profile;
    }

}
