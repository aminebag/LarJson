package com.aminebag.larjson.mapper.propertymapper;

import com.aminebag.larjson.api.LarJsonCloneable;
import com.aminebag.larjson.api.LarJsonPath;
import com.aminebag.larjson.blueprint.LarJsonBlueprintReader;
import com.aminebag.larjson.blueprint.LarJsonBlueprintWriter;
import com.aminebag.larjson.configuration.LarJsonTypedWriteConfiguration;
import com.aminebag.larjson.configuration.PropertyResolver;
import com.aminebag.larjson.exception.LarJsonException;
import com.aminebag.larjson.mapper.LarJsonMapperUtils;
import com.aminebag.larjson.mapper.element.LarJsonContext;
import com.aminebag.larjson.mapper.element.LarJsonListImpl;
import com.aminebag.larjson.mapper.valueoverwriter.ValueOverwriter;
import com.aminebag.larjson.parser.LarJsonParseException;
import com.aminebag.larjson.parser.LarJsonToken;
import com.aminebag.larjson.parser.LarJsonTokenParser;
import com.aminebag.larjson.utils.LongList;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Amine Bagdouri
 *
 * A mapper for {@link List} model properties
 */
public class ListLarJsonPropertyMapper extends LarJsonPropertyMapper<List> {

    private final LarJsonPropertyMapper<?> mapper;

    public ListLarJsonPropertyMapper(String name, Method getterMethod, Method setterMethod, int getterIndex,
                                     int setterIndex, boolean required, LarJsonPropertyMapper<?> mapper) {
        super(name, getterMethod, setterMethod, getterIndex, setterIndex, required);
        this.mapper = mapper;
    }

    @Override
    public List calculateValue(LarJsonContext context, long key, long parentJsonPosition, long parentBlueprintPosition
            , LarJsonPath parentPath , String pathElement) throws IOException, LarJsonException {
        LarJsonBlueprintReader blueprintReader = context.getBlueprintReader();
        blueprintReader.position(parentBlueprintPosition + key);
        long jsonPosition = parentJsonPosition + blueprintReader.get();

        return context.getCachedValue(jsonPosition, ()->calculateCacheableValue(context, jsonPosition, parentPath, pathElement));
    }

    private List calculateCacheableValue(LarJsonContext context, long jsonPosition, LarJsonPath parentPath
            , String pathElement) throws IOException, LarJsonException {
        LarJsonBlueprintReader blueprintReader = context.getBlueprintReader();

        long blueprintLength = blueprintReader.get();
        long blueprintPosition = blueprintReader.position() - blueprintLength;

        LongList keys = blueprintReader.getList();

        return new LarJsonListImpl<>(mapper, jsonPosition, blueprintPosition, keys, context, parentPath, pathElement);
    }

    @Override
    public void write(LarJsonContext context, long key, long parentJsonPosition, long parentBlueprintPosition,
                      JsonWriter jsonWriter, ValueOverwriter valueOverwriter,
                      LarJsonTypedWriteConfiguration writeConfiguration, PropertyResolver propertyResolver)
            throws IOException, LarJsonException {
        jsonWriter.beginArray();
        LarJsonBlueprintReader blueprintReader = context.getBlueprintReader();
        blueprintReader.position(parentBlueprintPosition + key);
        long jsonPosition = parentJsonPosition + blueprintReader.get();
        List overwrittenList = valueOverwriter.getListOrDefault(jsonPosition, null);
        if(overwrittenList != null) {
            for(Object o : overwrittenList) {
                LarJsonMapperUtils.writeValue(jsonWriter, o, writeConfiguration);
            }
        } else {
            long blueprintLength = blueprintReader.get();
            long blueprintPosition = blueprintReader.position() - blueprintLength;

            LongList keys = blueprintReader.getList();
            for (int i = 0; i < keys.size(); i++) {
                long subKey = keys.get(i);
                if(subKey == 0L) {
                    jsonWriter.nullValue();
                    continue;
                }
                mapper.write(context, subKey - 1, jsonPosition, blueprintPosition, jsonWriter, valueOverwriter,
                        writeConfiguration, propertyResolver);
            }
        }
        jsonWriter.endArray();
    }

    public void deepClone(LarJsonContext context, long key, long parentJsonPosition, long parentBlueprintPosition,
                          ValueOverwriter sourceValueOverwriter, ValueOverwriter destValueOverwriter) throws IOException {
        if(key < 0L) {
            return;
        }
        LarJsonBlueprintReader blueprintReader = context.getBlueprintReader();
        blueprintReader.position(parentBlueprintPosition + key);
        long jsonPosition = parentJsonPosition + blueprintReader.get();
        List overwrittenList = sourceValueOverwriter.getListOrDefault(jsonPosition, null);
        if(overwrittenList != null) {
            deepCloneOverwrittenList(destValueOverwriter, jsonPosition, overwrittenList);
        } else {
            long blueprintLength = blueprintReader.get();
            long blueprintPosition = blueprintReader.position() - blueprintLength;

            LongList keys = blueprintReader.getList();
            deepCloneUnmodifiedList(context, sourceValueOverwriter, destValueOverwriter, jsonPosition, mapper,
                    blueprintPosition, keys);
        }
    }

    @Override
    public List nullValue() {
        return null;
    }

    public static void deepCloneUnmodifiedList(LarJsonContext context, ValueOverwriter sourceValueOverwriter,
                                               ValueOverwriter destValueOverwriter, long jsonPosition,
                                               LarJsonPropertyMapper<?> mapper, long blueprintPosition,
                                               LongList keys) throws IOException {
        for (int i = 0; i < keys.size(); i++) {
            long subKey = keys.get(i);
            mapper.deepClone(context, subKey - 1, jsonPosition, blueprintPosition,
                    sourceValueOverwriter, destValueOverwriter);
        }
    }

    public static void deepCloneOverwrittenList(ValueOverwriter destValueOverwriter, long jsonPosition,
                                           List overwrittenList) {
        List newList = new ArrayList<>(overwrittenList.size());
        for(Object o : overwrittenList) {
            if(o instanceof LarJsonCloneable) {
                o = ((LarJsonCloneable)o).clone();
            }
            newList.add(o);
        }
        destValueOverwriter.putListIfAbsent(jsonPosition, newList);
    }

    @Override
    public long enrichBlueprint(LarJsonContext context, LarJsonBlueprintWriter blueprintWriter,
                                LarJsonTokenParser tokenParser, long parentJsonPosition, long parentBlueprintPosition)
            throws IOException, LarJsonException {
        long absoluteBlueprintPosition = blueprintWriter.position();
        LarJsonToken token = tokenParser.peek();
        if(token == LarJsonToken.BEGIN_ARRAY) {
            long absoluteJsonPosition = tokenParser.getCurrentPosition();
            tokenParser.beginArray();
            long relativeJsonPosition = absoluteJsonPosition - parentJsonPosition;
            List<Long> keys = new ArrayList<>();
            long maxKey = 0L;
            while (tokenParser.peek() != LarJsonToken.END_ARRAY) {
                if(tokenParser.peek() == LarJsonToken.NULL) {
                    tokenParser.nextNull();
                    keys.add(0L);
                    continue;
                }
                long key = mapper.enrichBlueprint(context, blueprintWriter, tokenParser,
                        absoluteJsonPosition, absoluteBlueprintPosition) + 1;
                keys.add(key);
                if(key > maxKey) {
                    maxKey = key;
                }
            }
            tokenParser.endArray();
            blueprintWriter.putList(keys, maxKey);
            long blueprintEnd = blueprintWriter.position();
            blueprintWriter.put(blueprintEnd - absoluteBlueprintPosition);
            blueprintWriter.put(relativeJsonPosition);
            return blueprintWriter.position() - parentBlueprintPosition;
        } else {
            throw new LarJsonParseException(
                    String.format("Expected array, but found %s at byte position %d",
                            token.name(), tokenParser.getCurrentPosition()));
        }
    }

    @Override
    public Class<List> getType() {
        return List.class;
    }
}
