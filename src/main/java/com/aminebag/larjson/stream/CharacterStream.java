package com.aminebag.larjson.stream;

import java.io.IOException;

public interface CharacterStream {
    public char nextChar() throws IOException;
    public boolean hasAtLeastRemainingCharacters(int chars) throws IOException;
}
