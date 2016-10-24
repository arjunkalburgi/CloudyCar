package com.cloudycrew.cloudycar.models;

/**
 * Created by George on 2016-10-13.
 */

public class Route {
    private Point start;
    private Point end;
    public Route(Point start,Point end){
        this.start=start;
        this.end=end;
    }

    public Point getStartingPoint() {
        return this.start;
    }

    public Point getEndingPoint() {
        return this.end;
    }
}
