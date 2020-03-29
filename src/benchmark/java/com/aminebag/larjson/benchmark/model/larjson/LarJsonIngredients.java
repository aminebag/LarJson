package com.aminebag.larjson.benchmark.model.larjson;

import java.util.List;

/**
 * @author Amine Bagdouri
 */
public interface LarJsonIngredients {
    String getYeast();
    List<LarJsonHop> getHops();
    List<LarJsonMalt> getMalt();
}
