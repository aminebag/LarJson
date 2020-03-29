package com.aminebag.larjson.benchmark.model.gson;

import java.util.List;

/**
 * @author Amine Bagdouri
 */
public class GsonBeer {
    private String firstBrewed;
    private double attenuationLevel;
    private double targetOg;
    private String imageUrl;
    private double ebc;
    private String description;
    private double targetFg;
    private double srm;
    private String contributedBy;
    private double abv;
    private String name;
    private float ph;
    private String tagline;
    private int id;
    private double ibu;
    private String brewersTips;
    private GsonMeasure boilVolume;
    private GsonMeasure volume;
    private List<String> foodPairing;
    private GsonBeerMethod method;
    private GsonIngredients ingredients;

    public String getFirstBrewed() {
        return firstBrewed;
    }

    public void setFirstBrewed(String firstBrewed) {
        this.firstBrewed = firstBrewed;
    }

    public double getAttenuationLevel() {
        return attenuationLevel;
    }

    public void setAttenuationLevel(double attenuationLevel) {
        this.attenuationLevel = attenuationLevel;
    }

    public double getTargetOg() {
        return targetOg;
    }

    public void setTargetOg(double targetOg) {
        this.targetOg = targetOg;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public double getEbc() {
        return ebc;
    }

    public void setEbc(double ebc) {
        this.ebc = ebc;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getTargetFg() {
        return targetFg;
    }

    public void setTargetFg(double targetFg) {
        this.targetFg = targetFg;
    }

    public double getSrm() {
        return srm;
    }

    public void setSrm(double srm) {
        this.srm = srm;
    }

    public String getContributedBy() {
        return contributedBy;
    }

    public void setContributedBy(String contributedBy) {
        this.contributedBy = contributedBy;
    }

    public double getAbv() {
        return abv;
    }

    public void setAbv(double abv) {
        this.abv = abv;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getPh() {
        return ph;
    }

    public void setPh(float ph) {
        this.ph = ph;
    }

    public String getTagline() {
        return tagline;
    }

    public void setTagline(String tagline) {
        this.tagline = tagline;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getIbu() {
        return ibu;
    }

    public void setIbu(double ibu) {
        this.ibu = ibu;
    }

    public String getBrewersTips() {
        return brewersTips;
    }

    public void setBrewersTips(String brewersTips) {
        this.brewersTips = brewersTips;
    }

    public GsonMeasure getBoilVolume() {
        return boilVolume;
    }

    public void setBoilVolume(GsonMeasure boilVolume) {
        this.boilVolume = boilVolume;
    }

    public GsonMeasure getVolume() {
        return volume;
    }

    public void setVolume(GsonMeasure volume) {
        this.volume = volume;
    }

    public List<String> getFoodPairing() {
        return foodPairing;
    }

    public void setFoodPairing(List<String> foodPairing) {
        this.foodPairing = foodPairing;
    }

    public GsonBeerMethod getMethod() {
        return method;
    }

    public void setMethod(GsonBeerMethod method) {
        this.method = method;
    }

    public GsonIngredients getIngredients() {
        return ingredients;
    }

    public void setIngredients(GsonIngredients ingredients) {
        this.ingredients = ingredients;
    }
}
