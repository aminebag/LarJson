package com.aminebag.larjson.mapper;

import java.util.List;

/**
 * @author Amine Bagdouri
 */
public class LarJsonMapperSubListTestUtils {

    public static List<String> defaultList(List<String> list) {
        return list.subList(1,6);
    }

    public static List<String> emptyList(List<String> list) {
        return list.subList(0,0);
    }

    public static List<String> nullFreeList(List<String> list) {
        return list.subList(1, 5);
    }

    public static String defaultJsonArray() {
        return "{\"whatever\" : [\"what\", \"hello\", \"salut\", null, \"null\", \"salut\", \"\", \"meh\"]}";
    }

    public static String emptyJsonArray() {
        return "{\"whatever\" : [\"what\", \"\", \"meh\"]}";
    }

    public static String nullFreeJsonArray() {
        return "{\"whatever\" : [\"what\", \"hello\", \"salut\", \"null\", \"salut\", \"\", \"meh\"]}";
    }
}
