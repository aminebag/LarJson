//package com.aminebag.larjson.api;
//
//import java.util.List;
//
///**
// * @author Amine Bagdouri
// */
//public interface LarJsonArrayNode extends LarJsonNode, List<Object> {
//
//    List<LarJsonNode> asNodeList();
//
//    String getString(int index);
//    Boolean getBoolean(int index);
//    Number getNumber(int index);
//    LarJsonObjectNode getObjectNode(int index);
//    LarJsonArrayNode getArrayNode(int index);
//
//    @Override
//    default LarJsonArrayNode asArrayNode(LarJsonArrayNode defaultValue) {
//        return this;
//    }
//
//    @Override
//    default LarJsonArrayNode asArrayNode() {
//        return this;
//    }
//
//    @Override
//    default Type getType() {
//        return Type.ARRAY;
//    }
//}
