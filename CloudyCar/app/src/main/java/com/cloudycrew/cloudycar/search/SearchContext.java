package com.cloudycrew.cloudycar.search;

/**
 * Created by George on 2016-11-26.
 */

public class SearchContext {
    private String keyword;
    private double lat;
    private double lon;
    private double radius;
    private double pricePerKm;

    public SearchContext withKeyword(String keyword) {
        this.keyword = keyword;
        return this;
    }

    public SearchContext withLocation(double lat, double lon, double radius) {
        this.lat = lat;
        this.lon = lon;
        this.radius = radius;
        return this;
    }

    public SearchContext withPricePerKm(double pricePerKm) {
        this.pricePerKm = pricePerKm;
        return this;
    }

    public String getKeyword() {
        return keyword;
    }

    public double getLat() {
        return lat;
    }

    public double getLon() {
        return lon;
    }

    public double getRadius() {
        return radius;
    }

    public double getPricePerKm() {
        return pricePerKm;
    }
}
