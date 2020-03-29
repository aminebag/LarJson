package com.aminebag.larjson.mapper.valueoverwriter;

/**
 * @author Amine Bagdouri
 */
public class ConcurrentValueOverwriterTest extends ValueOverwriterTest {
    @Override
    protected ValueOverwriter valueOverwriter() {
        return new ConcurrentValueOverwriter();
    }
}
