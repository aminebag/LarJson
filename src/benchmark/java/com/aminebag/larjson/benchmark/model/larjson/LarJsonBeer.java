package com.aminebag.larjson.benchmark.model.larjson;

import java.util.List;

/**
 * @author Amine Bagdouri
 */
public interface LarJsonBeer {
    String getFirstBrewed();
    double getAttenuationLevel();
    double getTargetOg();
    String getImageUrl();
    double getEbc();
    String getDescription();
    double getTargetFg();
    double getSrm();
    String getContributedBy();
    double getAbv();
    String getName();
    float getPh();
    String getTagline();
    int getId();
    double getIbu();
    String getBrewersTips();
    LarJsonMeasure getBoilVolume();
    LarJsonMeasure getVolume();
    List<String> getFoodPairing();
    LarJsonBeerMethod getMethod();
    LarJsonIngredients getIngredients();

}
