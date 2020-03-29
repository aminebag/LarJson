package com.aminebag.larjson.mapper;

import com.aminebag.larjson.api.LarJsonPath;
import com.aminebag.larjson.api.LarJsonRootList;
import com.aminebag.larjson.configuration.EqualsDelegate;
import com.aminebag.larjson.configuration.LarJsonTypedReadConfiguration;
import com.aminebag.larjson.configuration.LarJsonTypedWriteConfiguration;
import com.aminebag.larjson.configuration.PropertyResolver;
import com.aminebag.larjson.exception.LarJsonException;
import com.aminebag.larjson.exception.LarJsonValueReadException;
import com.aminebag.larjson.mapper.exception.LarJsonMappingDefinitionException;
import com.aminebag.larjson.mapper.exception.LarJsonUnknownAttributeException;
import com.google.gson.stream.JsonWriter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.Method;
import java.nio.file.Path;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.aminebag.larjson.mapper.LarJsonMapperTestUtils.jsonToFile;
import static com.aminebag.larjson.mapper.LarJsonTypedMapperTestModels.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Amine Bagdouri
 */
public class LarJsonTypedMapperTest {

    @Test
    void testMapperGetDefaultConfiguration() {
        LarJsonTypedMapper<ModelWithString> mapper = new LarJsonTypedMapper<>(ModelWithString.class);
        assertNotNull(mapper.getConfiguration());
    }

    @Test
    void testMapperGetCustomConfiguration() {
        LarJsonTypedReadConfiguration configuration = new LarJsonTypedReadConfiguration.Builder().build();
        LarJsonTypedMapper<ModelWithString> mapper = new LarJsonTypedMapper<>(ModelWithString.class, configuration);
        assertEquals(configuration, mapper.getConfiguration());
    }

    @Test
    void testMapperGetRootInterface() {
        LarJsonTypedMapper<ModelWithString> mapper = new LarJsonTypedMapper<>(ModelWithString.class);
        assertEquals(ModelWithString.class, mapper.getRootInterface());
    }

    @Test
    void testMapperToString() {
        LarJsonTypedMapper<ModelWithObject> mapper = new LarJsonTypedMapper<>(ModelWithObject.class);
        assertNotNull(mapper.toString());
    }

    @Test
    void testMapperUnresolvableParameterType() {
        try {
            new LarJsonTypedMapper<>(ModelWithUntypedList.class);
            fail();
        } catch (LarJsonMappingDefinitionException expected) {
        }
    }

    @Test
    void testMapperWithClassTypeModel() {
        try {
            new LarJsonTypedMapper<>(ClassTypeModel.class,
                    new LarJsonTypedReadConfiguration.Builder()
                            .setUnsupportedMethodAllowed(true)
                            .build());
            fail();
        } catch (LarJsonMappingDefinitionException expected) {
        }
    }

    @Test
    void testMapperWithModelWithClassTypeProperty() {
        try {
            new LarJsonTypedMapper<>(ModelWithClassTypeProperty.class);
            fail();
        } catch (LarJsonMappingDefinitionException expected) {
        }
    }

    @Test
    void testMapperWithModelWithClassPropertyThrowExceptionOnUnsupportedMethodCalled(@TempDir Path tempDir)
            throws IOException, LarJsonException {
        LarJsonTypedMapper<ModelWithClassTypeProperty> mapper = new LarJsonTypedMapper<>(ModelWithClassTypeProperty.class,
                new LarJsonTypedReadConfiguration.Builder()
                        .setUnsupportedMethodAllowed(true)
                        .build());
        String json = "{}";
        try(ModelWithClassTypeProperty model = mapper.readObject(jsonToFile(tempDir, json))){
            try {
                model.getWhatever();
                fail();
            }catch (UnsupportedOperationException expected) {
            }
        }
    }

    @Test
    void testMapperWithModelWithClassPropertyReturnNullOnUnsupportedMethodCalled(@TempDir Path tempDir)
            throws IOException, LarJsonException {
        LarJsonTypedMapper<ModelWithClassTypeProperty> mapper = new LarJsonTypedMapper<>(ModelWithClassTypeProperty.class,
                new LarJsonTypedReadConfiguration.Builder()
                        .setUnsupportedMethodAllowed(true)
                        .returnNullOnUnsupportedMethodCalled()
                        .build());
        String json = "{}";
        try(ModelWithClassTypeProperty model = mapper.readObject(jsonToFile(tempDir, json))){
            assertNull(model.getWhatever());
        }
    }

    @Test
    void testMapperWithModelWithClassPropertyCustomUnsupportedMethodCalledBehavior(@TempDir Path tempDir)
            throws IOException, LarJsonException {
        ClassTypeModel classTypeModel = new ClassTypeModel();
        LarJsonTypedMapper<ModelWithClassTypeProperty> mapper = new LarJsonTypedMapper<>(ModelWithClassTypeProperty.class,
                new LarJsonTypedReadConfiguration.Builder()
                        .setUnsupportedMethodAllowed(true)
                        .setUnsupportedMethodCalledBehavior((o,m,a)->{
                            assertNotNull(o);
                            assertTrue(ModelWithClassTypeProperty.class.isAssignableFrom(o.getClass()));
                            try {
                                assertEquals(ModelWithClassTypeProperty.class.getDeclaredMethod("getWhatever"), m);
                            } catch (NoSuchMethodException e) {
                                fail();
                            }
                            assertNull(a);
                            return classTypeModel;
                        })
                        .build());
        String json = "{}";
        try(ModelWithClassTypeProperty model = mapper.readObject(jsonToFile(tempDir, json))){
            assertEquals(classTypeModel, model.getWhatever());
        }
    }

    @Test
    void testMapperWithModelWithListOfClassTypeProperty() {
        try {
            new LarJsonTypedMapper<>(ModelWithListOfClassTypeProperty.class);
            fail();
        } catch (LarJsonMappingDefinitionException expected) {
        }
    }

    @Test
    void testMapperWithModelWithInvalidGetter() {
        try {
            new LarJsonTypedMapper<>(ModelWithInvalidGetter.class,
                    new LarJsonTypedReadConfiguration.Builder().build());
            fail();
        } catch (LarJsonMappingDefinitionException expected) {
        }
    }

    @Test
    void testMapperWithModelWithUnsupportedMethod() {
        try {
            new LarJsonTypedMapper<>(ModelWithUnsupportedMethod.class,
                    new LarJsonTypedReadConfiguration.Builder().build());
            fail();
        } catch (LarJsonMappingDefinitionException expected) {
        }
    }

    @Test
    void testMapperWithModelWithUnsupportedMethodAllowed() {
        new LarJsonTypedMapper<>(ModelWithUnsupportedMethod.class,
                    new LarJsonTypedReadConfiguration.Builder().setUnsupportedMethodAllowed(true).build());
    }

    @Test
    void testMapperWithModelWithStringSetterOnly() {
        try {
            new LarJsonTypedMapper<>(ModelWithStringSetterOnly.class,
                    new LarJsonTypedReadConfiguration.Builder().setMutable(true).build());
            fail();
        } catch (LarJsonMappingDefinitionException expected) {
        }
    }

    @Test
    void testMapperWithModelWithStringSetterTypeDifferentThanGetterType() {
        try {
            new LarJsonTypedMapper<>(ModelWithStringSetterTypeDifferentThanGetterType.class,
                    new LarJsonTypedReadConfiguration.Builder().setMutable(true).build());
            fail();
        } catch (LarJsonMappingDefinitionException expected) {
        }
    }

    @Test
    void testMapperWithModelWithStringManySettersForOneGetter() {
        try {
            new LarJsonTypedMapper<>(ModelWithStringManySettersForOneGetter.class,
                    new LarJsonTypedReadConfiguration.Builder().setMutable(true).build());
            fail();
        } catch (LarJsonMappingDefinitionException expected) {
        }
    }

    @Test
    void testMapperWithModelWithStringSetterOnlyUnsupportedMethodAllowed(@TempDir Path tempDir)
            throws IOException, LarJsonException {
        LarJsonTypedMapper<ModelWithStringSetterOnly> mapper = new LarJsonTypedMapper<>(ModelWithStringSetterOnly.class,
                new LarJsonTypedReadConfiguration.Builder().setMutable(true)
                        .setUnsupportedMethodAllowed(true).build());
        String json = "{}";
        try(ModelWithStringSetterOnly model = mapper.readObject(jsonToFile(tempDir, json))){
            assertNotNull(model);
            try {
                model.setWhatever("hello");
                fail();
            }catch (UnsupportedOperationException expected) {
            }
        }
    }

    @Test
    void testMapperWithModelWithStringSetterTypeDifferentThanGetterTypeUnsupportedMethodAllowed(@TempDir Path tempDir)
            throws IOException, LarJsonException {
        LarJsonTypedMapper<ModelWithStringSetterTypeDifferentThanGetterType> mapper =
                new LarJsonTypedMapper<>(ModelWithStringSetterTypeDifferentThanGetterType.class,
                    new LarJsonTypedReadConfiguration.Builder().setMutable(true)
                            .setUnsupportedMethodAllowed(true).build());
        String json = "{\"whatever\":\"salut\"}";
        try(ModelWithStringSetterTypeDifferentThanGetterType model = mapper.readObject(jsonToFile(tempDir, json))){
            assertNotNull(model);
            assertEquals("salut", model.getWhatever());
            try {
                model.setWhatever(5);
                fail();
            }catch (UnsupportedOperationException expected) {
            }
        }
    }

    @Test
    void testMapperWithModelWithStringManySettersForOneGetterUnsupportedMethodAllowed(@TempDir Path tempDir)
            throws IOException, LarJsonException{
        LarJsonTypedMapper<ModelWithStringManySettersForOneGetter> mapper =
                new LarJsonTypedMapper<>(ModelWithStringManySettersForOneGetter.class,
                        new LarJsonTypedReadConfiguration.Builder().setMutable(true)
                                .setUnsupportedMethodAllowed(true).build());
        String json = "{\"whatever\":\"salut\"}";
        try(ModelWithStringManySettersForOneGetter model = mapper.readObject(jsonToFile(tempDir, json))){
            assertNotNull(model);
            assertEquals("salut", model.getWhatever());
            try {
                model.setWhatever(5);
                fail();
            }catch (UnsupportedOperationException expected) {
            }
            try {
                model.setWhatever(7.1f);
                fail();
            }catch (UnsupportedOperationException expected) {
            }
            assertEquals("salut", model.getWhatever());
            model.setWhatever("here");
            assertEquals("here", model.getWhatever());
        }
    }

    @Test
    void testMapperWithModelWithListOfClassPropertyThrowExceptionOnUnsupportedMethodCalled(@TempDir Path tempDir)
            throws IOException, LarJsonException {
        LarJsonTypedMapper<ModelWithListOfClassTypeProperty> mapper = new LarJsonTypedMapper<>(
                ModelWithListOfClassTypeProperty.class,
                new LarJsonTypedReadConfiguration.Builder()
                        .setUnsupportedMethodAllowed(true)
                        .build());
        String json = "{}";
        try(ModelWithListOfClassTypeProperty model = mapper.readObject(jsonToFile(tempDir, json))){
            try {
                model.getWhatever();
                fail();
            }catch (UnsupportedOperationException expected) {
            }
        }
    }

    @Test
    void testMapperWithModelWithListOfClassPropertyReturnNullOnUnsupportedMethodCalled(@TempDir Path tempDir)
            throws IOException, LarJsonException {
        LarJsonTypedMapper<ModelWithListOfClassTypeProperty> mapper = new LarJsonTypedMapper<>(
                ModelWithListOfClassTypeProperty.class,
                new LarJsonTypedReadConfiguration.Builder()
                        .setUnsupportedMethodAllowed(true)
                        .returnNullOnUnsupportedMethodCalled()
                        .build());
        String json = "{}";
        try(ModelWithListOfClassTypeProperty model = mapper.readObject(jsonToFile(tempDir, json))){
            assertNull(model.getWhatever());
        }
    }

    @Test
    void testMapperWithModelWithListOfClassPropertyCustomUnsupportedMethodCalledBehavior(@TempDir Path tempDir)
            throws IOException, LarJsonException {
        LarJsonTypedMapper<ModelWithListOfClassTypeProperty> mapper = new LarJsonTypedMapper<>(
                ModelWithListOfClassTypeProperty.class,
                new LarJsonTypedReadConfiguration.Builder()
                        .setUnsupportedMethodAllowed(true)
                        .setUnsupportedMethodCalledBehavior((o,m,a)->{
                            assertNotNull(o);
                            assertTrue(ModelWithListOfClassTypeProperty.class.isAssignableFrom(o.getClass()));
                            try {
                                assertEquals(ModelWithListOfClassTypeProperty.class.getDeclaredMethod("getWhatever"), m);
                            } catch (NoSuchMethodException e) {
                                fail();
                            }
                            assertNull(a);
                            return Collections.emptyList();
                        })
                        .build());
        String json = "{}";
        try(ModelWithListOfClassTypeProperty model = mapper.readObject(jsonToFile(tempDir, json))){
            assertEquals(Collections.emptyList(), model.getWhatever());
        }
    }

    @Test
    void testMapperThrowExceptionOnValueReadFailed(@TempDir Path tempDir)
            throws IOException, LarJsonException {
        LarJsonTypedMapper<ModelWithDate> mapper = new LarJsonTypedMapper<>(ModelWithDate.class,
                new LarJsonTypedReadConfiguration.Builder()
                        .build());
        String json = "{\"whatever\":\"invalid date\"}";
        try(ModelWithDate model = mapper.readObject(jsonToFile(tempDir, json))){
            try{
                model.getWhatever();
                fail();
            }catch (LarJsonValueReadException expected) {
            }
        }
    }

    @Test
    void testMapperReturnNullOnValueReadFailed(@TempDir Path tempDir)
            throws IOException, LarJsonException {
        LarJsonTypedMapper<ModelWithDate> mapper = new LarJsonTypedMapper<>(ModelWithDate.class,
                new LarJsonTypedReadConfiguration.Builder()
                        .returnNullOnValueReadFailed()
                        .build());
        String json = "{\"whatever\":\"invalid date\"}";
        try(ModelWithDate model = mapper.readObject(jsonToFile(tempDir, json))){
            assertNull(model.getWhatever());
        }
    }

    @Test
    void testMapperReturnZeroOnValueReadFailed(@TempDir Path tempDir)
            throws IOException, LarJsonException {
        LarJsonTypedMapper<ModelWithPrimitiveFloat> mapper = new LarJsonTypedMapper<>(ModelWithPrimitiveFloat.class,
                new LarJsonTypedReadConfiguration.Builder()
                        .returnNullOnValueReadFailed()
                        .build());
        String json = "{\"whatever\":" +
                "0.00878568756782548789548745624855555555556666666666666668888888888888888898798798798765456547q4654}";
        try (ModelWithPrimitiveFloat model = mapper.readObject(jsonToFile(tempDir, json))) {
            assertEquals(0f, model.getWhatever());
        }
    }

    @Test
    void testMapperCustomValueReadFailedBehavior(@TempDir Path tempDir)
        throws IOException, LarJsonException {
        class CustomException extends RuntimeException{}
        LarJsonTypedMapper<ModelWithDate> mapper = new LarJsonTypedMapper<>(ModelWithDate.class,
                new LarJsonTypedReadConfiguration.Builder()
                        .setValueReadFailedBehavior((o,m,a,p,e)-> {
                            assertNotNull(o);
                            assertTrue(ModelWithDate.class.isAssignableFrom(o.getClass()));
                            try {
                                assertEquals(ModelWithDate.class.getDeclaredMethod("getWhatever"), m);
                            } catch (NoSuchMethodException ex) {
                                fail();
                            }
                            assertNull(a);
                            assertEquals("$.whatever", p);
                            assertNotNull(e);
                            assertTrue(LarJsonException.class.isAssignableFrom(e.getClass()));
                            throw new CustomException();
                        })
                        .build());
        String json = "{\"whatever\":\"invalid date\"}";
        try(ModelWithDate model = mapper.readObject(jsonToFile(tempDir, json))){
            try {
                model.getWhatever();
                fail();
            } catch (CustomException expected) {
            }
        }
    }

    @Test
    void testMapperListModelCustomValueReadFailedBehavior(@TempDir Path tempDir)
            throws IOException, LarJsonException {
        class CustomException extends RuntimeException{}
        LarJsonTypedMapper<ModelWithDateList> mapper = new LarJsonTypedMapper<>(ModelWithDateList.class,
                new LarJsonTypedReadConfiguration.Builder()
                        .setValueReadFailedBehavior((o,m,a,p,e)-> {
                            assertNotNull(o);
                            assertTrue(List.class.isAssignableFrom(o.getClass()));
                            assertTrue(List.class.isAssignableFrom(m.getDeclaringClass()));
                            assertEquals("get", m.getName());
                            assertEquals(1, m.getParameterCount());
                            assertEquals(int.class, m.getParameterTypes()[0]);
                            assertNotNull(a);
                            assertEquals(1, a.length);
                            assertEquals(1, a[0]);
                            assertEquals("$.whatever[1]", p);
                            assertNotNull(e);
                            assertTrue(LarJsonException.class.isAssignableFrom(e.getClass()));
                            throw new CustomException();
                        })
                        .build());
        String json = "{\"whatever\":[\"" + new SimpleDateFormat().format(new Date()) + "\", \"invalid date\"]}";
        try(ModelWithDateList model = mapper.readObject(jsonToFile(tempDir, json))){
            model.getWhatever().get(0);
            try {
                model.getWhatever().get(1);
                fail();
            } catch (CustomException expected) {
            }
            model.getWhatever().get(0);
        }
    }

    @Test
    void testMapperWithCache(@TempDir Path tempDir) throws IOException, LarJsonException {
        LarJsonTypedMapper<ModelWithObject> mapper = new LarJsonTypedMapper<>(ModelWithObject.class,
                new LarJsonTypedReadConfiguration.Builder().build());
        String json = "{\"something\": {\"whatever\" : \"hello\"}}";
        try(ModelWithObject model = mapper.readObject(jsonToFile(tempDir, json))){
            assertTrue(model.getSomething() == model.getSomething());
            assertEquals(model.getSomething(), model.getSomething());
            assertEquals(model.getSomething().hashCode(), model.getSomething().hashCode());
        }
    }

    @Test
    void testMapperWithoutCache(@TempDir Path tempDir) throws IOException, LarJsonException {
        LarJsonTypedMapper<ModelWithObject> mapper = new LarJsonTypedMapper<>(ModelWithObject.class,
                new LarJsonTypedReadConfiguration.Builder().setCacheSize(0).build());
        String json = "{\"something\": {\"whatever\" : \"hello\"}}";
        try(ModelWithObject model = mapper.readObject(jsonToFile(tempDir, json))){
            assertFalse(model.getSomething() == model.getSomething());
            assertEquals(model.getSomething(), model.getSomething());
            assertEquals(model.getSomething().hashCode(), model.getSomething().hashCode());
        }
    }

    @Test
    void testMapperCloseObject(@TempDir Path tempDir) throws IOException, LarJsonException {
        LarJsonTypedMapper<ModelWithString> mapper = new LarJsonTypedMapper<>(ModelWithString.class);
        String json = "{\"whatever\" : \"hello\"}";
        ModelWithString model = mapper.readObject(jsonToFile(tempDir, json));
        try {
            assertNotNull(model);
        } finally {
            mapper.close(model);
        }
    }

    @Test
    void testMapperCloseUncloseableObject() throws IOException {
        LarJsonTypedMapper<UncloseableModelWithString> mapper = new LarJsonTypedMapper<>(UncloseableModelWithString.class);
        try {
            mapper.close(() -> null);
            fail();
        }catch (IllegalArgumentException expected) {
        }
    }

    @Test
    void testMapperCloseUncloseableList() throws IOException {
        LarJsonTypedMapper<ModelWithString> mapper = new LarJsonTypedMapper<>(ModelWithString.class);
        try {
            mapper.close(new ArrayList<>());
            fail();
        }catch (IllegalArgumentException expected) {
        }
    }

    @Test
    void testMapperCloseSubObject(@TempDir Path tempDir) throws IOException, LarJsonException {
        LarJsonTypedMapper<ModelWithObject> mapper = new LarJsonTypedMapper<>(ModelWithObject.class);
        String json = "{\"something\" : {\"whatever\": \"hello\"}}";
        try(ModelWithObject model = mapper.readObject(jsonToFile(tempDir, json))){
            assertNotNull(model);
            assertNotNull(model.getSomething());
            try {
                model.getSomething().close();
                fail();
            } catch (UnsupportedOperationException expected) {
            }
        }
    }

    @Test
    void testMapperCloseArray(@TempDir Path tempDir) throws IOException, LarJsonException {
        LarJsonTypedMapper<ModelWithString> mapper = new LarJsonTypedMapper<>(ModelWithString.class);
        String json = "[{\"whatever\" : \"hello\"}, null, {\"whatever\": null}, {}]";
        List<ModelWithString> model = mapper.readArray(jsonToFile(tempDir, json));
        try {
            assertNotNull(model);
        } finally {
            mapper.close(model);
        }
    }

    @Test
    void testMapperCloseArrayElement(@TempDir Path tempDir) throws IOException, LarJsonException {
        LarJsonTypedMapper<ModelWithString> mapper = new LarJsonTypedMapper<>(ModelWithString.class);
        String json = "[{\"whatever\" : \"hello\"}, null, {\"whatever\": null}, {}]";
        try(LarJsonRootList<ModelWithString> model = mapper.readArray(jsonToFile(tempDir, json))){
            assertNotNull(model);
            assertEquals(4, model.size());
            assertNotNull(model.get(0));
            try {
                model.get(0).close();
                fail();
            } catch (UnsupportedOperationException expected) {
            }
        }
    }

    @Test
    void testMapperUnknownJsonAttributeAllowed(@TempDir Path tempDir) throws IOException, LarJsonException {
        LarJsonTypedMapper<ModelWithString> mapper = new LarJsonTypedMapper<>(ModelWithString.class);
        String json = "{\"whatever\": \"hello\", \"unknown\": 13.7}";
        try(ModelWithString model = mapper.readObject(jsonToFile(tempDir, json))) {
            assertNotNull(model);
            assertEquals("hello", model.getWhatever());
        }
    }

    @Test
    void testMapperUnknownJsonAttributeNotAllowed(@TempDir Path tempDir) throws IOException, LarJsonException {
        LarJsonTypedMapper<ModelWithString> mapper = new LarJsonTypedMapper<>(ModelWithString.class,
                new LarJsonTypedReadConfiguration.Builder().setUnknownJsonAttributeAllowed(false).build());
        String json = "{\"whatever\": \"hello\", \"unknown\": 13.7}";
        try(ModelWithString model = mapper.readObject(jsonToFile(tempDir, json))) {
            fail();
        } catch (LarJsonUnknownAttributeException expected) {
        }
    }

    @Test
    void testMapperEqualsDeleagte(@TempDir Path tempDir) throws IOException, LarJsonException, ParseException {
        LarJsonTypedMapper<RichModel> mapper = new LarJsonTypedMapper<>(RichModel.class,
                new LarJsonTypedReadConfiguration.Builder()
                        .setEqualsDelegate(RichModel.class, new EqualsDelegate<RichModel>() {
                            @Override
                            public boolean equals(RichModel thisInstance, Object otherInstance) {
                                RichModel other = (RichModel) otherInstance;
                                return other.getFirstName().equals(other.getFirstName());
                            }

                            @Override
                            public int hashCode(RichModel thisInstance) {
                                return thisInstance.getFirstName().hashCode();
                            }
                        }).build());
        String birthDate = new SimpleDateFormat().format(new Date());
        String json1 = "{\"address\":{\"whatever\":\"Hotel California\"}, \"firstName\":\"Amine\", \"lasName\":null, " +
                "\"birthDate\":\"" + birthDate + "\", \"happy\":true, \"mad\":false, \"age\": 28, " +
                "\"whatever\":\"anything\", \"words\":[\"SALUT\", null, \"HOLA\"], \"scores\":[{\"whatever\":12.70}, " +
                "{\"whatever\":20}, {\"whatever\":-0.014}, {}]}";
        String json2 = "{\"address\":{\"whatever\":\"Hotel California\"}, \"firstName\":\"Amine\", \"mad\":true}";
        try(RichModel model1 = mapper.readObject(jsonToFile(tempDir, json1));
            RichModel model2 = mapper.readObject(jsonToFile(tempDir, json2))) {
            assertNotNull(model1);
            assertNotNull(model2);
            assertEquals(model1, model2);
            assertEquals(model1.hashCode(), model2.hashCode());
            assertEquals(63380500, model1.hashCode());
        }
    }

    @Test
    void testMapperDefaultEqualsRootTrue(@TempDir Path tempDir) throws IOException, LarJsonException {
        LarJsonTypedMapper<ModelWithStringAndInt> mapper = new LarJsonTypedMapper<>(ModelWithStringAndInt.class,
                new LarJsonTypedReadConfiguration.Builder().build());
        String json = "{\"whatever\": \"hello\", \"something\": 13}";
        try(ModelWithStringAndInt model1 = mapper.readObject(jsonToFile(tempDir, json));
            ModelWithStringAndInt model2 = mapper.readObject(jsonToFile(tempDir, json))) {
            assertNotNull(model1);
            assertNotNull(model2);
            assertEquals(model1, model2);
            assertEquals(model1.hashCode(), model2.hashCode());
            assertEquals(99162335, model1.hashCode());
        }
    }

    @Test
    void testMapperDefaultEqualsRootFalse(@TempDir Path tempDir) throws IOException, LarJsonException {
        LarJsonTypedMapper<ModelWithStringAndInt> mapper = new LarJsonTypedMapper<>(ModelWithStringAndInt.class,
                new LarJsonTypedReadConfiguration.Builder().build());
        String json1 = "{\"whatever\": \"hello\", \"something\": 13}";
        String json2 = "{\"whatever\": \"salut\", \"something\": 13}";
        try(ModelWithStringAndInt model1 = mapper.readObject(jsonToFile(tempDir, json1));
            ModelWithStringAndInt model2 = mapper.readObject(jsonToFile(tempDir, json2))) {
            assertNotNull(model1);
            assertNotNull(model2);
            assertNotEquals(model1, model2);
            assertNotEquals(model1.hashCode(), model2.hashCode());
            assertEquals(99162335, model1.hashCode());
            assertEquals(109202186, model2.hashCode());
        }
    }

    @Test
    void testMapperDefaultEqualsRootTrueWithCustomImpl(@TempDir Path tempDir) throws IOException, LarJsonException {
        LarJsonTypedMapper<ModelWithStringAndInt> mapper = new LarJsonTypedMapper<>(ModelWithStringAndInt.class,
                new LarJsonTypedReadConfiguration.Builder().build());
        String json = "{\"whatever\": \"hello\", \"something\": 13}";
        try(ModelWithStringAndInt model1 = mapper.readObject(jsonToFile(tempDir, json))) {
            ModelWithStringAndInt model2 = new ModelWithStringAndInt() {
                @Override
                public Object clone() {
                    return null;
                }

                @Override
                public void write(JsonWriter jsonWriter) throws IOException, LarJsonException {

                }

                @Override
                public void write(Writer writer) throws IOException, LarJsonException {

                }

                @Override
                public void write(JsonWriter jsonWriter, LarJsonTypedWriteConfiguration writeConfiguration) throws IOException, LarJsonException {

                }

                @Override
                public void getLarJsonPath(StringBuilder sb) {

                }

                @Override
                public LarJsonPath getParentLarJsonPath() {
                    return null;
                }

                @Override
                public int getSomething() {
                    return 13;
                }

                @Override
                public String getWhatever() {
                    return "hello";
                }

                @Override
                public void close() throws IOException {

                }

                @Override
                public int hashCode() {
                    return getSomething() + getWhatever().hashCode();
                }
            };
            assertNotNull(model1);
            assertNotNull(model2);
            assertTrue(model1.equals(model2));
            assertEquals(model1.hashCode(), model2.hashCode());
            assertEquals(99162335, model1.hashCode());
        }
    }

    @Test
    void testMapperDefaultEqualsRootFalseWithCustomImpl(@TempDir Path tempDir) throws IOException, LarJsonException {
        LarJsonTypedMapper<ModelWithStringAndInt> mapper = new LarJsonTypedMapper<>(ModelWithStringAndInt.class,
                new LarJsonTypedReadConfiguration.Builder().build());
        String json = "{\"whatever\": \"hello\", \"something\": 13}";
        try(ModelWithStringAndInt model1 = mapper.readObject(jsonToFile(tempDir, json))) {
            ModelWithStringAndInt model2 = new ModelWithStringAndInt() {
                @Override
                public Object clone() {
                    return null;
                }

                @Override
                public void write(JsonWriter jsonWriter) throws IOException, LarJsonException {

                }

                @Override
                public void write(Writer writer) throws IOException, LarJsonException {

                }

                @Override
                public void write(JsonWriter jsonWriter, LarJsonTypedWriteConfiguration writeConfiguration) throws IOException, LarJsonException {

                }

                @Override
                public void getLarJsonPath(StringBuilder sb) {

                }

                @Override
                public LarJsonPath getParentLarJsonPath() {
                    return null;
                }

                @Override
                public int getSomething() {
                    return 12;
                }

                @Override
                public String getWhatever() {
                    return "hello";
                }

                @Override
                public void close() throws IOException {

                }

                @Override
                public int hashCode() {
                    return getSomething() + getWhatever().hashCode();
                }
            };
            assertNotNull(model1);
            assertNotNull(model2);
            assertFalse(model1.equals(model2));
            assertNotEquals(model1.hashCode(), model2.hashCode());
            assertEquals(99162335, model1.hashCode());
            assertEquals(99162334, model2.hashCode());

        }
    }

    @Test
    void testMapperPropertyResolver(@TempDir Path tempDir) throws IOException, LarJsonException {
        LarJsonTypedMapper<ModelWithStringMutable> mapper = new LarJsonTypedMapper<>(
                ModelWithStringMutable.class,
                new LarJsonTypedReadConfiguration.Builder().setPropertyResolverFactory(
                        rootInterface -> new PropertyResolver() {
                            @Override
                            public Method findGetter(String attributeName, Collection<Method> candidates) {
                                return candidates.stream()
                                        .filter(g->g.getName().equals(attributeName))
                                        .findFirst()
                                        .orElse(null);
                            }

                            @Override
                            public boolean isGetter(Method method) {
                                return method.getName().startsWith("get");
                            }

                            @Override
                            public Method findSetter(Method getterMethod, Collection<Method> candidates) {
                                try {
                                    return ModelWithStringMutable.class.getMethod("setWhatever", String.class);
                                } catch (NoSuchMethodException e) {
                                    fail(e);
                                    return null;
                                }
                            }

                            @Override
                            public String getAttributeName(Method getter) {
                                return getter.getName();
                            }
                }).setMutable(true).build());
        String json1 = "{\"whatever\": \"hello\"}";
        String json2 = "{\"getWhatever\": \"hello\"}";
        try(ModelWithStringMutable model1 = mapper.readObject(jsonToFile(tempDir, json1));
            ModelWithStringMutable model2 = mapper.readObject(jsonToFile(tempDir, json2))) {
            assertNotNull(model1);
            assertEquals(null, model1.getWhatever());
            model1.setWhatever("salut");
            assertEquals("salut", model1.getWhatever());

            assertNotNull(model2);
            assertEquals("hello", model2.getWhatever());
            model2.setWhatever("salut");
            assertEquals("salut", model2.getWhatever());
        }
    }

    @Test
    void testMapperObjectToString(@TempDir Path tempDir) throws IOException, LarJsonException, ParseException {
        LarJsonTypedMapper<RichModel> mapper = new LarJsonTypedMapper<>(RichModel.class);
        String birthDate = new SimpleDateFormat().format(new Date());
        String json = "{\"address\":{\"whatever\":\"Hotel California\"}, \"firstName\":\"Amine\", \"lasName\":null, " +
                "\"birthDate\":\"" + birthDate + "\", \"happy\":true, \"mad\":false, \"age\": 28, " +
                "\"whatever\":\"anything\", \"words\":[\"SALUT\", null, \"HOLA\"], \"scores\":[{\"whatever\":12.70}, " +
                "{\"whatever\":20}, {\"whatever\":-0.014}, {}]}";
        try(RichModel model = mapper.readObject(jsonToFile(tempDir, json))) {
            assertNotNull(model);
            String json2 = model.toString();
            try(RichModel model2 = mapper.readObject(jsonToFile(tempDir, json2))) {
                assertEquals(model2, model);
            }
        }
    }

    @Test
    void testMapperObjectToStringException(@TempDir Path tempDir) throws IOException, LarJsonException {
        LarJsonTypedMapper<RichModel> mapper = new LarJsonTypedMapper<>(RichModel.class);
        String birthDate = new SimpleDateFormat().format(new Date());
        String json = "{\"address\":{\"whatever\":\"Hotel California\"}, \"firstName\":\"Amine\", \"lasName\":null, " +
                "\"birthDate\":\"" + birthDate + "\", \"happy\":true, \"mad\":false, \"age\": \"young\", " +
                "\"whatever\":\"anything\", \"words\":[\"SALUT\", null, \"HOLA\"], \"scores\":[{\"whatever\":12.70}, " +
                "{\"whatever\":20}, {\"whatever\":-0.014}, {}]}";
        try(RichModel model = mapper.readObject(jsonToFile(tempDir, json))) {
            assertNotNull(model);
            try {
                model.toString();
                fail();
            } catch (LarJsonValueReadException expected) {
            }
        }
    }
}
