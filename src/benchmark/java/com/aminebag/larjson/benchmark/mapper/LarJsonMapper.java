package com.aminebag.larjson.benchmark.mapper;

import com.aminebag.larjson.api.LarJsonPerspectives;
import com.aminebag.larjson.benchmark.model.common.Unit;
import com.aminebag.larjson.benchmark.model.larjson.LarJsonBeerHouse;
import com.aminebag.larjson.configuration.LarJsonTypedReadConfiguration;
import com.aminebag.larjson.configuration.LarJsonTypedWriteConfiguration;
import com.aminebag.larjson.configuration.LarJsonTypedWriteConfigurationWrapper;
import com.aminebag.larjson.exception.LarJsonConversionException;
import com.aminebag.larjson.exception.LarJsonException;
import com.aminebag.larjson.mapper.LarJsonTypedMapper;
import com.aminebag.larjson.valueconverter.StringValueConverter;

import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.util.Locale;

/**
 * @author Amine Bagdouri
 */
public class LarJsonMapper implements JsonMapper {

    private final LarJsonTypedMapper<LarJsonBeerHouse> mapper;
    private final LarJsonTypedWriteConfiguration writeConfiguration;
    private File blueprintFile = null;

    public LarJsonMapper(long maxMemoryBlueprintSize) throws IOException {
        mapper = new LarJsonTypedMapper<>(LarJsonBeerHouse.class,
                new LarJsonTypedReadConfiguration.Builder()
                        .setMaxMemoryBlueprintSize(maxMemoryBlueprintSize)
                        .setAllPropertiesRequired(true)
                        .setUnknownJsonAttributeAllowed(false)
                        .setLowerSnakeCase()
                        .setTemporaryFileFactory(() -> {
                            if(blueprintFile != null) {
                                throw new IllegalStateException();
                            } else {
                                return blueprintFile = File.createTempFile("blueprint-", ".larjson");
                            }
                        })
                        .setStringValueConverter(Unit.class, new StringValueConverter<Unit>() {
                            @Override
                            public Unit fromString(String s) throws LarJsonConversionException {
                                if(s == null) {
                                    return null;
                                }
                                try {
                                    return Unit.valueOf(s.toUpperCase());
                                } catch (IllegalArgumentException e) {
                                    throw new LarJsonConversionException(e);
                                }
                            }

                            @Override
                            public String toString(Unit value) {
                                return value.toString().toLowerCase();
                            }
                        })
                        .build());
        writeConfiguration = new LarJsonTypedWriteConfigurationWrapper(mapper.getConfiguration().toWriteConfiguration()) {
            @Override
            public String getIndent() {
                return "";
            }
        };
    }

    @Override
    public LarJsonBeerHouse readObject(File jsonFile) throws IOException, LarJsonException {
        return mapper.readObject(jsonFile);
    }

    @Override
    public void writeObject(Object object, Writer writer) throws IOException, LarJsonException {
        LarJsonPerspectives.write(object, writer, writeConfiguration);
    }

    @Override
    public void closeObject(Object object) throws Exception {
        if(blueprintFile != null && blueprintFile.exists()) {
            System.out.println("Blueprint file size, in bytes : " + String.format(Locale.US, "%,d",
                    blueprintFile.length()));
        }
        mapper.close((LarJsonBeerHouse) object);
    }
}
