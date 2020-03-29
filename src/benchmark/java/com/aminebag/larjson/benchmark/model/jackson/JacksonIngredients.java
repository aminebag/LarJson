package com.aminebag.larjson.benchmark.model.jackson;

import java.util.List;

/**
 * @author Amine Bagdouri
 */
public class JacksonIngredients {
    private String yeast;
    private List<JacksonHop> hops;
    private List<JacksonMalt> malt;

    public String getYeast() {
        return yeast;
    }

    public void setYeast(String yeast) {
        this.yeast = yeast;
    }

    public List<JacksonHop> getHops() {
        return hops;
    }

    public void setHops(List<JacksonHop> hops) {
        this.hops = hops;
    }

    public List<JacksonMalt> getMalt() {
        return malt;
    }

    public void setMalt(List<JacksonMalt> malt) {
        this.malt = malt;
    }
}
