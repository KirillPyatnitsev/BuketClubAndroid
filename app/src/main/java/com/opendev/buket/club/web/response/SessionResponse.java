package com.opendev.buket.club.web.response;

import org.codehaus.jackson.annotate.JsonProperty;

import com.opendev.buket.club.consts.Fields;
import com.opendev.buket.club.model.Session;

/**
 * Created by mifkamaz on 27/11/15.
 */
public class SessionResponse extends DefaultResponse {
    @JsonProperty(Fields.SESSION)
    private Session session;

    public Session getSession() {
        return session;
    }
}
