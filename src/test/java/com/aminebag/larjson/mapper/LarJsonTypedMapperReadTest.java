package com.aminebag.larjson.mapper;

import com.aminebag.larjson.api.LarJsonRootList;
import com.aminebag.larjson.configuration.LarJsonTypedReadConfiguration;
import com.aminebag.larjson.exception.LarJsonException;
import com.aminebag.larjson.exception.LarJsonValueReadException;
import com.aminebag.larjson.mapper.exception.LarJsonConstraintViolationException;
import com.aminebag.larjson.parser.LarJsonParseException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.file.Path;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;

import static com.aminebag.larjson.mapper.LarJsonMapperTestUtils.jsonToFile;
import static com.aminebag.larjson.mapper.LarJsonTypedMapperTestModels.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Amine Bagdouri
 */
public class LarJsonTypedMapperReadTest {

    @Test
    void testReadObjectWithPrimitiveBoolean(@TempDir Path tempDir) throws IOException, LarJsonException {
        LarJsonTypedMapper<ModelWithPrimitiveBoolean> mapper = new LarJsonTypedMapper<>(ModelWithPrimitiveBoolean.class);
        String json = "{\"whatever\" : true}";
        try(ModelWithPrimitiveBoolean model = mapper.readObject(jsonToFile(tempDir, json))) {
            assertNotNull(model);
            assertTrue(model.isWhatever());
        }
    }

    @Test
    void testReadObjectWithPrimitiveBooleanNull(@TempDir Path tempDir) throws IOException, LarJsonException {
        LarJsonTypedMapper<ModelWithPrimitiveBoolean> mapper = new LarJsonTypedMapper<>(ModelWithPrimitiveBoolean.class);
        String json = "{\"whatever\" : null}";
        try(ModelWithPrimitiveBoolean model = mapper.readObject(jsonToFile(tempDir, json))) {
            assertNotNull(model);
            assertFalse(model.isWhatever());
        }
    }

    @Test
    void testReadObjectWithBooleanWrapper(@TempDir Path tempDir) throws IOException, LarJsonException {
        LarJsonTypedMapper<ModelWithBooleanWrapper> mapper = new LarJsonTypedMapper<>(ModelWithBooleanWrapper.class);
        String json = "{\"whatever\" : false}";
        try(ModelWithBooleanWrapper model = mapper.readObject(jsonToFile(tempDir, json))) {
            assertNotNull(model);
            assertFalse(model.isWhatever());
        }
    }

    @Test
    void testReadListOfBoolean(@TempDir Path tempDir) throws IOException, LarJsonException {
        LarJsonTypedMapper<ModelWithBooleanList> mapper = new LarJsonTypedMapper<>(ModelWithBooleanList.class);
        String json = "{\"whatever\" : [true, false, null]}";
        try(ModelWithBooleanList model = mapper.readObject(jsonToFile(tempDir, json))) {
            assertNotNull(model);
            assertNotNull(model.getWhatever());
            assertEquals(3, model.getWhatever().size());
            assertTrue(model.getWhatever().get(0));
            assertFalse(model.getWhatever().get(1));
            assertNull(model.getWhatever().get(2));
        }
    }

    @Test
    void testReadObjectWithPrimitiveInt(@TempDir Path tempDir) throws IOException, LarJsonException {
        LarJsonTypedMapper<ModelWithPrimitiveInt> mapper = new LarJsonTypedMapper<>(ModelWithPrimitiveInt.class);
        String json = "{\"whatever\" : 13}";
        try(ModelWithPrimitiveInt model = mapper.readObject(jsonToFile(tempDir, json))) {
            assertNotNull(model);
            assertEquals(13, model.getWhatever());
        }
    }

    @Test
    void testReadObjectWithPrimitiveIntNull(@TempDir Path tempDir) throws IOException, LarJsonException {
        LarJsonTypedMapper<ModelWithPrimitiveInt> mapper = new LarJsonTypedMapper<>(ModelWithPrimitiveInt.class);
        String json = "{\"whatever\" : null}";
        try(ModelWithPrimitiveInt model = mapper.readObject(jsonToFile(tempDir, json))) {
            assertNotNull(model);
            assertEquals(0, model.getWhatever());
        }
    }

    @Test
    void testReadObjectWithIntegerWrapper(@TempDir Path tempDir) throws IOException, LarJsonException {
        LarJsonTypedMapper<ModelWithIntegerWrapper> mapper = new LarJsonTypedMapper<>(ModelWithIntegerWrapper.class);
        String json = "{\"whatever\" : -27}";
        try(ModelWithIntegerWrapper model = mapper.readObject(jsonToFile(tempDir, json))) {
            assertNotNull(model);
            assertEquals(-27, model.getWhatever());
        }
    }

    @Test
    void testReadListOfInteger(@TempDir Path tempDir) throws IOException, LarJsonException {
        LarJsonTypedMapper<ModelWithIntegerList> mapper = new LarJsonTypedMapper<>(ModelWithIntegerList.class);
        String json = "{\"whatever\" : [12, -785.0, null, 0, " + Integer.MIN_VALUE + ", " + Integer.MAX_VALUE + "]}";
        try(ModelWithIntegerList model = mapper.readObject(jsonToFile(tempDir, json))) {
            assertNotNull(model);
            assertNotNull(model.getWhatever());
            assertEquals(6, model.getWhatever().size());
            assertEquals(12, model.getWhatever().get(0));
            assertEquals(-785, model.getWhatever().get(1));
            assertEquals(null, model.getWhatever().get(2));
            assertEquals(0, model.getWhatever().get(3));
            assertEquals(Integer.MIN_VALUE, model.getWhatever().get(4));
            assertEquals(Integer.MAX_VALUE, model.getWhatever().get(5));
        }
    }

    @Test
    void testReadObjectWithPrimitiveLong(@TempDir Path tempDir) throws IOException, LarJsonException {
        LarJsonTypedMapper<ModelWithPrimitiveLong> mapper = new LarJsonTypedMapper<>(ModelWithPrimitiveLong.class);
        String json = "{\"whatever\" : 13}";
        try(ModelWithPrimitiveLong model = mapper.readObject(jsonToFile(tempDir, json))) {
            assertNotNull(model);
            assertEquals(13L, model.getWhatever());
        }
    }

    @Test
    void testReadObjectWithPrimitiveLongNull(@TempDir Path tempDir) throws IOException, LarJsonException {
        LarJsonTypedMapper<ModelWithPrimitiveLong> mapper = new LarJsonTypedMapper<>(ModelWithPrimitiveLong.class);
        String json = "{\"whatever\" : null}";
        try(ModelWithPrimitiveLong model = mapper.readObject(jsonToFile(tempDir, json))) {
            assertNotNull(model);
            assertEquals(0L, model.getWhatever());
        }
    }

    @Test
    void testReadObjectWithLongWrapper(@TempDir Path tempDir) throws IOException, LarJsonException {
        LarJsonTypedMapper<ModelWithLongWrapper> mapper = new LarJsonTypedMapper<>(ModelWithLongWrapper.class);
        String json = "{\"whatever\" : -27}";
        try(ModelWithLongWrapper model = mapper.readObject(jsonToFile(tempDir, json))) {
            assertNotNull(model);
            assertEquals(-27L, model.getWhatever());
        }
    }

    @Test
    void testReadListOfLong(@TempDir Path tempDir) throws IOException, LarJsonException {
        LarJsonTypedMapper<ModelWithLongList> mapper = new LarJsonTypedMapper<>(ModelWithLongList.class);
        String json = "{\"whatever\" : [12, -785.0, null, 0, " + Long.MIN_VALUE + ", " + Long.MAX_VALUE + "]}";
        try(ModelWithLongList model = mapper.readObject(jsonToFile(tempDir, json))) {
            assertNotNull(model);
            assertNotNull(model.getWhatever());
            assertEquals(6, model.getWhatever().size());
            assertEquals(12L, model.getWhatever().get(0));
            assertEquals(-785L, model.getWhatever().get(1));
            assertEquals(null, model.getWhatever().get(2));
            assertEquals(0L, model.getWhatever().get(3));
            assertEquals(Long.MIN_VALUE, model.getWhatever().get(4));
            assertEquals(Long.MAX_VALUE, model.getWhatever().get(5));
        }
    }

    @Test
    void testReadObjectWithPrimitiveShort(@TempDir Path tempDir) throws IOException, LarJsonException {
        LarJsonTypedMapper<ModelWithPrimitiveShort> mapper = new LarJsonTypedMapper<>(ModelWithPrimitiveShort.class);
        String json = "{\"whatever\" : 13}";
        try(ModelWithPrimitiveShort model = mapper.readObject(jsonToFile(tempDir, json))) {
            assertNotNull(model);
            assertEquals((short) 13, model.getWhatever());
        }
    }

    @Test
    void testReadObjectWithPrimitiveShortNull(@TempDir Path tempDir) throws IOException, LarJsonException {
        LarJsonTypedMapper<ModelWithPrimitiveShort> mapper = new LarJsonTypedMapper<>(ModelWithPrimitiveShort.class);
        String json = "{\"whatever\" : null}";
        try(ModelWithPrimitiveShort model = mapper.readObject(jsonToFile(tempDir, json))) {
            assertNotNull(model);
            assertEquals((short) 0, model.getWhatever());
        }
    }

    @Test
    void testReadObjectWithShortWrapper(@TempDir Path tempDir) throws IOException, LarJsonException {
        LarJsonTypedMapper<ModelWithShortWrapper> mapper = new LarJsonTypedMapper<>(ModelWithShortWrapper.class);
        String json = "{\"whatever\" : -27}";
        try(ModelWithShortWrapper model = mapper.readObject(jsonToFile(tempDir, json))) {
            assertNotNull(model);
            assertEquals((short) -27, model.getWhatever());
        }
    }

    @Test
    void testReadListOfShort(@TempDir Path tempDir) throws IOException, LarJsonException {
        LarJsonTypedMapper<ModelWithShortList> mapper = new LarJsonTypedMapper<>(ModelWithShortList.class);
        String json = "{\"whatever\" : [12, -785.0, null, 0, " + Short.MIN_VALUE + ", " + Short.MAX_VALUE + "]}";
        try(ModelWithShortList model = mapper.readObject(jsonToFile(tempDir, json))) {
            assertNotNull(model);
            assertNotNull(model.getWhatever());
            assertEquals(6, model.getWhatever().size());
            assertEquals((short) 12, model.getWhatever().get(0));
            assertEquals((short) -785, model.getWhatever().get(1));
            assertEquals(null, model.getWhatever().get(2));
            assertEquals((short) 0, model.getWhatever().get(3));
            assertEquals(Short.MIN_VALUE, model.getWhatever().get(4));
            assertEquals(Short.MAX_VALUE, model.getWhatever().get(5));
        }
    }

    @Test
    void testReadObjectWithPrimitiveByte(@TempDir Path tempDir) throws IOException, LarJsonException {
        LarJsonTypedMapper<ModelWithPrimitiveByte> mapper = new LarJsonTypedMapper<>(ModelWithPrimitiveByte.class);
        String json = "{\"whatever\" : 13}";
        try(ModelWithPrimitiveByte model = mapper.readObject(jsonToFile(tempDir, json))) {
            assertNotNull(model);
            assertEquals((byte) 13, model.getWhatever());
        }
    }

    @Test
    void testReadObjectWithPrimitiveByteNull(@TempDir Path tempDir) throws IOException, LarJsonException {
        LarJsonTypedMapper<ModelWithPrimitiveByte> mapper = new LarJsonTypedMapper<>(ModelWithPrimitiveByte.class);
        String json = "{\"whatever\" : null}";
        try(ModelWithPrimitiveByte model = mapper.readObject(jsonToFile(tempDir, json))) {
            assertNotNull(model);
            assertEquals((byte) 0, model.getWhatever());
        }
    }

    @Test
    void testReadObjectWithByteWrapper(@TempDir Path tempDir) throws IOException, LarJsonException {
        LarJsonTypedMapper<ModelWithByteWrapper> mapper = new LarJsonTypedMapper<>(ModelWithByteWrapper.class);
        String json = "{\"whatever\" : -27}";
        try(ModelWithByteWrapper model = mapper.readObject(jsonToFile(tempDir, json))) {
            assertNotNull(model);
            assertEquals((byte) -27, model.getWhatever());
        }
    }

    @Test
    void testReadListOfByte(@TempDir Path tempDir) throws IOException, LarJsonException {
        LarJsonTypedMapper<ModelWithByteList> mapper = new LarJsonTypedMapper<>(ModelWithByteList.class);
        String json = "{\"whatever\" : [12, -78.0, null, 0, " + Byte.MIN_VALUE + ", " + Byte.MAX_VALUE + "]}";
        try(ModelWithByteList model = mapper.readObject(jsonToFile(tempDir, json))) {
            assertNotNull(model);
            assertNotNull(model.getWhatever());
            assertEquals(6, model.getWhatever().size());
            assertEquals((byte) 12, model.getWhatever().get(0));
            assertEquals((byte) -78, model.getWhatever().get(1));
            assertEquals(null, model.getWhatever().get(2));
            assertEquals((byte) 0, model.getWhatever().get(3));
            assertEquals(Byte.MIN_VALUE, model.getWhatever().get(4));
            assertEquals(Byte.MAX_VALUE, model.getWhatever().get(5));
        }
    }

    @Test
    void testReadObjectWithPrimitiveChar(@TempDir Path tempDir) throws IOException, LarJsonException {
        LarJsonTypedMapper<ModelWithPrimitiveChar> mapper = new LarJsonTypedMapper<>(ModelWithPrimitiveChar.class);
        String json = "{\"whatever\" : \"q\"}";
        try(ModelWithPrimitiveChar model = mapper.readObject(jsonToFile(tempDir, json))) {
            assertNotNull(model);
            assertEquals('q', model.getWhatever());
        }
    }

    @Test
    void testReadObjectWithPrimitiveCharNull(@TempDir Path tempDir) throws IOException, LarJsonException {
        LarJsonTypedMapper<ModelWithPrimitiveChar> mapper = new LarJsonTypedMapper<>(ModelWithPrimitiveChar.class);
        String json = "{\"whatever\" : null}";
        try(ModelWithPrimitiveChar model = mapper.readObject(jsonToFile(tempDir, json))) {
            assertNotNull(model);
            assertEquals((char) 0, model.getWhatever());
        }
    }

    @Test
    void testReadObjectWithCharacterWrapper(@TempDir Path tempDir) throws IOException, LarJsonException {
        LarJsonTypedMapper<ModelWithCharacterWrapper> mapper = new LarJsonTypedMapper<>(ModelWithCharacterWrapper.class);
        String json = "{\"whatever\" : \"@\"}";
        try(ModelWithCharacterWrapper model = mapper.readObject(jsonToFile(tempDir, json))) {
            assertNotNull(model);
            assertEquals('@', model.getWhatever());
        }
    }

    @Test
    void testReadListOfCharacter(@TempDir Path tempDir) throws IOException, LarJsonException {
        LarJsonTypedMapper<ModelWithCharacterList> mapper = new LarJsonTypedMapper<>(ModelWithCharacterList.class);
        String json = "{\"whatever\" : [\"B\", null, \"\uABCD\", \"\\uABCE\", \"\n\", \"\\t\"]}";
        try(ModelWithCharacterList model = mapper.readObject(jsonToFile(tempDir, json))) {
            assertNotNull(model);
            assertNotNull(model.getWhatever());
            assertEquals(6, model.getWhatever().size());
            assertEquals('B', model.getWhatever().get(0));
            assertEquals(null, model.getWhatever().get(1));
            assertEquals('\uABCD', model.getWhatever().get(2));
            assertEquals('\uABCE', model.getWhatever().get(3));
            assertEquals('\n', model.getWhatever().get(4));
            assertEquals('\t', model.getWhatever().get(5));
        }
    }

    @Test
    void testReadObjectWithPrimitiveDouble(@TempDir Path tempDir) throws IOException, LarJsonException {
        LarJsonTypedMapper<ModelWithPrimitiveDouble> mapper = new LarJsonTypedMapper<>(ModelWithPrimitiveDouble.class);
        String json = "{\"whatever\" : 13.45704}";
        try(ModelWithPrimitiveDouble model = mapper.readObject(jsonToFile(tempDir, json))) {
            assertNotNull(model);
            assertEquals(13.45704, model.getWhatever());
        }
    }

    @Test
    void testReadObjectWithPrimitiveDoubleNull(@TempDir Path tempDir) throws IOException, LarJsonException {
        LarJsonTypedMapper<ModelWithPrimitiveDouble> mapper = new LarJsonTypedMapper<>(ModelWithPrimitiveDouble.class);
        String json = "{\"whatever\" : null}";
        try(ModelWithPrimitiveDouble model = mapper.readObject(jsonToFile(tempDir, json))) {
            assertNotNull(model);
            assertEquals(0.0, model.getWhatever());
        }
    }

    @Test
    void testReadObjectWithDoubleWrapper(@TempDir Path tempDir) throws IOException, LarJsonException {
        LarJsonTypedMapper<ModelWithDoubleWrapper> mapper = new LarJsonTypedMapper<>(ModelWithDoubleWrapper.class);
        String json = "{\"whatever\" : -0.0427667}";
        try(ModelWithDoubleWrapper model = mapper.readObject(jsonToFile(tempDir, json))) {
            assertNotNull(model);
            assertEquals(-0.0427667, model.getWhatever());
        }
    }

    @Test
    void testReadListOfDouble(@TempDir Path tempDir) throws IOException, LarJsonException {
        LarJsonTypedMapper<ModelWithDoubleList> mapper = new LarJsonTypedMapper<>(ModelWithDoubleList.class);
        String json = "{\"whatever\" : [12, 0.0, null, 513e+10, -12.789E-75, " + Double.MAX_VALUE + ", " +
                Double.MIN_VALUE + "]}";
        try(ModelWithDoubleList model = mapper.readObject(jsonToFile(tempDir, json))) {
            assertNotNull(model);
            assertNotNull(model.getWhatever());
            assertEquals(7, model.getWhatever().size());
            assertEquals(12.0, model.getWhatever().get(0));
            assertEquals(0.0, model.getWhatever().get(1));
            assertEquals(null, model.getWhatever().get(2));
            assertEquals(513e+10, model.getWhatever().get(3));
            assertEquals(-12.789E-75, model.getWhatever().get(4));
            assertEquals(Double.MAX_VALUE, model.getWhatever().get(5));
            assertEquals(Double.MIN_VALUE, model.getWhatever().get(6));
        }
    }

    @Test
    void testLenientReadListOfDouble(@TempDir Path tempDir) throws IOException, LarJsonException {
        LarJsonTypedMapper<ModelWithDoubleList> mapper = new LarJsonTypedMapper<>(ModelWithDoubleList.class,
                new LarJsonTypedReadConfiguration.Builder().setLenient(true).build());
        String json = "{\"whatever\" : [" + Double.NaN + ", " + Double.NEGATIVE_INFINITY + ", " +
                Double.POSITIVE_INFINITY + "]}";
        try(ModelWithDoubleList model = mapper.readObject(jsonToFile(tempDir, json))) {
            assertNotNull(model);
            assertNotNull(model.getWhatever());
            assertEquals(3, model.getWhatever().size());
            assertEquals(Double.NaN, model.getWhatever().get(0));
            assertEquals(Double.NEGATIVE_INFINITY, model.getWhatever().get(1));
            assertEquals(Double.POSITIVE_INFINITY, model.getWhatever().get(2));
        }
    }

    @Test
    void testReadObjectWithPrimitiveFloat(@TempDir Path tempDir) throws IOException, LarJsonException {
        LarJsonTypedMapper<ModelWithPrimitiveFloat> mapper = new LarJsonTypedMapper<>(ModelWithPrimitiveFloat.class);
        String json = "{\"whatever\" : 13.45704}";
        try(ModelWithPrimitiveFloat model = mapper.readObject(jsonToFile(tempDir, json))) {
            assertNotNull(model);
            assertEquals(13.45704f, model.getWhatever());
        }
    }

    @Test
    void testReadObjectWithPrimitiveFloatNull(@TempDir Path tempDir) throws IOException, LarJsonException {
        LarJsonTypedMapper<ModelWithPrimitiveFloat> mapper = new LarJsonTypedMapper<>(ModelWithPrimitiveFloat.class);
        String json = "{\"whatever\" : null}";
        try(ModelWithPrimitiveFloat model = mapper.readObject(jsonToFile(tempDir, json))) {
            assertNotNull(model);
            assertEquals(0.0f, model.getWhatever());
        }
    }

    @Test
    void testReadObjectWithFloatWrapper(@TempDir Path tempDir) throws IOException, LarJsonException {
        LarJsonTypedMapper<ModelWithFloatWrapper> mapper = new LarJsonTypedMapper<>(ModelWithFloatWrapper.class);
        String json = "{\"whatever\" : -0.0427667}";
        try(ModelWithFloatWrapper model = mapper.readObject(jsonToFile(tempDir, json))) {
            assertNotNull(model);
            assertEquals(-0.0427667f, model.getWhatever());
        }
    }

    @Test
    void testReadListOfFloat(@TempDir Path tempDir) throws IOException, LarJsonException {
        LarJsonTypedMapper<ModelWithFloatList> mapper = new LarJsonTypedMapper<>(ModelWithFloatList.class);
        String json = "{\"whatever\" : [12, 0.0, null, 513e+10, -12.789E-33, " + Float.MAX_VALUE + ", " +
                Float.MIN_VALUE + "]}";
        try(ModelWithFloatList model = mapper.readObject(jsonToFile(tempDir, json))) {
            assertNotNull(model);
            assertNotNull(model.getWhatever());
            assertEquals(7, model.getWhatever().size());
            assertEquals(12.0f, model.getWhatever().get(0));
            assertEquals(0.0f, model.getWhatever().get(1));
            assertEquals(null, model.getWhatever().get(2));
            assertEquals(513e+10f, model.getWhatever().get(3));
            assertEquals(-12.789E-33f, model.getWhatever().get(4));
            assertEquals(Float.MAX_VALUE, model.getWhatever().get(5));
            assertEquals(Float.MIN_VALUE, model.getWhatever().get(6));
        }
    }

    @Test
    void testLenientReadListOfFloat(@TempDir Path tempDir) throws IOException, LarJsonException {
        LarJsonTypedMapper<ModelWithFloatList> mapper = new LarJsonTypedMapper<>(ModelWithFloatList.class,
                new LarJsonTypedReadConfiguration.Builder().setLenient(true).build());
        String json = "{\"whatever\" : [" + Float.NaN + ", " + Float.NEGATIVE_INFINITY + ", " +
                Float.POSITIVE_INFINITY + "]}";
        try(ModelWithFloatList model = mapper.readObject(jsonToFile(tempDir, json))) {
            assertNotNull(model);
            assertNotNull(model.getWhatever());
            assertEquals(3, model.getWhatever().size());
            assertEquals(Float.NaN, model.getWhatever().get(0));
            assertEquals(Float.NEGATIVE_INFINITY, model.getWhatever().get(1));
            assertEquals(Float.POSITIVE_INFINITY, model.getWhatever().get(2));
        }
    }

    @Test
    void testReadObjectWithString(@TempDir Path tempDir) throws IOException, LarJsonException {
        LarJsonTypedMapper<ModelWithString> mapper = new LarJsonTypedMapper<>(ModelWithString.class);
        String json = "{\"whatever\" : \" hell0-th€re !\"}";
        try(ModelWithString model = mapper.readObject(jsonToFile(tempDir, json))) {
            assertNotNull(model);
            assertEquals(" hell0-th€re !", model.getWhatever());
        }
    }

    @Test
    void testReadListOfString(@TempDir Path tempDir) throws IOException, LarJsonException {
        LarJsonTypedMapper<ModelWithStringList> mapper = new LarJsonTypedMapper<>(ModelWithStringList.class);
        String json = "{\"whatever\" : [\"hello\", \"Yes\uABCD\", null, \"\", \"\\uBBDD \t\\n\\b\", \"null\"]}";
        try(ModelWithStringList model = mapper.readObject(jsonToFile(tempDir, json))) {
            assertNotNull(model);
            assertNotNull(model.getWhatever());
            assertEquals(6, model.getWhatever().size());
            assertEquals("hello", model.getWhatever().get(0));
            assertEquals("Yes\uABCD", model.getWhatever().get(1));
            assertEquals(null, model.getWhatever().get(2));
            assertEquals("", model.getWhatever().get(3));
            assertEquals("\uBBDD \t\n\b", model.getWhatever().get(4));
            assertEquals("null", model.getWhatever().get(5));
        }
    }

    @Test
    void testReadLarJsonListOfString(@TempDir Path tempDir) throws IOException, LarJsonException {
        LarJsonTypedMapper<ModelWithStringLarJsonList> mapper = new LarJsonTypedMapper<>(ModelWithStringLarJsonList.class);
        String json = "{\"whatever\" : [\"hello\", \"Yes\uABCD\", null, \"\", \"\\uBBDD \t\\n\\b\", \"null\"]}";
        try(ModelWithStringLarJsonList model = mapper.readObject(jsonToFile(tempDir, json))) {
            assertNotNull(model);
            assertNotNull(model.getWhatever());
            assertEquals(6, model.getWhatever().size());
            assertEquals("hello", model.getWhatever().get(0));
            assertEquals("Yes\uABCD", model.getWhatever().get(1));
            assertEquals(null, model.getWhatever().get(2));
            assertEquals("", model.getWhatever().get(3));
            assertEquals("\uBBDD \t\n\b", model.getWhatever().get(4));
            assertEquals("null", model.getWhatever().get(5));
        }
    }

    @Test
    void testReadCollectionOfString(@TempDir Path tempDir) throws IOException, LarJsonException {
        LarJsonTypedMapper<ModelWithStringCollection> mapper = new LarJsonTypedMapper<>(ModelWithStringCollection.class);
        String json = "{\"whatever\" : [\"hello\", \"Yes\uABCD\", null, \"\", \"\\uBBDD \t\\n\\b\", \"null\"]}";
        try(ModelWithStringCollection model = mapper.readObject(jsonToFile(tempDir, json))) {
            assertNotNull(model);
            assertNotNull(model.getWhatever());
            assertEquals(6, model.getWhatever().size());
            Iterator<String> iterator = Arrays.asList("hello", "Yes\uABCD", null, "", "\uBBDD \t\n\b", "null").iterator();
            for(String s : model.getWhatever()) {
                assertEquals(iterator.next(), s);
            }
            assertFalse(iterator.hasNext());
        }
    }

    @Test
    void testReadIterableOfString(@TempDir Path tempDir) throws IOException, LarJsonException {
        LarJsonTypedMapper<ModelWithStringIterable> mapper = new LarJsonTypedMapper<>(ModelWithStringIterable.class);
        String json = "{\"whatever\" : [\"hello\", \"Yes\uABCD\", null, \"\", \"\\uBBDD \t\\n\\b\", \"null\"]}";
        try(ModelWithStringIterable model = mapper.readObject(jsonToFile(tempDir, json))) {
            assertNotNull(model);
            assertNotNull(model.getWhatever());
            Iterator<String> iterator = Arrays.asList("hello", "Yes\uABCD", null, "", "\uBBDD \t\n\b", "null").iterator();
            for(String s : model.getWhatever()) {
                assertEquals(iterator.next(), s);
            }
            assertFalse(iterator.hasNext());
        }
    }

    @Test
    void testReadObjectWithCharSequence(@TempDir Path tempDir) throws IOException, LarJsonException {
        LarJsonTypedMapper<ModelWithCharSequence> mapper = new LarJsonTypedMapper<>(ModelWithCharSequence.class);
        String json = "{\"whatever\" : \" hell0-th€re !\"}";
        try(ModelWithCharSequence model = mapper.readObject(jsonToFile(tempDir, json))) {
            assertNotNull(model);
            assertEquals(" hell0-th€re !", model.getWhatever().toString());
        }
    }

    @Test
    void testReadListOfCharSequence(@TempDir Path tempDir) throws IOException, LarJsonException {
        LarJsonTypedMapper<ModelWithCharSequenceList> mapper = new LarJsonTypedMapper<>(ModelWithCharSequenceList.class);
        String json = "{\"whatever\" : [\"hello\", \"Yes\uABCD\", null, \"\", \"\\uBBDD \t\\n\\b\", \"null\"]}";
        try(ModelWithCharSequenceList model = mapper.readObject(jsonToFile(tempDir, json))) {
            assertNotNull(model);
            assertNotNull(model.getWhatever());
            assertEquals(6, model.getWhatever().size());
            assertEquals("hello", model.getWhatever().get(0).toString());
            assertEquals("Yes\uABCD", model.getWhatever().get(1).toString());
            assertEquals(null, model.getWhatever().get(2));
            assertEquals("", model.getWhatever().get(3).toString());
            assertEquals("\uBBDD \t\n\b", model.getWhatever().get(4).toString());
            assertEquals("null", model.getWhatever().get(5).toString());
        }
    }

    @Test
    void testReadObjectWithBigInteger(@TempDir Path tempDir) throws IOException, LarJsonException {
        LarJsonTypedMapper<ModelWithBigInteger> mapper = new LarJsonTypedMapper<>(ModelWithBigInteger.class);
        BigInteger bigInteger = new BigInteger(Long.MIN_VALUE + "" + Long.MAX_VALUE);
        String json = "{\"whatever\" : " + bigInteger + "}";
        try(ModelWithBigInteger model = mapper.readObject(jsonToFile(tempDir, json))) {
            assertNotNull(model);
            assertEquals(bigInteger, model.getWhatever());
        }
    }

    @Test
    void testReadListOfBigInteger(@TempDir Path tempDir) throws IOException, LarJsonException {
        LarJsonTypedMapper<ModelWithBigIntegerList> mapper = new LarJsonTypedMapper<>(ModelWithBigIntegerList.class);
        BigInteger bigInteger = new BigInteger(Long.MIN_VALUE + "" + Long.MAX_VALUE);
        String json =
                "{\"whatever\" : [12, 0.0, null, -182, " + bigInteger + ", " + bigInteger + ".0]}";
        try(ModelWithBigIntegerList model = mapper.readObject(jsonToFile(tempDir, json))) {
            assertNotNull(model);
            assertNotNull(model.getWhatever());
            assertEquals(6, model.getWhatever().size());
            assertEquals(new BigInteger("12"), model.getWhatever().get(0));
            assertEquals(new BigInteger("0"), model.getWhatever().get(1));
            assertEquals(null, model.getWhatever().get(2));
            assertEquals(new BigInteger("-182"), model.getWhatever().get(3));
            assertEquals(bigInteger, model.getWhatever().get(4));
            assertEquals(bigInteger, model.getWhatever().get(5));
        }
    }

    @Test
    void testReadObjectWithBigDecimal(@TempDir Path tempDir) throws IOException, LarJsonException {
        LarJsonTypedMapper<ModelWithBigDecimal> mapper = new LarJsonTypedMapper<>(ModelWithBigDecimal.class);
        BigDecimal bigDecimal = new BigDecimal(Long.MIN_VALUE + "" + Long.MAX_VALUE + "." + Integer.MAX_VALUE);
        String json = "{\"whatever\" : " + bigDecimal + "}";
        try(ModelWithBigDecimal model = mapper.readObject(jsonToFile(tempDir, json))) {
            assertNotNull(model);
            assertEquals(bigDecimal, model.getWhatever());
        }
    }

    @Test
    void testReadListOfBigDecimal(@TempDir Path tempDir) throws IOException, LarJsonException {
        LarJsonTypedMapper<ModelWithBigDecimalList> mapper = new LarJsonTypedMapper<>(ModelWithBigDecimalList.class);
        BigDecimal bigDecimal1 = new BigDecimal(Long.MIN_VALUE + "" + Long.MAX_VALUE + "." + Integer.MAX_VALUE);
        BigDecimal bigDecimal2 = new BigDecimal("-0.000000000000000000000000000000000000000000000000000000000000"
                + Long.MAX_VALUE);
        String json =
                "{\"whatever\" : [12, 0.0, null, -182.05, " + bigDecimal1 + ", " + bigDecimal2 + "]}";
        try(ModelWithBigDecimalList model = mapper.readObject(jsonToFile(tempDir, json))) {
            assertNotNull(model);
            assertNotNull(model.getWhatever());
            assertEquals(6, model.getWhatever().size());
            assertEquals(new BigDecimal("12"), model.getWhatever().get(0));
            assertEquals(new BigDecimal("0.0"), model.getWhatever().get(1));
            assertEquals(null, model.getWhatever().get(2));
            assertEquals(new BigDecimal("-182.05"), model.getWhatever().get(3));
            assertEquals(bigDecimal1, model.getWhatever().get(4));
            assertEquals(bigDecimal2, model.getWhatever().get(5));
        }
    }

    @Test
    void testReadObjectWithNumber(@TempDir Path tempDir) throws IOException, LarJsonException {
        LarJsonTypedMapper<ModelWithNumber> mapper = new LarJsonTypedMapper<>(ModelWithNumber.class);
        BigDecimal bigDecimal = new BigDecimal(Long.MIN_VALUE + "" + Long.MAX_VALUE + "." + Integer.MAX_VALUE);
        String json = "{\"whatever\" : " + bigDecimal + "}";
        try(ModelWithNumber model = mapper.readObject(jsonToFile(tempDir, json))) {
            assertNotNull(model);
            assertEquals(bigDecimal, model.getWhatever());
        }
    }

    @Test
    void testReadListOfNumber(@TempDir Path tempDir) throws IOException, LarJsonException {
        LarJsonTypedMapper<ModelWithNumberList> mapper = new LarJsonTypedMapper<>(ModelWithNumberList.class);
        BigDecimal bigDecimal = new BigDecimal("-0.000000000000000000000000000000000000000000000000000000000000"
                + Long.MAX_VALUE);
        BigInteger bigInteger = new BigInteger(Long.MIN_VALUE + "" + Long.MAX_VALUE);
        String json = "{\"whatever\" : [12, 0.0, null, -182.05, " + Long.MIN_VALUE +  ", " + Long.MAX_VALUE + ", " +
                bigDecimal + ", " + Double.MIN_VALUE + ", " + bigInteger + ", " + Double.MAX_VALUE + "]}";
        try(ModelWithNumberList model = mapper.readObject(jsonToFile(tempDir, json))) {
            assertNotNull(model);
            assertNotNull(model.getWhatever());
            assertEquals(10, model.getWhatever().size());
            assertEquals(12L, model.getWhatever().get(0));
            assertEquals(0.0, model.getWhatever().get(1));
            assertEquals(null, model.getWhatever().get(2));
            assertEquals(-182.05, model.getWhatever().get(3));
            assertEquals(Long.MIN_VALUE, model.getWhatever().get(4));
            assertEquals(Long.MAX_VALUE, model.getWhatever().get(5));
            assertEquals(bigDecimal, model.getWhatever().get(6));
            assertEquals(Double.MIN_VALUE, model.getWhatever().get(7));
            assertEquals(bigInteger, model.getWhatever().get(8));
            assertEquals(Double.MAX_VALUE, model.getWhatever().get(9));
        }
    }

    @Test
    void testReadObjectWithEnum(@TempDir Path tempDir) throws IOException, LarJsonException {
        LarJsonTypedMapper<ModelWithEnum> mapper = new LarJsonTypedMapper<>(ModelWithEnum.class);
        String json = "{\"whatever\" : \"" + TestEnum.HOLA + "\"}";
        try(ModelWithEnum model = mapper.readObject(jsonToFile(tempDir, json))) {
            assertNotNull(model);
            assertEquals(TestEnum.HOLA, model.getWhatever());
        }
    }

    @Test
    void testReadListOfEnum(@TempDir Path tempDir) throws IOException, LarJsonException {
        LarJsonTypedMapper<ModelWithEnumList> mapper = new LarJsonTypedMapper<>(ModelWithEnumList.class);
        String json = "{\"whatever\" : [null, \"HELLO\", \"SALUT\"]}";
        try(ModelWithEnumList model = mapper.readObject(jsonToFile(tempDir, json))) {
            assertNotNull(model);
            assertNotNull(model.getWhatever());
            assertEquals(3, model.getWhatever().size());
            assertEquals(null, model.getWhatever().get(0));
            assertEquals(TestEnum.HELLO, model.getWhatever().get(1));
            assertEquals(TestEnum.SALUT, model.getWhatever().get(2));
        }
    }

    @Test
    void testReadObjectWithConverted(@TempDir Path tempDir) throws IOException, LarJsonException {
        LarJsonTypedMapper<ModelWithEnum> mapper = new LarJsonTypedMapper<>(ModelWithEnum.class,
                new LarJsonTypedReadConfiguration.Builder().setStringValueConverter(
                        TestEnum.class, new TestEnumConverter()).build());
        String json = "{\"whatever\" : null}";
        try(ModelWithEnum model = mapper.readObject(jsonToFile(tempDir, json))) {
            assertNotNull(model);
            assertEquals(TestEnum.SALUT, model.getWhatever());
        }
    }

    @Test
    void testReadListOfConverted(@TempDir Path tempDir) throws IOException, LarJsonException {
        LarJsonTypedMapper<ModelWithEnumList> mapper = new LarJsonTypedMapper<>(ModelWithEnumList.class,
                new LarJsonTypedReadConfiguration.Builder().setStringValueConverter(
                        TestEnum.class, new TestEnumConverter()).build());
        String json = "{\"whatever\" : [null, \"   \t\", \"EN\"]}";
        try(ModelWithEnumList model = mapper.readObject(jsonToFile(tempDir, json))) {
            assertNotNull(model);
            assertNotNull(model.getWhatever());
            assertEquals(3, model.getWhatever().size());
            assertEquals(TestEnum.SALUT, model.getWhatever().get(0));
            assertEquals(TestEnum.HOLA, model.getWhatever().get(1));
            assertEquals(TestEnum.HELLO, model.getWhatever().get(2));
        }
    }

    @Test
    void testReadObjectWithDefaultFormatDate(@TempDir Path tempDir) throws IOException, LarJsonException,
            ParseException {
        LarJsonTypedMapper<ModelWithDate> mapper = new LarJsonTypedMapper<>(ModelWithDate.class);
        SimpleDateFormat dateFormat = new SimpleDateFormat();
        String date = dateFormat.format(new Date());
        String json = "{\"whatever\" : \"" + date + "\"}";
        try(ModelWithDate model = mapper.readObject(jsonToFile(tempDir, json))) {
            assertNotNull(model);
            assertEquals(dateFormat.parse(date), model.getWhatever());
        }
    }

    @Test
    void testReadObjectWithCustomFormatDate(@TempDir Path tempDir) throws IOException, LarJsonException,
            ParseException {
        String format = "yyyy.MM.dd G 'at' HH:mm:ss z";
        LarJsonTypedMapper<ModelWithDate> mapper = new LarJsonTypedMapper<>(ModelWithDate.class,
                new LarJsonTypedReadConfiguration.Builder().setDateFormat(format).build());
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        String date = dateFormat.format(new Date());
        String json = "{\"whatever\" : \"" + date + "\"}";
        try(ModelWithDate model = mapper.readObject(jsonToFile(tempDir, json))) {
            assertNotNull(model);
            assertEquals(dateFormat.parse(date), model.getWhatever());
        }
    }

    @Test
    void testReadListOfDate(@TempDir Path tempDir) throws IOException, LarJsonException, ParseException {
        String format = "yyyy.MM.dd G 'at' HH:mm:ss z";
        LarJsonTypedMapper<ModelWithDateList> mapper = new LarJsonTypedMapper<>(ModelWithDateList.class,
                new LarJsonTypedReadConfiguration.Builder().setDateFormat(format).build());
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        String date = dateFormat.format(new Date());
        String json = "{\"whatever\" : [null, \"" + date + "\"]}";
        try(ModelWithDateList model = mapper.readObject(jsonToFile(tempDir, json))) {
            assertNotNull(model);
            assertNotNull(model.getWhatever());
            assertEquals(2, model.getWhatever().size());
            assertEquals(null, model.getWhatever().get(0));
            assertEquals(dateFormat.parse(date), model.getWhatever().get(1));
        }
    }

    @Test
    void testReadObjectWithDefaultFormatLocalDateTime(@TempDir Path tempDir) throws IOException, LarJsonException {
        LarJsonTypedMapper<ModelWithLocalDateTime> mapper = new LarJsonTypedMapper<>(ModelWithLocalDateTime.class);
        LocalDateTime now = LocalDateTime.now();
        String json = "{\"whatever\" : \"" + now.toString() + "\"}";
        try(ModelWithLocalDateTime model = mapper.readObject(jsonToFile(tempDir, json))) {
            assertNotNull(model);
            assertEquals(now, model.getWhatever());
        }
    }

    @Test
    void testReadObjectWithCustomFormatLocalDateTime(@TempDir Path tempDir) throws IOException, LarJsonException {
        String format = "yyyy.MM.dd G 'at' HH:mm:ss";
        LarJsonTypedMapper<ModelWithLocalDateTime> mapper = new LarJsonTypedMapper<>(ModelWithLocalDateTime.class,
                new LarJsonTypedReadConfiguration.Builder().setLocalDateTimeFormat(format).build());
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern(format);
        String date = LocalDateTime.now().format(dateFormat);
        String json = "{\"whatever\" : \"" + date + "\"}";
        try(ModelWithLocalDateTime model = mapper.readObject(jsonToFile(tempDir, json))) {
            assertNotNull(model);
            assertEquals(LocalDateTime.parse(date, dateFormat), model.getWhatever());
        }
    }

    @Test
    void testReadListOfLocalDateTime(@TempDir Path tempDir) throws IOException, LarJsonException {
        String format = "yyyy.MM.dd G 'at' HH:mm:ss";
        LarJsonTypedMapper<ModelWithLocalDateTimeList> mapper = new LarJsonTypedMapper<>(ModelWithLocalDateTimeList.class,
                new LarJsonTypedReadConfiguration.Builder().setLocalDateTimeFormat(format).build());
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern(format);
        String date = LocalDateTime.now().format(dateFormat);
        String json = "{\"whatever\" : [null, \"" + date + "\"]}";
        try(ModelWithLocalDateTimeList model = mapper.readObject(jsonToFile(tempDir, json))) {
            assertNotNull(model);
            assertNotNull(model.getWhatever());
            assertEquals(2, model.getWhatever().size());
            assertEquals(null, model.getWhatever().get(0));
            assertEquals(LocalDateTime.parse(date, dateFormat), model.getWhatever().get(1));
        }
    }

    @Test
    void testReadObjectWithDefaultFormatLocalDate(@TempDir Path tempDir) throws IOException, LarJsonException {
        LarJsonTypedMapper<ModelWithLocalDate> mapper = new LarJsonTypedMapper<>(ModelWithLocalDate.class);
        LocalDate now = LocalDate.now();
        String json = "{\"whatever\" : \"" + now.toString() + "\"}";
        try(ModelWithLocalDate model = mapper.readObject(jsonToFile(tempDir, json))) {
            assertNotNull(model);
            assertEquals(now, model.getWhatever());
        }
    }

    @Test
    void testReadObjectWithCustomFormatLocalDate(@TempDir Path tempDir) throws IOException, LarJsonException {
        String format = "yyyy.MM.dd G";
        LarJsonTypedMapper<ModelWithLocalDate> mapper = new LarJsonTypedMapper<>(ModelWithLocalDate.class,
                new LarJsonTypedReadConfiguration.Builder().setLocalDateFormat(format).build());
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern(format);
        String date = LocalDate.now().format(dateFormat);
        String json = "{\"whatever\" : \"" + date + "\"}";
        try(ModelWithLocalDate model = mapper.readObject(jsonToFile(tempDir, json))) {
            assertNotNull(model);
            assertEquals(LocalDate.parse(date, dateFormat), model.getWhatever());
        }
    }

    @Test
    void testReadListOfLocalDate(@TempDir Path tempDir) throws IOException, LarJsonException {
        String format = "yyyy.MM.dd G";
        LarJsonTypedMapper<ModelWithLocalDateList> mapper = new LarJsonTypedMapper<>(ModelWithLocalDateList.class,
                new LarJsonTypedReadConfiguration.Builder().setLocalDateFormat(format).build());
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern(format);
        String date = LocalDate.now().format(dateFormat);
        String json = "{\"whatever\" : [null, \"" + date + "\"]}";
        try(ModelWithLocalDateList model = mapper.readObject(jsonToFile(tempDir, json))) {
            assertNotNull(model);
            assertNotNull(model.getWhatever());
            assertEquals(2, model.getWhatever().size());
            assertEquals(null, model.getWhatever().get(0));
            assertEquals(LocalDate.parse(date, dateFormat), model.getWhatever().get(1));
        }
    }

    @Test
    void testReadObjectWithDefaultFormatLocalTime(@TempDir Path tempDir) throws IOException, LarJsonException {
        LarJsonTypedMapper<ModelWithLocalTime> mapper = new LarJsonTypedMapper<>(ModelWithLocalTime.class);
        LocalTime now = LocalTime.now();
        String json = "{\"whatever\" : \"" + now.toString() + "\"}";
        try(ModelWithLocalTime model = mapper.readObject(jsonToFile(tempDir, json))) {
            assertNotNull(model);
            assertEquals(now, model.getWhatever());
        }
    }

    @Test
    void testReadObjectWithCustomFormatLocalTime(@TempDir Path tempDir) throws IOException, LarJsonException {
        String format = "HH_mm_ss__SSS";
        LarJsonTypedMapper<ModelWithLocalTime> mapper = new LarJsonTypedMapper<>(ModelWithLocalTime.class,
                new LarJsonTypedReadConfiguration.Builder().setLocalTimeFormat(format).build());
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern(format);
        String date = LocalTime.now().format(dateFormat);
        String json = "{\"whatever\" : \"" + date + "\"}";
        try(ModelWithLocalTime model = mapper.readObject(jsonToFile(tempDir, json))) {
            assertNotNull(model);
            assertEquals(LocalTime.parse(date, dateFormat), model.getWhatever());
        }
    }

    @Test
    void testReadListOfLocalTime(@TempDir Path tempDir) throws IOException, LarJsonException {
        String format = "HH_mm_ss__SSS";
        LarJsonTypedMapper<ModelWithLocalTimeList> mapper = new LarJsonTypedMapper<>(ModelWithLocalTimeList.class,
                new LarJsonTypedReadConfiguration.Builder().setLocalTimeFormat(format).build());
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern(format);
        String date = LocalTime.now().format(dateFormat);
        String json = "{\"whatever\" : [null, \"" + date + "\"]}";
        try(ModelWithLocalTimeList model = mapper.readObject(jsonToFile(tempDir, json))) {
            assertNotNull(model);
            assertNotNull(model.getWhatever());
            assertEquals(2, model.getWhatever().size());
            assertEquals(null, model.getWhatever().get(0));
            assertEquals(LocalTime.parse(date, dateFormat), model.getWhatever().get(1));
        }
    }

    @Test
    void testReadObjectWithDefaultFormatZonedDateTime(@TempDir Path tempDir) throws IOException, LarJsonException {
        LarJsonTypedMapper<ModelWithZonedDateTime> mapper = new LarJsonTypedMapper<>(ModelWithZonedDateTime.class);
        ZonedDateTime now = ZonedDateTime.now();
        String json = "{\"whatever\" : \"" + now.toString() + "\"}";
        try(ModelWithZonedDateTime model = mapper.readObject(jsonToFile(tempDir, json))) {
            assertNotNull(model);
            assertEquals(now, model.getWhatever());
        }
    }

    @Test
    void testReadObjectWithCustomFormatZonedDateTime(@TempDir Path tempDir) throws IOException, LarJsonException {
        String format = "yyyy.MM.dd G 'at' HH:mm:ss Z";
        LarJsonTypedMapper<ModelWithZonedDateTime> mapper = new LarJsonTypedMapper<>(ModelWithZonedDateTime.class,
                new LarJsonTypedReadConfiguration.Builder().setZonedDateTimeFormat(format).build());
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern(format);
        String date = ZonedDateTime.now().format(dateFormat);
        String json = "{\"whatever\" : \"" + date + "\"}";
        try(ModelWithZonedDateTime model = mapper.readObject(jsonToFile(tempDir, json))) {
            assertNotNull(model);
            assertEquals(ZonedDateTime.parse(date, dateFormat), model.getWhatever());
        }
    }

    @Test
    void testReadListOfZonedDateTime(@TempDir Path tempDir) throws IOException, LarJsonException {
        String format = "yyyy.MM.dd G 'at' HH:mm:ss Z";
        LarJsonTypedMapper<ModelWithZonedDateTimeList> mapper = new LarJsonTypedMapper<>(ModelWithZonedDateTimeList.class,
                new LarJsonTypedReadConfiguration.Builder().setZonedDateTimeFormat(format).build());
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern(format);
        String date = ZonedDateTime.now().format(dateFormat);
        String json = "{\"whatever\" : [null, \"" + date + "\"]}";
        try(ModelWithZonedDateTimeList model = mapper.readObject(jsonToFile(tempDir, json))) {
            assertNotNull(model);
            assertNotNull(model.getWhatever());
            assertEquals(2, model.getWhatever().size());
            assertEquals(null, model.getWhatever().get(0));
            assertEquals(ZonedDateTime.parse(date, dateFormat), model.getWhatever().get(1));
        }
    }

    @Test
    void testReadNestedLists(@TempDir Path tempDir) throws IOException, LarJsonException {
        LarJsonTypedMapper<ModelWithNestedLists> mapper =
                new LarJsonTypedMapper<>(ModelWithNestedLists.class);
        String json = "{\"whatever\" :[[[null, [], [null, [], [\"hello\", null, \"bonjour\"], [\"hola\"]]], null, " +
                "[]], [], null]}";
        try(ModelWithNestedLists model = mapper.readObject(jsonToFile(tempDir, json))) {
            assertNotNull(model);
            assertEquals(Arrays.asList(Arrays.asList(Arrays.asList(null, Arrays.asList(), Arrays.asList(
                            null, Arrays.asList(), Arrays.asList("hello", null, "bonjour"), Arrays.asList("hola"))),
                            null, Arrays.asList()), Arrays.asList(), null), model.getWhatever());
        }
    }

    @Test
    void testReadObjectOfObject(@TempDir Path tempDir) throws IOException, LarJsonException {
        LarJsonTypedMapper<ModelWithObject> mapper = new LarJsonTypedMapper<>(ModelWithObject.class);
        String json = "{\"something\": {\"whatever\" : \"hello\"}}";
        try(ModelWithObject model = mapper.readObject(jsonToFile(tempDir, json))) {
            assertNotNull(model);
            assertNotNull(model.getSomething());
            assertEquals("hello", model.getSomething().getWhatever());
        }
    }

    @Test
    void testReadListOfObject(@TempDir Path tempDir) throws IOException, LarJsonException {
        LarJsonTypedMapper<ModelWithObjectList> mapper = new LarJsonTypedMapper<>(ModelWithObjectList.class);
        String json = "{\"something\": [{\"whatever\" : \"hello\"}, null, {\"whatever\": null}, {}]}";
        try(ModelWithObjectList model = mapper.readObject(jsonToFile(tempDir, json))) {
            assertNotNull(model);
            assertNotNull(model.getSomething());
            assertEquals(4, model.getSomething().size());
            assertNotNull(model.getSomething().get(0));
            assertEquals("hello", model.getSomething().get(0).getWhatever());
            assertNull(model.getSomething().get(1));
            assertNotNull(model.getSomething().get(2));
            assertEquals(null, model.getSomething().get(2).getWhatever());
            assertNotNull(model.getSomething().get(3));
            assertEquals(null, model.getSomething().get(3).getWhatever());
        }
    }

    @Test
    void testReadArray(@TempDir Path tempDir) throws IOException, LarJsonException {
        LarJsonTypedMapper<ModelWithString> mapper = new LarJsonTypedMapper<>(ModelWithString.class);
        String json = "[{\"whatever\" : \"hello\"}, null, {\"whatever\": null}, {}]";
        try(LarJsonRootList<ModelWithString> model = mapper.readArray(jsonToFile(tempDir, json))) {
            assertNotNull(model);
            assertEquals(4, model.size());
            assertNotNull(model.get(0));
            assertEquals("hello", model.get(0).getWhatever());
            assertNull(model.get(1));
            assertNotNull(model.get(2));
            assertEquals(null, model.get(2).getWhatever());
            assertNotNull(model.get(3));
            assertEquals(null, model.get(3).getWhatever());
        }
    }

    @Test
    void testReadRichObject(@TempDir Path tempDir) throws IOException, LarJsonException, ParseException {
        LarJsonTypedMapper<RichModel> mapper = new LarJsonTypedMapper<>(RichModel.class);
        String birthDate = new SimpleDateFormat().format(new Date());
        String json = "{\"address\":{\"whatever\":\"Hotel California\"}, \"firstName\":\"Amine\", \"lasName\":null, " +
                "\"birthDate\":\"" + birthDate + "\", \"happy\":true, \"mad\":false, \"age\": 28, " +
                "\"whatever\":\"anything\", \"words\":[\"SALUT\", null, \"HOLA\"], \"scores\":[{\"whatever\":12.70}, " +
                "{\"whatever\":20}, {\"whatever\":-0.014}, {}]}";
        try(RichModel model = mapper.readObject(jsonToFile(tempDir, json))) {
            assertNotNull(model);
            assertNotNull(model.getAddress());
            assertEquals("Hotel California", model.getAddress().getWhatever());
            assertEquals("Amine", model.getFirstName());
            assertEquals(null, model.getLastName());
            assertEquals(null, model.getMiddleName());
            assertEquals(new SimpleDateFormat().parse(birthDate), model.getBirthDate());
            assertEquals(true, model.getHappy());
            assertEquals(false, model.isMad());
            assertEquals(28, model.getAge());
            assertEquals("anything", model.getWhatever());
            assertEquals(Arrays.asList(TestEnum.SALUT, null, TestEnum.HOLA), model.getWords());
            assertNotNull(model.getScores());
            assertEquals(4, model.getScores().size());
            Iterator<Double> iterator = Arrays.asList(12.7, 20.0, -0.014, null).iterator();
            for(ModelWithDoubleWrapper score : model.getScores()) {
                assertNotNull(score);
                assertEquals(iterator.next(), score.getWhatever());
            }
            assertFalse(iterator.hasNext());
        }
    }

    @Test
    void testReadArrayFollowedWithJson(@TempDir Path tempDir) throws IOException, LarJsonException {
        LarJsonTypedMapper<ModelWithString> mapper = new LarJsonTypedMapper<>(ModelWithString.class,
                new LarJsonTypedReadConfiguration.Builder().setLenient(true).build());
        String json = "[{\"whatever\" : \"hello\"}, null, {\"whatever\": null}, {}] {}";
        try (LarJsonRootList<ModelWithString> model = mapper.readArray(jsonToFile(tempDir, json))) {
            fail();
        }catch (LarJsonParseException expected) {
        }
    }

    @Test
    void testReadObjectFollowedWithJson(@TempDir Path tempDir) throws IOException, LarJsonException {
        LarJsonTypedMapper<ModelWithString> mapper = new LarJsonTypedMapper<>(ModelWithString.class,
                new LarJsonTypedReadConfiguration.Builder().setLenient(true).build());
        String json = "{\"whatever\": null} []";
        try(ModelWithString model = mapper.readObject(jsonToFile(tempDir, json))) {
            fail();
        }catch (LarJsonParseException expected) {
        }
    }

    @Test
    void testReadLenient(@TempDir Path tempDir) throws IOException, LarJsonException {
        LarJsonTypedMapper<ModelWithString> mapper = new LarJsonTypedMapper<>(ModelWithString.class,
                new LarJsonTypedReadConfiguration.Builder().setLenient(true).build());
        String json = "{\"whatever\" : hello}";
        try(ModelWithString model = mapper.readObject(jsonToFile(tempDir, json))) {
            assertNotNull(model);
            assertEquals("hello", model.getWhatever());
        }
    }

    @Test
    void testReadStrict(@TempDir Path tempDir) throws IOException, LarJsonException {
        LarJsonTypedMapper<ModelWithString> mapper = new LarJsonTypedMapper<>(ModelWithString.class);
        String json = "{\"whatever\" : hello}";
        try(ModelWithString model = mapper.readObject(jsonToFile(tempDir, json))) {
            assertNotNull(model);
            try {
                model.getWhatever();
                fail();
            }catch (LarJsonValueReadException expected) {
            }
        }
    }

    @Test
    void testReadConstraintsValidationDisabled(@TempDir Path tempDir) throws IOException, LarJsonException {
        LarJsonTypedMapper<ModelWithValidation> mapper = new LarJsonTypedMapper<>(ModelWithValidation.class);
        String json = "{\"whatever\" : \"  \t\"}";
        try(ModelWithValidation model = mapper.readObject(jsonToFile(tempDir, json))) {
            assertNotNull(model);
            assertEquals("  \t", model.getWhatever());
        }
    }

    @Test
    void testReadConstraintsValid(@TempDir Path tempDir) throws IOException, LarJsonException {
        LarJsonTypedMapper<ModelWithValidation> mapper = new LarJsonTypedMapper<>(ModelWithValidation.class,
                new LarJsonTypedReadConfiguration.Builder().enableValidation().build());
        String json = "{\"whatever\" : \"  \tHi  \n\"}";
        try(ModelWithValidation model = mapper.readObject(jsonToFile(tempDir, json))) {
            assertNotNull(model);
            assertEquals("  \tHi  \n", model.getWhatever());
        }
    }

    @Test
    void testReadConstraintsInvalid(@TempDir Path tempDir) throws IOException, LarJsonException {
        LarJsonTypedMapper<ModelWithValidation> mapper = new LarJsonTypedMapper<>(ModelWithValidation.class,
                new LarJsonTypedReadConfiguration.Builder().enableValidation().build());
        String json = "{\"whatever\" : \"  \t  \n\"}";
        try(ModelWithValidation model = mapper.readObject(jsonToFile(tempDir, json))) {
            fail();
        } catch (LarJsonConstraintViolationException expected) {
        }
    }

    @Test
    void testReadListConstraintsValidationDisabled(@TempDir Path tempDir) throws IOException, LarJsonException {
        LarJsonTypedMapper<ModelWithValidation> mapper = new LarJsonTypedMapper<>(ModelWithValidation.class);
        String json = "[{\"whatever\" : \"  \t\"}, {\"whatever\" : \"Yes\uABCD\"}]";
        try(LarJsonRootList<ModelWithValidation> model = mapper.readArray(jsonToFile(tempDir, json))) {
            assertNotNull(model);
        }
    }

    @Test
    void testReadListConstraintsValid(@TempDir Path tempDir) throws IOException, LarJsonException {
        LarJsonTypedMapper<ModelWithValidation> mapper = new LarJsonTypedMapper<>(ModelWithValidation.class,
                new LarJsonTypedReadConfiguration.Builder().enableValidation().build());
        String json = "[{\"whatever\" : \" hello\"}, {\"whatever\" : \"Yes\uABCD\"}]";
        try(LarJsonRootList<ModelWithValidation> model = mapper.readArray(jsonToFile(tempDir, json))) {
            assertNotNull(model);
        }
    }

    @Test
    void testReadListConstraintsInvalid(@TempDir Path tempDir) throws IOException, LarJsonException {
        LarJsonTypedMapper<ModelWithValidation> mapper = new LarJsonTypedMapper<>(ModelWithValidation.class,
                new LarJsonTypedReadConfiguration.Builder().enableValidation().build());
        String json = "[{\"whatever\" : \"Yes\uABCD\"}, {\"whatever\" : \"  \t\"}]";
        try(LarJsonRootList<ModelWithValidation> model = mapper.readArray(jsonToFile(tempDir, json))) {
            fail();
        } catch (LarJsonConstraintViolationException expected) {
        }
    }

}
