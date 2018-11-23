package com.linh.wiinav.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.linh.wiinav.R;
import com.linh.wiinav.models.Report;

public class CustomInfoWindowGoogleMap implements GoogleMap.InfoWindowAdapter {

    private View mWindow;
    private Context context;

    public CustomInfoWindowGoogleMap(Context context){
        this.context = context;
    }

    private void init(Marker marker, View view) {
        if (marker.getTag() instanceof Report) {
            Report reportedData = (Report) marker.getTag();
            if(reportedData != null) {
                    view = LayoutInflater.from(context).inflate(R.layout.info_report, null);
                    TextView tvTitle = view.findViewById(R.id.info_report_title);
                    TextView tvDescription = view.findViewById(R.id.info_report_description);
                    TextView tvDownVote = view.findViewById(R.id.info_report_down_vote);
                    TextView tvUpVote = view.findViewById(R.id.info_report_up_vote);
                    TextView tvReporterName = view.findViewById(R.id.info_report_reporter_name);
                    TextView tvRemainingTime = view.findViewById(R.id.info_report_remain_time);

                    tvTitle.setText(reportedData.getTitle());
                    tvDescription.setText(reportedData.getContent());
                    tvReporterName.setText(reportedData.getReporter().getUsername());
                    tvUpVote.setText(1 + "");
                    tvDownVote.setText(1 + "");
                    tvRemainingTime.setText(String.valueOf(reportedData.getRemainingTime()/3600));
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
