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

    private void init(Marker marker) {
        if (marker.getTag() instanceof Report) {
            Report reportedData = (Report) marker.getTag();
            if(reportedData != null) {
                    mWindow = LayoutInflater.from(context).inflate(R.layout.dialog_info_report, null);
                    TextView tvTitle = mWindow.findViewById(R.id.info_report_title);
                    TextView tvDescription = mWindow.findViewById(R.id.info_report_description);
                    TextView tvDownVote = mWindow.findViewById(R.id.info_report_down_vote);
                    TextView tvUpVote = mWindow.findViewById(R.id.info_report_up_vote);
                    TextView tvReporterName = mWindow.findViewById(R.id.info_report_reporter_name);
                    TextView tvRemainingTime = mWindow.findViewById(R.id.info_report_remain_time);

                    tvTitle.setText(reportedData.getTitle());
                    tvDescription.setText(reportedData.getContent());
                    tvReporterName.setText(reportedData.getReporter().getUsername());
                    tvUpVote.setText(reportedData.getUpVote() + "");
                    tvDownVote.setText(reportedData.getDownVote() + "");
                    tvRemainingTime.setText(String.valueOf(reportedData.getRemainingTime()/3600));
            }
        }
    }

    @Override
    public View getInfoWindow(Marker marker) {
        init(marker);
        return mWindow;
    }

    @Override
    public View getInfoContents(Marker marker) {
        init(marker);
        return mWindow;
    }


}
