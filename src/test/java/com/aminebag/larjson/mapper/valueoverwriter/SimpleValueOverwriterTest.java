package com.aminebag.larjson.mapper.valueoverwriter;

/**
 * @author Amine Bagdouri
 */
public class SimpleValueOverwriterTest extends ValueOverwriterTest {
    @Override
    protected ValueOverwriter valueOverwriter() {
        return new SimpleValueOverwriter();
    }
}
