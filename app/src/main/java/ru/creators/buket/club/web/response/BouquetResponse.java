package ru.creators.buket.club.web.response;

import org.codehaus.jackson.annotate.JsonProperty;

import ru.creators.buket.club.consts.Fields;
import ru.creators.buket.club.model.Bouquet;

/**
 * Created by mifkamaz on 13/12/15.
 */
public class BouquetResponse extends DefaultResponse{

    @JsonProperty(Fields.BOUQUET_ITEM)
    private Bouquet bouquet;

    public Bouquet getBouquet() {
        return bouquet;
    }
}
