package com.cloudycrew.cloudycar.models;

/**
 * Created by George on 2016-10-13.
 */

public class Point {
    public double getLongitude() {
        return longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    private double longitude;
    private double latitude;
    public Point(double longitude, double latitude) {
        this.latitude=latitude;
        this.longitude=longitude;
    }
}
