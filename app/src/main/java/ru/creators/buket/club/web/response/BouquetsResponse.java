package ru.creators.buket.club.web.response;

import org.codehaus.jackson.annotate.JsonProperty;

import ru.creators.buket.club.consts.Fields;
import ru.creators.buket.club.model.lists.ListBouquet;
import ru.creators.buket.club.model.lists.ListOrder;

/**
 * Created by mifkamaz on 13/12/15.
 */
public class BouquetsResponse extends DefaultResponse{

    @JsonProperty(Fields.BOUQUET_ITEMS)
    private ListBouquet listBouquet;

    public ListBouquet getListBouquet() {
        return listBouquet;
    }
}
