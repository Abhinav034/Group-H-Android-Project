package com.example.grouphfinalproject;

import android.graphics.Color;
import android.os.AsyncTask;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.PolyUtil;

import java.io.IOException;
import java.util.HashMap;

public class GetRouteData extends AsyncTask<Object , String , String> {

        String directionsData;
        GoogleMap map;
        String url;

        String distance;
        String duration;

        LatLng latLng;
        LatLng userLocation;
        LatLng customPosition;

    @Override
    protected String doInBackground(Object... objects) {

       map =(GoogleMap) objects[0];
       url = (String) objects[1];
       latLng = (LatLng) objects[2];
       userLocation = (LatLng)objects[3];
        FetchUrl fetchUrl = new FetchUrl();
        try {
            directionsData = fetchUrl.readUrl(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return directionsData;

    }

    @Override
    protected void onPostExecute(String s) {
        HashMap<String , String> distanceData = null;

        DataParser distanceParser = new DataParser();
        distanceData = distanceParser.parseDistance(s);


        distance = distanceData.get("distance");
        duration = distanceData.get("duration");

        System.out.println(distance);
        System.out.println(duration);
        map.clear();
        // create new marker with new title and snippet

        MarkerOptions options = new MarkerOptions().position(latLng)
                .draggable(true)
                .title("Duration:"+duration)
                .snippet("Distance:"+distance)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));

        map.addMarker(options);

        MarkerOptions options1 = new MarkerOptions()
                .position(userLocation)
                .icon(BitmapDescriptorFactory.defaultMarker());

       // map.addMarker(options1);


                if (customPosition != null){
                    MarkerOptions options2 = new MarkerOptions()
                            .position(customPosition)
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));

                    map.addMarker(options2);
                }



             String [] directionList;
             DataParser directionParser = new DataParser();
             directionList = directionParser.parseDirection(s);
             displayDirection(directionList);



    }
    private void displayDirection(String[] directionList){
        if (directionList == null){


           return;

        }else{
            int count = directionList.length;
            for(int i = 0; i<count;i++){
                PolylineOptions options = new PolylineOptions()
                        .color(Color.BLUE)
                        .width(10)
                        .addAll(PolyUtil.decode(directionList[i]));
                map.addPolyline(options);

        }

        }
    }
}
