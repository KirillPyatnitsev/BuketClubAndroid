package ru.creators.buket.club.web.response;

import org.codehaus.jackson.annotate.JsonProperty;

import java.util.ArrayList;

import ru.creators.buket.club.model.DictionaryItem;
import ru.creators.buket.club.consts.Fields;
import ru.creators.buket.club.model.lists.ListDictionaryItem;


/**
 * Created by mifkamaz on 12/12/15.
 */
public class DictionaryResponse extends DefaultResponse{

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
