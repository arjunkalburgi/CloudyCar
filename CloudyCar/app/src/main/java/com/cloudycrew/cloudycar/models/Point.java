package com.cloudycrew.cloudycar.models;

import java.io.Serializable;

/**
 * Created by George on 2016-10-13.
 */

public class Point implements Serializable {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Point point = (Point) o;

        if (Double.compare(point.getLongitude(), getLongitude()) != 0) return false;
        return Double.compare(point.getLatitude(), getLatitude()) == 0;

    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        temp = Double.doubleToLongBits(getLongitude());
        result = (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(getLatitude());
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }
    @Override
    public String toString(){
        return String.format("Long: %5f, Lat: %5f",this.longitude,this.latitude);
    }
}
