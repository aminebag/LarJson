package com.aminebag.larjson.mapper;

import java.util.List;

/**
 * @author Amine Bagdouri
 */
public class LarJsonTypedMapperSingleThreadedMutableSubListTest extends LarJsonTypedMapperSingleThreadedMutableListTest {

    @Override
    protected List<String> defaultList(List<String> list) {
        return LarJsonMapperSubListTestUtils.defaultList(list);
    }

    @Override
    protected List<String> emptyList(List<String> list) {
        return LarJsonMapperSubListTestUtils.emptyList(list);
    }

    @Override
    protected List<String> nullFreeList(List<String> list) {
        return LarJsonMapperSubListTestUtils.nullFreeList(list);
    }

    @Override
    protected String defaultJsonArray() {
        return LarJsonMapperSubListTestUtils.defaultJsonArray();
    }

    @Override
    protected String emptyJsonArray() {
        return LarJsonMapperSubListTestUtils.emptyJsonArray();
    }

    @Override
    protected String nullFreeJsonArray() {
        return LarJsonMapperSubListTestUtils.nullFreeJsonArray();
    }
}
