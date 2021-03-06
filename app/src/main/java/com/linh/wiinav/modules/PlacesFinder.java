package com.linh.wiinav.modules;

import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.linh.wiinav.models.Place;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlacesFinder {
    private static final String TAG = "PlacesFinder";
    private static final String GOOGLE_PLACES_API = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?";
    private static final String GOOGLE_PLACE_KEY = "AIzaSyCP6Jfity_JyX97-2J15cN-_GUaqJv6DPc";
    private static final int RADIUS = 200;
    private PlacesFinderListenter listener;
    private LatLng location;
    private List<String> types;

    public PlacesFinder(PlacesFinderListenter listener, List<String> types, LatLng location) {
        this.listener = listener;
        this.types = types;
        this.location = location;
    }

    private String createUrl() {
        StringBuilder sb = new StringBuilder();
        sb.append(GOOGLE_PLACES_API);
        sb.append("location=");
        sb.append(location.latitude + ",");
        sb.append(location.longitude);
        sb.append("&radius=");
        sb.append(RADIUS);
        sb.append("&types=");
        for (int i = 0; i < types.size(); i++) {
           if (i < types.size() - 1) sb.append(types.get(i) + ",");
           else sb.append(types.get(i));
        }
        sb.append("&key=");
        sb.append(GOOGLE_PLACE_KEY);

        return sb.toString();
    }

    public void execute() {
        listener.onPlacesFinderStart();
        Log.i(TAG, "execute: " + createUrl());
        new DownloadRawData().execute(createUrl());
    }

    private class DownloadRawData extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            String link = params[0];
            InputStream is;
            try {
                URL url = new URL(link);
                is = url.openConnection().getInputStream();
                StringBuilder sb = new StringBuilder();
                BufferedReader reader = new BufferedReader(new InputStreamReader(is));

                String line;
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
                reader.close();
                is.close();
                return sb.toString();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String res) {
            try {
                parseJSon(res);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void parseJSon(String res) throws JSONException{
        if (res == null) {
            return;
        }
        Map<String, Place> places = new HashMap<>();
        JSONObject jsonData = new JSONObject(res);
        JSONArray jsonResults = jsonData.getJSONArray("results");
        for (int i = 0; i < jsonResults.length(); i++) {
            Place place = new Place();
            JSONObject jsonResult = jsonResults.getJSONObject(i);
            JSONObject jsonGeometry = jsonResult.getJSONObject("geometry");
            JSONObject jsonLocation = jsonGeometry.getJSONObject("location");
            JSONArray jsonTypes = jsonResult.getJSONArray("types");
            List<String> types = new ArrayList<>();
            for (int j = 0; j < jsonTypes.length(); j++) {
                types.add(jsonTypes.getString(j));
            }

            place.setIcon(jsonResult.getString("icon"));
            place.setName(jsonResult.getString("name"));
            place.setPlaceId(jsonResult.getString("place_id"));
            //place.setRating(jsonResult.getDouble("rating"));
            place.setLocation(new LatLng(jsonLocation.getDouble("lat"), jsonLocation.getDouble("lng")));
            place.setVicinity(jsonResult.getString("vicinity"));
            place.setTypes(types);

            places.put(place.getPlaceId(), place);
        }
        listener.onPlacesFinderSuccess(places);
    }
}
