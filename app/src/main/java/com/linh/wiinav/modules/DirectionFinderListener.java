package com.linh.wiinav.modules;

import com.linh.wiinav.models.Route;

import java.util.List;

public interface DirectionFinderListener {
    void onDirectionFinderStart();
    void onDirectionFinderSuccess(List<Route> route);
}
