package com.aminebag.larjson.benchmark.model.jackson;

/**
 * @author Amine Bagdouri
 */
public class JacksonHop {
    private String add;
    private JacksonMeasure amount;
    private String name;
    private String attribute;

    public String getAdd() {
        return add;
    }

    public void setAdd(String add) {
        this.add = add;
    }

    public JacksonMeasure getAmount() {
        return amount;
    }

    public void setAmount(JacksonMeasure amount) {
        this.amount = amount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAttribute() {
        return attribute;
    }

    public void setAttribute(String attribute) {
        this.attribute = attribute;
    }
}
