package com.aminebag.larjson.utils;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * @author Amine Bagdouri
 */
public class MutableLongListTest {

    @Test
    void testAdd() {
        MutableLongList mutableLongList = new MutableLongList();
        List<Long> values = new ArrayList<>();
        for(int i=0; i<10_000; i++) {
            values.add((long)(Math.random()*Long.MAX_VALUE));
        }
        for(long v : values) {
            mutableLongList.add(v);
        }
        assertEquals(values.size(), mutableLongList.size());
        for(int i=0; i<values.size(); i++) {
            assertEquals(values.get(i), mutableLongList.get(i));
        }
    }

    @Test
    void testNegativeIndex() {
        MutableLongList mutableLongList = new MutableLongList();
        mutableLongList.add(76L);
        mutableLongList.add(8_863_767_027L);
        try {
            mutableLongList.get(-1);
            fail();
        } catch (IndexOutOfBoundsException expected) {
        }
    }

    @Test
    void testIndexEqualsSize() {
        MutableLongList mutableLongList = new MutableLongList();
        mutableLongList.add(76L);
        mutableLongList.add(8_863_767_027L);
        try {
            mutableLongList.get(2);
            fail();
        } catch (IndexOutOfBoundsException expected) {
        }
    }
}
