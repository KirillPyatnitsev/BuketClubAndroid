package ru.creators.buket.club.web.response;

import org.codehaus.jackson.annotate.JsonProperty;

import ru.creators.buket.club.consts.Fields;
import ru.creators.buket.club.model.Shop;
import ru.creators.buket.club.model.lists.ListShop;

/**
 * Created by mifkamaz on 13/12/15.
 */
public class ShopListResponse extends DefaultResponse{

    @JsonProperty(Fields.SHOPS)
    private ListShop listShop;

    public ListShop getListShop() {
        return listShop;
    }
}
