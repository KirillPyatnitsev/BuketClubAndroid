package ru.creators.buket.club.model.lists;

import java.util.ArrayList;

import ru.creators.buket.club.model.Shop;

/**
 * Created by mifkamaz on 13/12/15.
 */
public class ListShop extends ArrayList<Shop> {

    public boolean isContainsShopId(int shopId) {
        for (Shop shop : this) {
            if (shop.getId() == shopId)
                return true;
        }

        return false;
    }

    public void removeByShopId(int shopId) {
        for (int i = 0; i < this.size(); i++) {
            if (this.get(i).getId() == shopId) {
                this.remove(i);
                return;
            }
        }
    }
}
