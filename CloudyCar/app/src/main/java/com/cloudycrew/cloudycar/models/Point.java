package com.cloudycrew.cloudycar.models;

import com.cloudycrew.cloudycar.GeoDecoder;

import java.io.Serializable;

/**
 * Created by George on 2016-10-13.
 */
public class Point implements Serializable {
    /**
     * Gets longitude.
     *
     * @return the longitude
     */
    public double getLongitude() {
        return longitude;
    }

    /**
     * Gets latitude.
     *
     * @return the latitude
     */
    public double getLatitude() {
        return latitude;
    }

    private String description;
    private double longitude;
    private double latitude;

    /**
     * Instantiates a new Point.
     *
     * @param longitude   the longitude
     * @param latitude    the latitude
     * @param description the description
     */
    public Point(double longitude, double latitude, String description) {
        this.latitude=latitude;
        this.longitude=longitude;
        if(description.length() > 1){
            this.description = description;
        }else{
            this.description = this.toString();
        }
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

    /**
     * Gets description.
     *
     * @return the description
     */
    public String getDescription() {
        return description;
    }
}
