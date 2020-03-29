package com.aminebag.larjson.mapper.element;

import com.aminebag.larjson.blueprint.LarJsonBlueprintReader;
import com.aminebag.larjson.configuration.LarJsonTypedReadConfiguration;
import com.aminebag.larjson.configuration.PropertyResolver;
import com.aminebag.larjson.exception.LarJsonException;
import com.aminebag.larjson.mapper.cache.LarJsonValueCalculator;
import com.aminebag.larjson.mapper.valueoverwriter.ValueOverwriter;
import com.aminebag.larjson.stream.CharacterStream;

import java.io.IOException;

/**
 * @author Amine Bagdouri
 *
 * A context that provides access to objects needed to build a LarJson model object/array
 */
public interface LarJsonContext {

    LarJsonTypedReadConfiguration getReadConfiguration();

    LarJsonBlueprintReader getBlueprintReader() throws IOException;

    CharacterStream getCharacterStream(long jsonPosition) throws IOException;

    <T> T getCachedValue(long jsonPosition, LarJsonValueCalculator<T> valueCalculator) throws IOException, LarJsonException;

    ValueOverwriter getValueOverwriter();

    PropertyResolver getPropertyResolver();

    void checkClosed();

    Class<?> getRootInterface();
}
