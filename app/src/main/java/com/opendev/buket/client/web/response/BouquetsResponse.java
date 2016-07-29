package com.opendev.buket.client.web.response;

import org.codehaus.jackson.annotate.JsonProperty;

import com.opendev.buket.client.consts.Fields;
import com.opendev.buket.client.model.lists.ListBouquet;

/**
 * Created by mifkamaz on 13/12/15.
 */
public class BouquetsResponse extends DefaultResponse {

    @JsonProperty(Fields.BOUQUET_ITEMS)
    private ListBouquet listBouquet;

    public ListBouquet getListBouquet() {
        return listBouquet;
    }
}
