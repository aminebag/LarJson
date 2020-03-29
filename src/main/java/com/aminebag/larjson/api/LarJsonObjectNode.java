//package com.aminebag.larjson.api;
//
//import java.util.Map;
//
///**
// * @author Amine Bagdouri
// */
//public interface LarJsonObjectNode extends LarJsonNode, Map<String, Object> {
//
//    Map<String, LarJsonNode> asNodeMap();
//
//    String getString(String key);
//    Boolean getBoolean(String key);
//    Number getNumber(String key);
//    LarJsonObjectNode getObjectNode(String key);
//    LarJsonArrayNode getArrayNode(String key);
//
//    @Override
//    default LarJsonObjectNode asObjectNode(LarJsonObjectNode defaultValue) {
//        return this;
//    }
//
//    @Override
//    default LarJsonObjectNode asObjectNode() {
//        return this;
//    }
//
//    @Override
//    default Type getType() {
//        return Type.OBJECT;
//    }
//}
