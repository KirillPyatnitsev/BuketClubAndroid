package ru.creators.buket.club.web.response;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import ru.creators.buket.club.consts.Fields;
import ru.creators.buket.club.model.Error;
import ru.creators.buket.club.model.Meta;

/**
 * Created by mifkamaz on 19/11/15.
 */
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class DefaultResponse {

    @JsonProperty(Fields.ERROR)
    private Error error;

    @JsonProperty(Fields.META)
    private Meta meta;

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

    public Meta getMeta() {
        return meta;
    }
}
