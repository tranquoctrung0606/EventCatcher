package com.linh.wiinav.view.ui;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.linh.wiinav.R;
import com.linh.wiinav.view.model.ReportedData;

public class CustomInfoWindowGoogleMap implements GoogleMap.InfoWindowAdapter {
    private Context context;
    public CustomInfoWindowGoogleMap(Context context){
        this.context = context;
    }

    @Override
    public View getInfoWindow(Marker marker) {
//        ReportedData infoWindowData = (ReportedData) marker.getTag();
//        if (infoWindowData != null) {
//            if ("problem".equals(infoWindowData.getType())){
//                View view = ((Activity) context).getLayoutInflater()
//                        .inflate(R.layout.info_problem_report, null);
//
//                TextView tvTitle = view.findViewById(R.id.title);
//                TextView tvSnippet = view.findViewById(R.id.snippet);
//
//                tvTitle.setText(marker.getTitle());
//                tvSnippet.setText(marker.getSnippet());
//                return view;
//            }
//        }
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        ReportedData reportedData = (ReportedData) marker.getTag();
        if(reportedData != null) {
            if ("problem".equals(reportedData.getType())) {
                View view = ((Activity) context).getLayoutInflater()
                        .inflate(R.layout.info_problem_report, null);
                TextView tvTitle = view.findViewById(R.id.title);
                TextView tvSnippet = view.findViewById(R.id.snippet);

                tvTitle.setText(marker.getTitle());
                tvSnippet.setText(marker.getSnippet());
                return view;
            }
        }
        return null;
    }


}
