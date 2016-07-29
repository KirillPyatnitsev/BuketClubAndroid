package com.opendev.buket.client.web.response;

import org.codehaus.jackson.annotate.JsonProperty;

import com.opendev.buket.client.consts.Fields;
import com.opendev.buket.client.model.Bouquet;

/**
 * Created by mifkamaz on 13/12/15.
 */
public class BouquetResponse extends DefaultResponse {

    @JsonProperty(Fields.BOUQUET_ITEM)
    private Bouquet bouquet;

    public Bouquet getBouquet() {
        return bouquet;
    }
}
