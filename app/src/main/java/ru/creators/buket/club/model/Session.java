package ru.creators.buket.club.model;

import org.codehaus.jackson.annotate.JsonProperty;

import ru.creators.buket.club.consts.Fields;

/**
 * Created by mifkamaz on 27/11/15.
 */
public class Session {

    @JsonProperty(Fields.APP_MODE)
    private int appMode;

    @JsonProperty(Fields.ACCESS_TOKEN)
    private String accessToken;

    @JsonProperty(Fields.CREATED_AT)
    private String createdAt;

    public String getAccessToken() {
        return accessToken;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public int getAppMode() {
        return appMode;
    }

    public void setAppMode(int appMode) {
        this.appMode = appMode;
    }
}
