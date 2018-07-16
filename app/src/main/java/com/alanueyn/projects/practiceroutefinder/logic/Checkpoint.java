package com.alanueyn.projects.practiceroutefinder.logic;

public class Checkpoint {

    private static final double EARTH_EQUATORIAL_RADIUS = 6378.1370D;
    private static final double CONVERT_DEGREES_TO_RADIANS = Math.PI / 180D;
    public static final double CONVERT_RADIANS_TO_DEGREES = 180D / Math.PI;

    public void setLongtitude(double longtitude) {
        this.longtitude = longtitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    private double longtitude;
    private double latitude;
    private String name;

    public Checkpoint(Double longtitude, Double latitude, String name) {
        this.longtitude = longtitude * CONVERT_DEGREES_TO_RADIANS;
        this.latitude = latitude * CONVERT_DEGREES_TO_RADIANS;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public double getLongtitude() {
        return longtitude;
    }

    public double getLatitude() {
        return latitude;
    }

    @Override
    public String toString() {
        return getName();
    }

    public double measureDistance(Checkpoint checkpoint) {
        double deltaLat = checkpoint.getLatitude() - this.getLatitude();
        double deltaLong = checkpoint.getLongtitude() - this.getLongtitude();
        double a = Math.pow(Math.sin(deltaLat / 2D), 2D) +
                Math.cos(this.getLatitude()) * Math.cos(checkpoint.getLatitude()) * Math.pow(Math.sin(deltaLong / 2D), 2D);
        return EARTH_EQUATORIAL_RADIUS * 2D * Math.atan2(Math.sqrt(a), Math.sqrt(1D - a));
    }
}