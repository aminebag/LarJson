package com.aminebag.larjson.mapper.valueoverwriter;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Amine Bagdouri
 */
public abstract class ValueOverwriterTest {

    @Test
    void testConceive() {
        ValueOverwriter valueOverwriter = valueOverwriter();
        valueOverwriter.overwritePropertyValue(15, 3, 7, new Object());
        ValueOverwriter son = valueOverwriter.conceive();
        assertNotNull(son);
        assertTrue(son.isEmpty());
        assertTrue(son.getClass().equals(valueOverwriter.getClass()));
    }

    @Test
    void testIsEmpty() {
        ValueOverwriter valueOverwriter = valueOverwriter();
        assertTrue(valueOverwriter.isEmpty());
        valueOverwriter.overwritePropertyValue(15, 3, 7, new Object());
        assertFalse(valueOverwriter.isEmpty());
    }

    @Test
    void testOverwritePropertyValue() {
        ValueOverwriter valueOverwriter = valueOverwriter();
        Object value = new Object();
        valueOverwriter.overwritePropertyValue(15, 3, 7, value);
        Object overwritten = valueOverwriter.getOverwrittenPropertyValue(15, 3);
        assertTrue(valueOverwriter.isPropertyValueOverwritten(overwritten));
        assertEquals(value, overwritten);
        overwritten = valueOverwriter.getOverwrittenPropertyValue(15, 2);
        assertFalse(valueOverwriter.isPropertyValueOverwritten(overwritten));
        overwritten = valueOverwriter.getOverwrittenPropertyValue(13, 3);
        assertFalse(valueOverwriter.isPropertyValueOverwritten(overwritten));
    }

    @Test
    void testOverwritePropertyValueNull() {
        ValueOverwriter valueOverwriter = valueOverwriter();
        valueOverwriter.overwritePropertyValue(15, 3, 7, null);
        Object overwritten = valueOverwriter.getOverwrittenPropertyValue(15, 3);
        assertTrue(valueOverwriter.isPropertyValueOverwritten(overwritten));
        assertNull(overwritten);
        overwritten = valueOverwriter.getOverwrittenPropertyValue(15, 2);
        assertFalse(valueOverwriter.isPropertyValueOverwritten(overwritten));
        overwritten = valueOverwriter.getOverwrittenPropertyValue(13, 3);
        assertFalse(valueOverwriter.isPropertyValueOverwritten(overwritten));
    }

    @Test
    void testPutList() {
        ValueOverwriter valueOverwriter = valueOverwriter();
        List<?> list1 = Arrays.asList(1, 2, 3);
        List<?> list2 = Arrays.asList(2, 3, 4);
        List<?> list3 = Arrays.asList(3, 4, 5);
        assertTrue(list1 == valueOverwriter.getListOrDefault(17, list1));
        List<?> asserted1 = valueOverwriter.putListIfAbsent(17, list2);
        assertFalse(list2 == asserted1);
        assertEquals(list2, asserted1);
        assertTrue(asserted1 == valueOverwriter.getListOrDefault(17, list1));
        assertTrue(asserted1 == valueOverwriter.putListIfAbsent(17, list3));
        assertTrue(asserted1 == valueOverwriter.getListOrDefault(17, list1));
        assertTrue(list1 == valueOverwriter.getListOrDefault(16, list1));
        asserted1.remove(0);
        assertNotEquals(list2, asserted1);
    }

    protected abstract ValueOverwriter valueOverwriter();
}
