package com.opendev.buket.client.web.response;

import org.codehaus.jackson.annotate.JsonProperty;

import com.opendev.buket.client.consts.Fields;
import com.opendev.buket.client.model.lists.ListDictionaryItem;


/**
 * Created by mifkamaz on 12/12/15.
 */
public class DictionaryResponse extends DefaultResponse {

    @JsonProperty(Fields.FLOWER_TYPES)
    private ListDictionaryItem dictonaryFlowerTypes;

    @JsonProperty(Fields.FLOWER_COLORS)
    private ListDictionaryItem dictonaryFloverClors;

    @JsonProperty(Fields.DAY_EVENTS)
    private ListDictionaryItem dictonaryDayEvents;

    public ListDictionaryItem getDictonaryFlowerTypes() {
        return dictonaryFlowerTypes;
    }

    public ListDictionaryItem getDictonaryFloverClors() {
        return dictonaryFloverClors;
    }

    public ListDictionaryItem getDictonaryDayEvents() {
        return dictonaryDayEvents;
    }
}
