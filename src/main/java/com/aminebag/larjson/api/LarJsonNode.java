//package com.aminebag.larjson.api;
//
///**
// * @author Amine Bagdouri
// */
//public interface LarJsonNode extends LarJsonGenericWriteable, LarJsonElement {
//
//    Type getType();
//
//    default LarJsonArrayNode asArrayNode(LarJsonArrayNode defaultValue) {
//        return defaultValue;
//    }
//
//    default LarJsonArrayNode asArrayNode() {
//        return null;
//    }
//
//    default LarJsonObjectNode asObjectNode(LarJsonObjectNode defaultValue) {
//        return defaultValue;
//    }
//
//    default LarJsonObjectNode asObjectNode() {
//        return null;
//    }
//
//    default boolean asBoolean(boolean defaultValue) {
//        return defaultValue;
//    }
//
//    default Boolean asBoolean() {
//        return null;
//    }
//
//    default Number asNumber(Number defaultValue) {
//        return defaultValue;
//    }
//
//    default Number asNumber() {
//        return null;
//    }
//
//    default String asString(String defaultValue) {
//        return defaultValue;
//    }
//
//    default String asString() {
//        return null;
//    }
//
//    enum Type {
//        ARRAY, OBJECT, STRING, NUMBER, BOOLEAN, NULL
//    }
//}
