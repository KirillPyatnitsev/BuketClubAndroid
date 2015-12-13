package ru.creators.buket.club.web.response;

import org.codehaus.jackson.annotate.JsonProperty;

import ru.creators.buket.club.consts.Fields;
import ru.creators.buket.club.model.Session;

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
