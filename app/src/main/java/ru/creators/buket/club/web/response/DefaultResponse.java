package ru.creators.buket.club.web.response;

import org.codehaus.jackson.annotate.JsonProperty;

import ru.creators.buket.club.consts.Fields;
import ru.creators.buket.club.model.Error;

/**
 * Created by mifkamaz on 19/11/15.
 */
public class DefaultResponse {

    @JsonProperty(Fields.ERROR)
    private Error error;

    private boolean isDone(){
        return error==null;
    }

    public Error getError() {
        return error;
    }

    private Error status;

    public Error getStatus() {
        return status;
    }

    public void setStatus(Error status) {
        this.status = status;
    }

    public boolean isStatusDone(){
        return status.isStatusDone();
    }
}
