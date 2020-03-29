package com.aminebag.larjson.benchmark.model.gson;

import java.util.List;

/**
 * @author Amine Bagdouri
 */
public class GsonBeerMethod {
    private String twist;
    private List<GsonMashTemp> mashTemp;
    private GsonFermentation fermentation;

    public String getTwist() {
        return twist;
    }

    public void setTwist(String twist) {
        this.twist = twist;
    }

    public List<GsonMashTemp> getMashTemp() {
        return mashTemp;
    }

    public void setMashTemp(List<GsonMashTemp> mashTemp) {
        this.mashTemp = mashTemp;
    }

    public GsonFermentation getFermentation() {
        return fermentation;
    }

    public void setFermentation(GsonFermentation fermentation) {
        this.fermentation = fermentation;
    }
}
