package com.example.grouphfinalproject.Activities;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Looper;
import android.view.View;
import android.widget.ImageButton;

import com.example.grouphfinalproject.GetRouteData;
import com.example.grouphfinalproject.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    FusedLocationProviderClient fusedLocationProviderClient;
    LocationRequest locationRequest;
    LocationCallback locationCallback;
    ImageButton button;
    Double olat , olng;
    Double dlat , dlng;

    Marker homeMarker;

    ArrayList<Marker>markers = new ArrayList<>();


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {


                fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());
            }
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        button = findViewById(R.id.goButton);


        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        locationRequest = new LocationRequest();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(3000);
        locationRequest.setSmallestDisplacement(20);
        locationRequest.setFastestInterval(2000);


        getLocation();


        if (!isOnline()){

           AlertDialog.Builder alert = new AlertDialog.Builder(this);
           alert.setIcon(R.drawable.ic_warning);
           alert.setTitle("Network Problem");
           alert.setMessage("Please make sure you are connected to internet");

           alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
               @Override
               public void onClick(DialogInterface dialog, int which) {

                   Intent intent = new Intent(MapsActivity.this , NotesActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                        startActivity(intent);


               }

           });
            AlertDialog alertDialog = alert.create();
            alertDialog.show();



        }

    }
    public boolean isOnline() {
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

    private void getLocation(){

        locationCallback = new LocationCallback(){

            @Override
            public void onLocationResult(LocationResult locationResult) {
                for(Location location : locationResult.getLocations()){

                   olat =  location.getLatitude();
                   olng =  location.getLongitude();
                    setHomeMarker(location);


                }
            }
        };
    }

    private void setHomeMarker(Location location){

        LatLng userLocation = new LatLng(location.getLatitude() , location.getLongitude());

        MarkerOptions options = new MarkerOptions()
                .position(userLocation)
                .title("Your location");

            homeMarker = mMap.addMarker(options);
            markers.add(homeMarker);
            cameraFocus(userLocation);

        if (markers.size() > 1){
            for (Marker marker:markers){
                marker.remove();
            }markers.clear();

        }
        homeMarker = mMap.addMarker(options);
        markers.add(homeMarker);

    }

    private void cameraFocus(LatLng latLng){

        CameraPosition position = CameraPosition.builder()
                .target(latLng)
                .zoom(14)
                .build();

        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(position));
    }


    public void setDestMarker(LatLng latLng){

        MarkerOptions options = new MarkerOptions()
                .position(latLng)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
        mMap.addMarker(options);
        cameraFocus(latLng);

    }




    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());


        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);

        }Intent intent = getIntent();

        dlat = intent.getDoubleExtra("lat" , 0);
        dlng = intent.getDoubleExtra("lng" , 0);

        System.out.println(dlng+","+dlat);


        LatLng latLng = new LatLng(dlat , dlng);

        setDestMarker(latLng);





    }

    public void navButtonPressed(View view) {

        String url = getDirectionsUrl();
        Object [] dataTransfer = new Object[4];

        dataTransfer[0] = mMap;
        dataTransfer[1] = url;
        dataTransfer[2] = new LatLng(dlat , dlng);
        dataTransfer[3] = new LatLng(olat , olng);

        GetRouteData getRouteData = new GetRouteData();
        getRouteData.execute(dataTransfer);

    }
    private String getDirectionsUrl(){
      StringBuilder stringBuilder = new StringBuilder("https://maps.googleapis.com/maps/api/directions/json?");
        stringBuilder.append("origin=" + olat + "," + olng);
        stringBuilder.append("&destination=" + dlat + "," + dlng);
        stringBuilder.append("&key=" + getString(R.string.api_key_directions));

        System.out.println(stringBuilder);
        return stringBuilder.toString();
    }



}