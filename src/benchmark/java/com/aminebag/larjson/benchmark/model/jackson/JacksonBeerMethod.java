package com.aminebag.larjson.benchmark.model.jackson;

import java.util.List;

/**
 * @author Amine Bagdouri
 */
public class JacksonBeerMethod {
    private String twist;
    private List<JacksonMashTemp> mashTemp;
    private JacksonFermentation fermentation;

    public String getTwist() {
        return twist;
    }

    public void setTwist(String twist) {
        this.twist = twist;
    }

    public List<JacksonMashTemp> getMashTemp() {
        return mashTemp;
    }

    public void setMashTemp(List<JacksonMashTemp> mashTemp) {
        this.mashTemp = mashTemp;
    }

    public JacksonFermentation getFermentation() {
        return fermentation;
    }

    public void setFermentation(JacksonFermentation fermentation) {
        this.fermentation = fermentation;
    }
}
