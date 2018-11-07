package com.linh.wiinav.view.module;

import com.linh.wiinav.view.model.Route;

import java.util.List;

public interface DirectionFinderListener {
    void onDirectionFinderStart();
    void onDirectionFinderSuccess(List<Route> route);
}
