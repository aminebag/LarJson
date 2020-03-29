package com.aminebag.larjson.mapper;

import com.aminebag.larjson.configuration.LarJsonTypedReadConfiguration;

/**
 * @author Amine Bagdouri
 */
public class LarJsonTypedMapperThreadSafeCloneTest extends LarJsonTypedMapperCloneTest {

    @Override
    protected LarJsonTypedReadConfiguration.Builder enrichConfigBuilder(LarJsonTypedReadConfiguration.Builder builder) {
        return super.enrichConfigBuilder(builder).setThreadSafe(true);
    }
}
