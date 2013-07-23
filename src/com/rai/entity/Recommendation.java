package com.rai.entity;

/**
 * Created with IntelliJ IDEA.
 * User: igobrilhante
 * Date: 23/07/13
 * Time: 10:29
 * To change this template use File | Settings | File Templates.
 */
public class Recommendation {
    private double rating;
    private Venue venue;

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public Venue getVenue() {
        return venue;
    }

    public void setVenue(Venue venue) {
        this.venue = venue;
    }

}
