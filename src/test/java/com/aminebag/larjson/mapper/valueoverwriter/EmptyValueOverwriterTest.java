package com.aminebag.larjson.mapper.valueoverwriter;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Amine Bagdouri
 */
public class EmptyValueOverwriterTest {

    @Test
    void testIsEmpty() {
        ValueOverwriter valueOverwriter = valueOverwriter();
        assertTrue(valueOverwriter.isEmpty());
    }

    @Test
    void testIsPropertyValueOverwritten() {
        ValueOverwriter valueOverwriter = valueOverwriter();
        assertFalse(valueOverwriter.isPropertyValueOverwritten(new Object()));
    }

    @Test
    void testConceive() {
        ValueOverwriter valueOverwriter = valueOverwriter();
        assertTrue(valueOverwriter == valueOverwriter.conceive());
    }

    @Test
    void testGetOverwrittenPropertyValue() {
        ValueOverwriter valueOverwriter = valueOverwriter();
        valueOverwriter.getOverwrittenPropertyValue(12,6);
    }

    @Test
    void testOverwritePropertyValue() {
        ValueOverwriter valueOverwriter = valueOverwriter();
        try {
            valueOverwriter.overwritePropertyValue(15, 3, 7, new Object());
            fail();
        } catch (UnsupportedOperationException expected) {
        }
    }

    @Test
    void testPutListIfAbsent() {
        ValueOverwriter valueOverwriter = valueOverwriter();
        try {
            valueOverwriter.putListIfAbsent(15, Arrays.asList(13, 7));
            fail();
        } catch (UnsupportedOperationException expected) {
        }
    }

    @Test
    void testGetListOrDefault() {
        ValueOverwriter valueOverwriter = valueOverwriter();
        List<?> list = Arrays.asList(7, 2);
        assertTrue(list == valueOverwriter.getListOrDefault(13, list));
    }

    private ValueOverwriter valueOverwriter() {
        return EmptyValueOverwriter.INSTANCE;
    }
}
