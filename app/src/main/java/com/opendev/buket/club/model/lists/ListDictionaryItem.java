package com.opendev.buket.club.model.lists;

import java.util.ArrayList;

import com.opendev.buket.club.model.DictionaryItem;

/**
 * Created by mifkamaz on 13/12/15.
 */
public class ListDictionaryItem extends ArrayList<DictionaryItem> {

    public int getItemId(int index) {
        return this.get(index).id;
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
