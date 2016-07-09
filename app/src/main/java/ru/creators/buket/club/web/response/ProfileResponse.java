package ru.creators.buket.club.web.response;

import org.codehaus.jackson.annotate.JsonProperty;

import ru.creators.buket.club.consts.Fields;
import ru.creators.buket.club.model.Profile;

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
