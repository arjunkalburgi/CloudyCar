package com.cloudycrew.cloudycar.models;

import java.io.Serializable;

/**
 * Created by George on 2016-10-13.
 */
public class Route implements Serializable{
    private Location start;
    private Location end;
    private long meters;

    /**
     * Instantiates a new Route.
     *
     * @param start the start
     * @param end   the end
     */
    public Route(Location start, Location end){
        this.start=start;
        this.end=end;
    }

    /**
     * Gets starting point.
     *
     * @return the starting point
     */
    public Location getStartingPoint() {
        return this.start;
    }

    /**
     * Gets ending point.
     *
     * @return the ending point
     */
    public Location getEndingPoint() {
        return this.end;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Route route = (Route) o;

        if (start != null ? !start.equals(route.start) : route.start != null) return false;
        if(meters != route.getMeters()) return false;
        return end != null ? end.equals(route.end) : route.end == null;

    }

    @Override
    public int hashCode() {
        int result = start != null ? start.hashCode() : 0;
        result = 31 * result + (end != null ? end.hashCode() : 0);
        return result;
    }

    public long getMeters() {
        return meters;
    }

    public void setMeters(long meters) {
        this.meters = meters;
    }
}
