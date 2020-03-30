package com.aminebag.larjson.parser;

import java.util.List;

abstract class LarJsonRootObject {
    List[] lists;

    abstract LarJsonRootObject setPosition(int index, long position);
    abstract LarJsonRootObject setList(int index, long position);

}
