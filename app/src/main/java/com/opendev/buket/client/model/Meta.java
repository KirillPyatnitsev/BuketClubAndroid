package com.opendev.buket.client.model;

import org.codehaus.jackson.annotate.JsonProperty;

import com.opendev.buket.client.consts.Fields;

/**
 * Created by mifkamaz on 30/11/15.
 */
public class Meta {

    @JsonProperty(Fields.PAGINATION)
    private Pagination pagination;

    public Pagination getPagination() {
        return pagination;
    }

    public void setPagination(Pagination pagination) {
        this.pagination = pagination;
    }
}
