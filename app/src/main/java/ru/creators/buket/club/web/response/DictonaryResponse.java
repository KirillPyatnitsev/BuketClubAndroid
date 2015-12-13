package ru.creators.buket.club.web.response;

import org.codehaus.jackson.annotate.JsonProperty;

import java.util.ArrayList;

import ru.creators.buket.club.consts.DictionaryItem;
import ru.creators.buket.club.consts.Fields;


/**
 * Created by mifkamaz on 12/12/15.
 */
public class DictonaryResponse extends DefaultResponse{

    @JsonProperty(Fields.FLOWER_TYPES)
    private ArrayList<DictionaryItem> dictonaryFlowerTypes;

    @JsonProperty(Fields.FLOWER_COLORS)
    private ArrayList<DictionaryItem> dictonaryFloverClors;

    @JsonProperty(Fields.DAY_EVENTS)
    private ArrayList<DictionaryItem> dictonaryDayEvents;

    public ArrayList<DictionaryItem> getDictonaryFlowerTypes() {
        return dictonaryFlowerTypes;
    }

    public ArrayList<DictionaryItem> getDictonaryFloverClors() {
        return dictonaryFloverClors;
    }

    public ArrayList<DictionaryItem> getDictonaryDayEvents() {
        return dictonaryDayEvents;
    }
}
