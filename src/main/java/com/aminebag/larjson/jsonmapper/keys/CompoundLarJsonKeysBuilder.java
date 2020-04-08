package com.aminebag.larjson.jsonmapper.keys;

import java.util.ArrayList;
import java.util.List;

public class CompoundLarJsonKeysBuilder {
    private final int elementLength;
    private List<FixedLengthKeys> keysList;

    public CompoundLarJsonKeysBuilder(int elementLength) {
        this.elementLength = elementLength;
    }

    private FixedLengthKeys getLast() {
        return keysList.get(keysList.size()-1);
    }

    public void addElement() {
        if(keysList == null) {
            keysList = new ArrayList<>();
            keysList.add(LarJsonKeys.createFixedLength(elementLength));
        } else {
            keysList.add(getLast().reproduce());
        }
    }

    public void setKey(int index, long key){
        FixedLengthKeys last = getLast();
        FixedLengthKeys produced = last.setKey(index, key);
        if(last != produced) {
            keysList.set(keysList.size()-1, produced);
        }
    }
    
    public LarJsonKeys build(){
        if(keysList == null) {
            return LarJsonKeys.EMPTY_KEYS;
        } else if (keysList.size() == 1) {
            return keysList.get(0);
        } else {
            return getLast().concat(keysList);
        }
    }

}
