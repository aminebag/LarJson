package com.aminebag.larjson.benchmark.model.gson;

import java.util.List;

/**
 * @author Amine Bagdouri
 */
public class GsonIngredients {
    private String yeast;
    private List<GsonHop> hops;
    private List<GsonMalt> malt;

    public String getYeast() {
        return yeast;
    }

    public void setYeast(String yeast) {
        this.yeast = yeast;
    }

    public List<GsonHop> getHops() {
        return hops;
    }

    public void setHops(List<GsonHop> hops) {
        this.hops = hops;
    }

    public List<GsonMalt> getMalt() {
        return malt;
    }

    public void setMalt(List<GsonMalt> malt) {
        this.malt = malt;
    }
}
