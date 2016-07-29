package com.opendev.buket.client.model;

import org.codehaus.jackson.annotate.JsonProperty;

import com.opendev.buket.client.consts.Fields;

/**
 * Created by mifkamaz on 13/12/15.
 */
public class DictionaryItem {
    @JsonProperty(Fields.ID)
    public int id;
    @JsonProperty(Fields.VALUE)
    public String value;
}
