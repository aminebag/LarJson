package com.aminebag.larjson.jsonmapper;

import com.aminebag.larjson.jsonmapper.model.Model;
import org.junit.jupiter.api.Test;

public class LarJsonMapperInitTest {

    @Test
    void test(){
        LarJsonMapper<Model> modelParser = new LarJsonMapper<>(Model.class, null);
        System.out.println("");
    }
}
