package com.aminebag.larjson.jsonmapper;

import com.aminebag.larjson.jsonmapper.configuration.LarJsonConfiguration;

import java.io.Closeable;

public interface LarJsonRoot extends LarJsonBranch, Closeable {
    LarJsonConfiguration getConfiguration();
}
