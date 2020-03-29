package com.aminebag.larjson.benchmark.model.jackson;

import java.util.List;

/**
 * @author Amine Bagdouri
 */
public class JacksonBeerHouse {
    private List<JacksonBeerGroup> beers;

    public List<JacksonBeerGroup> getBeers() {
        return beers;
    }

    public void setBeers(List<JacksonBeerGroup> beers) {
        this.beers = beers;
    }
}
