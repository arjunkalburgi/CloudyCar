package com.cloudycrew.cloudycar.models;

import java.io.Serializable;

/**
 * Created by George on 2016-10-13.
 */
public class Location implements Serializable {
    private String description;
    private GeoPoint point;

    /**
     * Instantiates a new Location.
     *
     * @param longitude   the longitude
     * @param latitude    the latitude
     * @param description the description
     */
    public Location(double longitude, double latitude, String description) {
        this.point = new GeoPoint(latitude, longitude);
        if(description.length() > 1){
            this.description = description;
        }else{
            this.description = this.toString();
        }
    }

    /**
     * Gets longitude.
     *
     * @return the longitude
     */
    public double getLongitude() {
        return point.getLon();
    }

    /**
     * Gets latitude.
     *
     * @return the latitude
     */
    public double getLatitude() {
        return point.getLat();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Location location = (Location) o;

        if (Double.compare(location.getLongitude(), getLongitude()) != 0) return false;
        return Double.compare(location.getLatitude(), getLatitude()) == 0;

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
        return String.format("Long: %5f, Lat: %5f", getLongitude(), getLatitude());
    }

    /**
     * Gets description.
     *
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Set the description
     *
     * @param description the description
     */
    public void setDescription(String description) {
        this.description = description;
    }
}
