package com.aminebag.larjson.parser;

import com.aminebag.larjson.parser.model.Model;
import org.junit.jupiter.api.Test;

public class LarJsonObjectParserInitTest {

    @Test
    void test(){
        LarJsonObjectParser<Model> modelParser = new LarJsonObjectParser<>(Model.class);
        System.out.println("");
    }
}
