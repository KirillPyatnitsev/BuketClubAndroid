package ru.creators.buket.club.model.lists;

import java.util.ArrayList;

import ru.creators.buket.club.model.DictionaryItem;

/**
 * Created by mifkamaz on 13/12/15.
 */
public class ListDictionaryItem extends ArrayList<DictionaryItem> {
    public String getValueByItemId(int itemId) {
        for (DictionaryItem dictionaryItem : this) {
            if (dictionaryItem.id == itemId) {
                return dictionaryItem.value;
            }
        }
        return null;
    }

    public int getItemIdByValue(int itemValue) {
        for (DictionaryItem dictionaryItem : this) {
            if (dictionaryItem.value.equals(itemValue)) {
                return dictionaryItem.id;
            }
        }
        return -1;
    }

    public int getItemId(int index) {
        return this.get(index).id;
    }

    public int getIdByItemId(int itemId) {
        for (int i = 0; i < this.size(); i++) {
            if (this.getItemId(i) == itemId) {
                return i;
            }
        }
        return -1;
    }

    public String getValue(int index) {
        return this.get(index).value;
    }

    public String[] getValuesArray() {
        String[] ret = new String[this.size()];
        for (int i = 0; i < this.size(); i++) {
            ret[i] = this.getValue(i);
        }

        return ret;
    }

}
