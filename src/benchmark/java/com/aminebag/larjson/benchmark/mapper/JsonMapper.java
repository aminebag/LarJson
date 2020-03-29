package com.aminebag.larjson.benchmark.mapper;

import java.io.File;
import java.io.Writer;

/**
 * @author Amine Bagdouri
 */
public interface JsonMapper {

    Object readObject(File jsonFile) throws Exception;
    void writeObject(Object object, Writer writer) throws Exception;
    void closeObject(Object object) throws Exception;
}
