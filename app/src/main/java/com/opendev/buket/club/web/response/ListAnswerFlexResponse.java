package com.opendev.buket.club.web.response;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.opendev.buket.club.consts.Fields;
import com.opendev.buket.club.model.lists.ListAnswerFlex;

/**
 * Created by mifkamaz on 13/12/15.
 */
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class ListAnswerFlexResponse extends DefaultResponse {

    @JsonProperty(Fields.ANSWERS)
    private ListAnswerFlex listAnswerFlex;

    public ListAnswerFlex getListAnswerFlex() {
        return listAnswerFlex;
    }

    public void setListAnswerFlex(ListAnswerFlex listAnswerFlex) {
        this.listAnswerFlex = listAnswerFlex;
    }
}
