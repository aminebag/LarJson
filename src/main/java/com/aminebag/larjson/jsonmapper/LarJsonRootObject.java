package com.aminebag.larjson.jsonmapper;

import java.util.List;

abstract class LarJsonRootObject {
    List[] lists;

    abstract LarJsonRootObject setPosition(int index, long position);
    abstract LarJsonRootObject setList(int index, long position);

}
