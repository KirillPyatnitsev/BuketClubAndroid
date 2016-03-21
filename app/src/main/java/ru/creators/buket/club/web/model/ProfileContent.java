package ru.creators.buket.club.web.model;

import org.codehaus.jackson.annotate.JsonProperty;

import ru.creators.buket.club.consts.Fields;
import ru.creators.buket.club.model.Profile;

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
