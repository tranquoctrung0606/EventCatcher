package com.linh.wiinav.models;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

public class Place {
    private LatLng location;
    private String icon;
    private String name;
    private String placeId;
    private Double rating;
    private List<String> types;
    private String vicinity;

    public Place(LatLng location, String icon, String name, String placeId, Double rating, List<String> types, String vicinity) {
        this.location = location;
        this.icon = icon;
        this.name = name;
        this.placeId = placeId;
        this.rating = rating;
        this.types = types;
        this.vicinity = vicinity;
    }

    public Place() {
    }

    public LatLng getLocation() {
        return location;
    }

    public void setLocation(LatLng location) {
        this.location = location;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPlaceId() {
        return placeId;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public List<String> getTypes() {
        return types;
    }

    public void setTypes(List<String> types) {
        this.types = types;
    }

    public String getVicinity() {
        return vicinity;
    }

    public void setVicinity(String vicinity) {
        this.vicinity = vicinity;
    }

    @Override
    public String toString() {
        return "Place{" +
                "location=" + location +
                ", icon='" + icon + '\'' +
                ", name='" + name + '\'' +
                ", placeId='" + placeId + '\'' +
                ", rating=" + rating +
                ", types=" + types +
                ", vicinity='" + vicinity + '\'' +
                '}';
    }
}
