package com.linh.wiinav.modules;

import com.linh.wiinav.models.Place;

import java.util.List;

public interface PlacesFinderListenter {
    void onPlacesFinderStart();

    void onPlacesFinderSuccess(List<Place> places);
}
