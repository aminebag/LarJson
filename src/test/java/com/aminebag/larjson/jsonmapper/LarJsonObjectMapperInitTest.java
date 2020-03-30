package com.aminebag.larjson.jsonmapper;

import com.aminebag.larjson.jsonmapper.model.Model;
import org.junit.jupiter.api.Test;

public class LarJsonObjectMapperInitTest {

    @Test
    void test(){
        LarJsonObjectMapper<Model> modelParser = new LarJsonObjectMapper<>(Model.class);
        System.out.println("");
    }
}
