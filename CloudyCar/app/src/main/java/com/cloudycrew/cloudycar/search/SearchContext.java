package com.cloudycrew.cloudycar.search;

/**
 * Created by George on 2016-11-26.
 */

public class SearchContext {
    private String keyword;
    private double lat = -1;
    private double lon = -1;
    private double radius = -1;
    private double minPricePerKm = -1;
    private double maxPricePerKm = -1;

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

    public SearchContext withPricePerKm(double minPricePerKm, double maxPricePerKm) {
        this.minPricePerKm = minPricePerKm;
        this.maxPricePerKm = maxPricePerKm;
        return this;
    }

    public boolean hasKeyword() {
        return keyword != null;
    }

    public boolean hasLocation() {
        return radius != -1;
    }

    public boolean hasPricePerKm() {
        return minPricePerKm != -1;
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

    public double getMinPricePerKm() {
        return minPricePerKm;
    }

    public double getMaxPricePerKm() {
        return maxPricePerKm;
    }
}
