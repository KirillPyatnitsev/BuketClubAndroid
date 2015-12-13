package ru.creators.buket.club.consts;

import org.codehaus.jackson.annotate.JsonProperty;

/**
 * Created by mifkamaz on 13/12/15.
 */
public class DictionaryItem {
    @JsonProperty(Fields.ID)
    public int id;
    @JsonProperty(Fields.VALUE)
    public String value;
}
