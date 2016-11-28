package com.cloudycrew.cloudycar.search;

import java.io.Serializable;

import static com.cloudycrew.cloudycar.utils.StringUtils.isNullOrEmpty;

/**
 * Created by George on 2016-11-26.
 */

public class SearchContext implements Serializable {
    private String keyword;
    private double lat = -1;
    private double lon = -1;
    private int radius = -1;
    private double minPrice = -1;
    private double minPricePerKm = -1;

    public SearchContext withKeyword(String keyword) {
        this.keyword = keyword;
        return this;
    }

    public SearchContext withLocation(double lat, double lon, int radius) {
        this.lat = lat;
        this.lon = lon;
        this.radius = radius;
        return this;
    }

    public SearchContext withPricePerKm(double minPricePerKm) {
        this.minPricePerKm = minPricePerKm;
        return this;
    }

    public SearchContext withPrice(double minPrice) {
        this.minPrice = minPrice;
        return this;
    }

    public boolean hasKeyword() {
        return !isNullOrEmpty(keyword);
    }

    public boolean hasLocation() {
        return radius != -1;
    }

    public boolean hasPrice() {
        return minPrice != -1;
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

    public int getRadius() {
        return radius;
    }

    public double getMinPrice() {
        return minPrice;
    }

    public double getMinPricePerKm() {
        return minPricePerKm;
    }
}
