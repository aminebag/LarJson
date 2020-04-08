package com.aminebag.larjson.jsonmapper;

import com.aminebag.larjson.stream.CharacterStream;

import java.io.IOException;
import java.util.List;

public interface LarJsonBranch {
    CharacterStream getCharacterStream(long position) throws IOException;
    long getKey(int index);
    List<?> getList(int index);
    int getTopObjectOffset();
    LarJsonRoot getRoot();
}
