//package com.aminebag.larjson.jsonmapper;
//
//import com.aminebag.larjson.channel.FileRandomReadAccessChannel;
//import com.aminebag.larjson.jsonmapper.configuration.RandomReadAccessChannelFactory;
//import com.aminebag.larjson.exception.LarJsonException;
//import com.aminebag.larjson.api.LarJsonRootArrayNode;
//import com.aminebag.larjson.api.LarJsonRootNode;
//import com.aminebag.larjson.api.LarJsonRootObjectNode;
//
//import java.io.File;
//import java.io.IOException;
//
///**
// * @author Amine Bagdouri
// */
//public class LarJsonGenericMapper {
//
//    public LarJsonRootObjectNode readObject(File jsonFile) throws IOException, LarJsonException {
//        return readObject((conf) -> new FileRandomReadAccessChannel(jsonFile));
//    }
//
//    public LarJsonRootObjectNode readObject(RandomReadAccessChannelFactory channelFactory)
//            throws IOException, LarJsonException {
//        return null;
//    }
//
//    public LarJsonRootArrayNode readArray(File jsonFile) throws IOException, LarJsonException {
//        return readArray((conf) -> new FileRandomReadAccessChannel(jsonFile));
//    }
//
//    public LarJsonRootArrayNode readArray(RandomReadAccessChannelFactory channelFactory)
//            throws IOException, LarJsonException {
//        return null;
//    }
//
//    public LarJsonRootNode readAny(File jsonFile) throws IOException, LarJsonException {
//        return readAny((conf) -> new FileRandomReadAccessChannel(jsonFile));
//    }
//
//    public LarJsonRootNode readAny(RandomReadAccessChannelFactory channelFactory)
//            throws IOException, LarJsonException {
//        return null;
//    }
//}
