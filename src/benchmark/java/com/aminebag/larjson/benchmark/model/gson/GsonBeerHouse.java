package com.aminebag.larjson.benchmark.model.gson;

import java.util.List;

/**
 * @author Amine Bagdouri
 */
public class GsonBeerHouse {
    private List<GsonBeerGroup> beers;

    public List<GsonBeerGroup> getBeers() {
        return beers;
    }

    public void setBeers(List<GsonBeerGroup> beers) {
        this.beers = beers;
    }
}
