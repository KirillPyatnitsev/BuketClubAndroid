package com.opendev.buket.client.web.response;

import org.codehaus.jackson.annotate.JsonProperty;

import com.opendev.buket.client.consts.Fields;
import com.opendev.buket.client.model.Session;

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
