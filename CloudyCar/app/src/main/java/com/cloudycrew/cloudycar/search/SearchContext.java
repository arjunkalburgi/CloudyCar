package com.cloudycrew.cloudycar.search;

import java.io.Serializable;

import static com.cloudycrew.cloudycar.utils.StringUtils.isNullOrEmpty;

/**
 * Created by George on 2016-11-26.
 */

/**
 * Used to store information that will be used to construct an elasticsearch query. Acts strictly as
 * a model, with no modifications made to data stored within in.
 */
public class SearchContext implements Serializable {
    private String keyword;
    private double lat = -1;
    private double lon = -1;
    private int radius = -1;
    private double minPrice = -1;
    private double minPricePerKm = -1;

    /**
     * Creates a search context with a keyword
     *
     * @param keyword the keyword
     * @return The search context with a keyword
     */
    public SearchContext withKeyword(String keyword) {
        this.keyword = keyword;
        return this;
    }

    /**
     * Creates a search context with a location and radius to search
     *
     * @param lat latitude of location
     * @param lon longittude of location
     * @param radius radius to search
     * @return the search context
     */
    public SearchContext withLocation(double lat, double lon, int radius) {
        this.lat = lat;
        this.lon = lon;
        this.radius = radius;
        return this;
    }

    /**
     * Creates a search context with a price per kilometre
     *
     * @param minPricePerKm the minimum price per kilometre
     * @return the search context
     */
    public SearchContext withPricePerKm(double minPricePerKm) {
        this.minPricePerKm = minPricePerKm;
        return this;
    }

    /**
     * Creates a search context with a price
     *
     * @param minPrice the minimum price
     * @return the search context
     */
    public SearchContext withPrice(double minPrice) {
        this.minPrice = minPrice;
        return this;
    }

    /**
     * Determines if this search context contains a keyword
     *
     * @return true if it has a keyword false otherwise
     */
    public boolean hasKeyword() {
        return !isNullOrEmpty(keyword);
    }

    /**
     * Determines if this search context contains a location
     *
     * @return true if it has a location false otherwise
     */
    public boolean hasLocation() {
        return radius != -1;
    }

    /**
     * Determines if this search context has a price
     *
     * @return true if it has a price false otherwise
     */
    public boolean hasPrice() {
        return minPrice != -1;
    }

    /**
     * Determines if this search context has a price per kilometre
     *
     * @return true if it has a price per kilometre false otherwise
     */
    public boolean hasPricePerKm() {
        return minPricePerKm != -1;
    }

    /**
     * Gets the keyword from the search context
     *
     * @return the keyword
     */
    public String getKeyword() {
        return keyword;
    }

    /**
     * Gets the latitude from the search context
     *
     * @return the latitude
     */
    public double getLat() {
        return lat;
    }

    /**
     * Gets the longitude from the search context
     *
     * @return the longitude
     */
    public double getLon() {
        return lon;
    }

    /**
     * Gets the radius from the search context
     *
     * @return the radius
     */
    public int getRadius() {
        return radius;
    }

    /**
     * Gets the minimum price from the search context
     *
     * @return the minimum price
     */
    public double getMinPrice() {
        return minPrice;
    }

    /**
     * Gets the minimum price per kilometre from the search context
     *
     * @return the minimum price per kilometre
     */
    public double getMinPricePerKm() {
        return minPricePerKm;
    }
}
