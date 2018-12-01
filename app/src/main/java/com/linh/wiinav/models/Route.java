package com.linh.wiinav.models;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

public class Route implements Comparable<Route>{
    private Distance distance;
    private Duration duration;
    private String endAddress;
    private LatLng endLocation;
    private String startAddress;
    private LatLng startLocation;

    private List<LatLng> points;
    private List<LatLng> intersectionCoordinate;

    public Route() {
    }

    public Route(Distance distance, Duration duration,
                 String endAddress, LatLng endLocation, String startAddress,
                 LatLng startLocation, List<LatLng> points, List<LatLng> intersectionCoordinate) {
        this.distance = distance;
        this.duration = duration;
        this.endAddress = endAddress;
        this.endLocation = endLocation;
        this.startAddress = startAddress;
        this.startLocation = startLocation;
        this.points = points;
        this.intersectionCoordinate = intersectionCoordinate;
    }

    @Override
    public String toString() {
        return "Route{" +
                "distance=" + distance +
                ", duration=" + duration +
                ", endAddress='" + endAddress + '\'' +
                ", endLocation=" + endLocation +
                ", startAddress='" + startAddress + '\'' +
                ", startLocation=" + startLocation +
                ", points=" + points +
                '}';
    }

    public Distance getDistance() {
        return distance;
    }

    public void setDistance(Distance distance) {
        this.distance = distance;
    }

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public String getEndAddress() {
        return endAddress;
    }

    public void setEndAddress(String endAddress) {
        this.endAddress = endAddress;
    }

    public LatLng getEndLocation() {
        return endLocation;
    }

    public void setEndLocation(LatLng endLocation) {
        this.endLocation = endLocation;
    }

    public String getStartAddress() {
        return startAddress;
    }

    public void setStartAddress(String startAddress) {
        this.startAddress = startAddress;
    }

    public LatLng getStartLocation() {
        return startLocation;
    }

    public void setStartLocation(LatLng startLocation) {
        this.startLocation = startLocation;
    }

    public List<LatLng> getPoints() {
        return points;
    }

    public void setPoints(List<LatLng> points) {
        this.points = points;
    }

    public List<LatLng> getIntersectionCoordinate() {
        return intersectionCoordinate;
    }

    public void setIntersectionCoordinate(List<LatLng> intersectionCoordinate) {
        this.intersectionCoordinate = intersectionCoordinate;
    }

    @Override
    public int compareTo(Route o) {
        return this.getDuration().compareTo(o.getDuration());
    }
}
