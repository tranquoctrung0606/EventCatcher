package com.linh.wiinav.modules;

import com.linh.wiinav.models.Place;

import java.util.Map;

public interface PlacesFinderListenter {
    void onPlacesFinderStart();

    void onPlacesFinderSuccess(Map<String,Place> places);
}
