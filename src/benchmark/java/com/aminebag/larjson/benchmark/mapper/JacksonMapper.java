package com.aminebag.larjson.benchmark.mapper;

import com.aminebag.larjson.benchmark.model.common.Unit;
import com.aminebag.larjson.benchmark.model.jackson.JacksonBeerHouse;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.BeanDeserializerModifier;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.File;
import java.io.IOException;
import java.io.Writer;

/**
 * @author Amine Bagdouri
 */
public class JacksonMapper implements JsonMapper {

    private final ObjectMapper mapper = new ObjectMapper();

    public JacksonMapper() {
        mapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
        SimpleModule module = new SimpleModule();
        module.setDeserializerModifier(new BeanDeserializerModifier() {
            @Override
            public JsonDeserializer<Unit> modifyEnumDeserializer(DeserializationConfig config,
                                                                 final JavaType type,
                                                                 BeanDescription beanDesc,
                                                                 final JsonDeserializer<?> deserializer) {
                return new JsonDeserializer<Unit>() {
                    @Override
                    public Unit deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
                        return Enum.valueOf(Unit.class, jp.getValueAsString().toUpperCase());
                    }
                };
            }
        });
        module.addSerializer(Enum.class, new StdSerializer<Enum>(Enum.class) {
            @Override
            public void serialize(Enum value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
                jgen.writeString(value.name().toLowerCase());
            }
        });
        mapper.registerModule(module);
    }

    @Override
    public JacksonBeerHouse readObject(File jsonFile) throws Exception {
        return mapper.readValue(jsonFile, JacksonBeerHouse.class);
    }

    @Override
    public void writeObject(Object object, Writer writer) throws Exception {
        mapper.writer().writeValue(writer, object);
    }

    @Override
    public void closeObject(Object object) {

    }
}
