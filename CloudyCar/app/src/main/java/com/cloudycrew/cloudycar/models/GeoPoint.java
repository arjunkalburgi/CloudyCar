package com.cloudycrew.cloudycar.models;

import java.io.Serializable;

/**
 * Created by George on 2016-11-26.
 */

public class GeoPoint implements Serializable {
    private double lat;
    private double lon;

    /**
     * Instantiate the GeoPoint
     * @param lat Double representing the latitude
     * @param lon Double representing the longitude
     */
    public GeoPoint(double lat, double lon) {
        this.lat = lat;
        this.lon = lon;
    }

    /**
     * Get the latitude
     * @return - Double representing the latitude
     */
    public double getLat() {
        return lat;
    }

    /**
     * Set the latitude
     * @param lat - latitude to set
     */
    public void setLat(double lat) {
        this.lat = lat;
    }

    /**
     * Get the longitude
     * @return - Double representing the longitude
     */
    public double getLon() {
        return lon;
    }

    /**
     * Set the longitude
     * @param lon - longitude to set
     */
    public void setLon(double lon) {
        this.lon = lon;
    }
}
