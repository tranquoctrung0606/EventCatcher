package com.linh.wiinav.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.linh.wiinav.R;
import com.linh.wiinav.models.ReportedData;
import com.linh.wiinav.models.Route;

public class CustomInfoWindowGoogleMap implements GoogleMap.InfoWindowAdapter {

    private View mWindow;
    private Context context;

    public CustomInfoWindowGoogleMap(Context context){
        this.context = context;
    }

    private void init(Marker marker, View view) {
        if (marker.getTag() instanceof Route) {
            Route route = (Route) marker.getTag();
            if (route != null) {
                mWindow = LayoutInflater.from(context).inflate(R.layout.info_route, null);

                TextView routeTitle = mWindow.findViewById(R.id.routeTitle);
                TextView routeDistance = mWindow.findViewById(R.id.routeDistance);
                TextView routeDuration = mWindow.findViewWithTag(R.id.routeDuration);

                routeTitle.setText("");
                routeDistance.setText(route.distance.text);
                routeDuration.setText(route.duration.text);
            }
        }

        if (marker.getTag() instanceof ReportedData) {
            ReportedData reportedData = (ReportedData) marker.getTag();
            if(reportedData != null) {
                if ("problem".equals(reportedData.getType())) {
                    mWindow = LayoutInflater.from(context).inflate(R.layout.info_problem_report, null);
                    TextView tvTitle = mWindow.findViewById(R.id.title);
                    TextView tvSnippet = mWindow.findViewById(R.id.snippet);

                    tvTitle.setText(marker.getTitle());
                    tvSnippet.setText(marker.getSnippet());
                }
            }
        }
    }

    @Override
    public View getInfoWindow(Marker marker) {
        init(marker, mWindow);
        return mWindow;
    }

    @Override
    public View getInfoContents(Marker marker) {
        init(marker, mWindow);
        return mWindow;
    }


}
