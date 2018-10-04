package com.linh.wiinav.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.linh.wiinav.R;

public class CustomInfoWindowGoogleMap implements GoogleMap.InfoWindowAdapter {
    private Context context;
    public CustomInfoWindowGoogleMap(Context context){
        this.context = context;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        InfoWindowData infoWindowData = (InfoWindowData) marker.getTag();
        View view = null;
        if ("problem".equals(infoWindowData.getType())){
            view = ((Activity) context).getLayoutInflater()
                    .inflate(R.layout.info_problem_report, null);

            TextView tvTitle = view.findViewById(R.id.title);
            TextView tvSnippet = view.findViewById(R.id.snippet);
            Button btnCall = view.findViewById(R.id.btnCall);

            tvTitle.setText(marker.getTitle());
            tvSnippet.setText(marker.getSnippet());

            btnCall.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(Intent.ACTION_CALL);
                intent.setData(Uri.parse("tel:" + "0906447095"));
                try{
                    context.startActivity(intent);
                }
                catch (android.content.ActivityNotFoundException ex){
                    Toast.makeText(context,"yourActivity is not founded",Toast.LENGTH_SHORT).show();
                }
                }
            });
        }
        return view;
    }


}
