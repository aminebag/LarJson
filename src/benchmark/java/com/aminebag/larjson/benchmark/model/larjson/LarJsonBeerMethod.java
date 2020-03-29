package com.aminebag.larjson.benchmark.model.larjson;

import java.util.List;

/**
 * @author Amine Bagdouri
 */
public interface LarJsonBeerMethod {
    String getTwist();
    List<LarJsonMashTemp> getMashTemp();
    LarJsonFermentation getFermentation();
}
