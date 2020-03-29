package com.aminebag.larjson.configuration.propertyresolver;

import com.aminebag.larjson.configuration.PropertyResolver;
import com.aminebag.larjson.configuration.PropertyResolverFactory;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.Arrays;

import static com.aminebag.larjson.mapper.LarJsonTypedMapperTestModels.ModelWithMultipleProperties;
import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Amine Bagdouri
 */
public abstract class AbstractPropertyResolverTest {

    @Test
    void testFindGetterPresent() throws NoSuchMethodException {
        PropertyResolver resolver = propertyResolverFactory().get(ModelWithMultipleProperties.class);
        assertEquals(secondGetter(), resolver.findGetter(secondAttributeName(),
                Arrays.asList(firstGetter(), secondGetter(), thirdGetter(), fourthGetter())));
    }

    @Test
    void testFindBooleanGetter() throws NoSuchMethodException {
        PropertyResolver resolver = propertyResolverFactory().get(ModelWithMultipleProperties.class);
        assertEquals(fourthGetter(), resolver.findGetter(fourthAttributeName(),
                Arrays.asList(firstGetter(), secondGetter(), thirdGetter(), fourthGetter())));
    }

    @Test
    void testFindGetterIncorrect() throws NoSuchMethodException {
        PropertyResolver resolver = propertyResolverFactory().get(ModelWithMultipleProperties.class);
        assertNull(resolver.findGetter(secondAttributeNameIncorrect(),
                Arrays.asList(firstGetter(), secondGetter(), thirdGetter(), fourthGetter())));
    }

    @Test
    void testFindGetterMissing() throws NoSuchMethodException {
        PropertyResolver resolver = propertyResolverFactory().get(ModelWithMultipleProperties.class);
        assertNull(resolver.findGetter(secondAttributeName(),
                Arrays.asList(firstGetter(), thirdGetter(), fourthGetter())));
    }

    @Test
    void testFindGetterConsecutiveUpperCases() throws NoSuchMethodException {
        PropertyResolver resolver = propertyResolverFactory().get(ModelWithMultipleProperties.class);
        assertEquals(thirdGetter(), resolver.findGetter(thirdAttributeName(),
                Arrays.asList(firstGetter(), secondGetter(), thirdGetter(), fourthGetter())));
    }

    @Test
    void testFindGetterConsecutiveUpperCasesIncorrect() throws NoSuchMethodException {
        PropertyResolver resolver = propertyResolverFactory().get(ModelWithMultipleProperties.class);
        assertNull(resolver.findGetter(thirdAttributeNameIncorrect(),
                Arrays.asList(firstGetter(), secondGetter(), thirdGetter(), fourthGetter())));
    }

    @Test
    void testFindBooleanSetter() throws NoSuchMethodException {
        PropertyResolver resolver = propertyResolverFactory().get(ModelWithMultipleProperties.class);
        assertEquals(fourthSetter(), resolver.findSetter(fourthGetter(),
                Arrays.asList(firstSetter(), secondSetter(), fourthSetter())));
    }

    @Test
    void testFindSetterPresent() throws NoSuchMethodException {
        PropertyResolver resolver = propertyResolverFactory().get(ModelWithMultipleProperties.class);
        assertEquals(secondSetter(), resolver.findSetter(secondGetter(),
                Arrays.asList(firstSetter(), secondSetter(), fourthSetter())));
    }

    @Test
    void testFindSetterMissing() throws NoSuchMethodException {
        PropertyResolver resolver = propertyResolverFactory().get(ModelWithMultipleProperties.class);
        assertNull(resolver.findSetter(secondGetter(), Arrays.asList(firstSetter(), fourthSetter())));
    }

    @Test
    void testIsGetterTrue() throws NoSuchMethodException {
        PropertyResolver resolver = propertyResolverFactory().get(ModelWithMultipleProperties.class);
        assertTrue(resolver.isGetter(firstGetter()));
        assertTrue(resolver.isGetter(secondGetter()));
    }

    @Test
    void testIsGetterFalse() throws NoSuchMethodException {
        PropertyResolver resolver = propertyResolverFactory().get(ModelWithMultipleProperties.class);
        assertFalse(resolver.isGetter(helloMethod()));
    }

    @Test
    void testGetAttributeName() throws NoSuchMethodException {
        PropertyResolver resolver = propertyResolverFactory().get(ModelWithMultipleProperties.class);
        assertEquals(secondAttributeName(), resolver.getAttributeName(secondGetter()));
    }

    @Test
    void testGetBooleanAttributeName() throws NoSuchMethodException {
        PropertyResolver resolver = propertyResolverFactory().get(ModelWithMultipleProperties.class);
        assertEquals(fourthAttributeName(), resolver.getAttributeName(fourthGetter()));
    }

    private Method firstGetter() throws NoSuchMethodException {
        return ModelWithMultipleProperties.class.getDeclaredMethod("getMyFirstLife");
    }

    private Method secondGetter() throws NoSuchMethodException {
        return ModelWithMultipleProperties.class.getDeclaredMethod("getMySecondLife");
    }

    private Method thirdGetter() throws NoSuchMethodException {
        return ModelWithMultipleProperties.class.getDeclaredMethod("getMYThirdLife");
    }

    private Method fourthGetter() throws NoSuchMethodException {
        return ModelWithMultipleProperties.class.getDeclaredMethod("isMyFourthLife");
    }

    private Method firstSetter() throws NoSuchMethodException {
        return ModelWithMultipleProperties.class.getDeclaredMethod("setMyFirstLife", String.class);
    }

    private Method secondSetter() throws NoSuchMethodException {
        return ModelWithMultipleProperties.class.getDeclaredMethod("setMySecondLife", String.class);
    }

    private Method fourthSetter() throws NoSuchMethodException {
        return ModelWithMultipleProperties.class.getDeclaredMethod("setMyFourthLife", boolean.class);
    }

    private Method helloMethod() throws NoSuchMethodException {
        return ModelWithMultipleProperties.class.getDeclaredMethod("hello");
    }

    protected abstract String secondAttributeName();
    protected abstract String secondAttributeNameIncorrect();
    protected abstract String thirdAttributeName();
    protected abstract String thirdAttributeNameIncorrect();
    protected abstract String fourthAttributeName();
    protected abstract PropertyResolverFactory propertyResolverFactory();
}
