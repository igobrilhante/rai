package com.rai.entity;

/**
 * Created with IntelliJ IDEA.
 * User: igobrilhante
 * Date: 23/07/13
 * Time: 10:30
 * To change this template use File | Settings | File Templates.
 */
public class Venue {
    private String id;
    private String name;
    private double distance;
    private String address;
    private double latitude;
    private double longitude;

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
