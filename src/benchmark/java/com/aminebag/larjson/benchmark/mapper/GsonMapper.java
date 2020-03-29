package com.aminebag.larjson.benchmark.mapper;

import com.aminebag.larjson.benchmark.model.common.Unit;
import com.aminebag.larjson.benchmark.model.gson.GsonBeerHouse;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.*;

/**
 * @author Amine Bagdouri
 */
public class GsonMapper implements JsonMapper {

    private final Gson mapper = new GsonBuilder()
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .registerTypeAdapter(Unit.class, new TypeAdapter<Unit>() {

                @Override
                public void write(JsonWriter out, Unit value) throws IOException {
                    out.value(value.name().toLowerCase());
                }

                @Override
                public Unit read(JsonReader in) throws IOException {
                    return Unit.valueOf(in.nextString().toUpperCase());
                }
            })
            .create();

    @Override
    public GsonBeerHouse readObject(File jsonFile) throws Exception {
        try(BufferedReader reader = new BufferedReader(new FileReader(jsonFile))) {
            return mapper.fromJson(reader, GsonBeerHouse.class);
        }
    }

    @Override
    public void writeObject(Object object, Writer writer) throws Exception {
        mapper.toJson(object, writer);
    }

    @Override
    public void closeObject(Object object) throws Exception {

    }
}
