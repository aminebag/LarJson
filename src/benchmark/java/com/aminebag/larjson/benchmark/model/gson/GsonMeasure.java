package com.aminebag.larjson.benchmark.model.gson;

import com.aminebag.larjson.benchmark.model.common.Unit;

/**
 * @author Amine Bagdouri
 */
public class GsonMeasure {
    private Unit unit;
    private double value;

    public Unit getUnit() {
        return unit;
    }

    public void setUnit(Unit unit) {
        this.unit = unit;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }
}
