package com.example.grouphfinalproject.Fragments;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.grouphfinalproject.Activities.Main2Activity;
import com.example.grouphfinalproject.GetRouteData;
import com.example.grouphfinalproject.Models.NoteModel;
import com.example.grouphfinalproject.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class MapFragment extends Fragment implements OnMapReadyCallback {

    private GoogleMap mMap;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;


    private Double olat , olng;
    private Double dlat , dlng;

    private LatLng currentLocation, destination;

    private ArrayList<Marker> markers = new ArrayList<>();


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());
            }
        } else {
            requestPermissions( new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);

        }
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_map , container , false );
        ImageButton imageButton = view.findViewById(R.id.directionButton);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = getDirectionsUrl();
                Object [] dataTransfer = new Object[4];

                dataTransfer[0] = mMap;
                dataTransfer[1] = url;
                dataTransfer[2] = new LatLng(dlat , dlng);
                dataTransfer[3] = new LatLng(olat , olng);

                GetRouteData getRouteData = new GetRouteData();
                getRouteData.execute(dataTransfer);
            }
        });
        return view;
    }

   @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map_fragment);
        mapFragment.getMapAsync(this);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());

        locationRequest = new LocationRequest();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(3000);
        locationRequest.setSmallestDisplacement(20);
        locationRequest.setFastestInterval(2000);

        getLocation();


    }

    private void getLocation(){

      locationCallback = new LocationCallback(){
          @Override
          public void onLocationResult(LocationResult locationResult) {
              for(Location location : locationResult.getLocations()){

//                 setHomeMarker(location);
                  olat = location.getLatitude();
                  olng = location.getLongitude();

                  mMap.setMyLocationEnabled(true);

                  currentLocation = new LatLng(location.getLatitude(), location.getLongitude());


                  CameraPosition cameraPosition = CameraPosition.builder()
                          .target(currentLocation)
                          .bearing(0)
                          .tilt(45)
                          .zoom(15)
                          .build();
                  mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

              }
          }
      };



    }

    private void setHomeMarker(Location location){
        Marker homeMarker;
        LatLng userlocation = new LatLng(location.getLatitude() , location.getLongitude());

        MarkerOptions options = new MarkerOptions()
                .position(userlocation);


        homeMarker = mMap.addMarker(options);
        markers.add(homeMarker);
        cameraFocus(userlocation);

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


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());


        } else {
            requestPermissions( new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);

        }

        if(Main2Activity.noteModelData != null) {
            destination = new LatLng(Main2Activity.noteModelData.getLatitude(), Main2Activity.noteModelData.getLongitude());
             dlat = Main2Activity.noteModelData.getLatitude();
            dlng = Main2Activity.noteModelData.getLongitude();

//            LatLng latLng = new LatLng(dlat, dlng);
            MarkerOptions options = new MarkerOptions()
                    .position(destination)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));


            mMap.addMarker(options);
        }

    }

    private String getDirectionsUrl(){
        StringBuilder stringBuilder = new StringBuilder("https://maps.googleapis.com/maps/api/directions/json?");
        stringBuilder.append("origin=" + currentLocation.latitude + "," + currentLocation.longitude);
        stringBuilder.append("&destination=" + destination.latitude + "," + destination.longitude);
        stringBuilder.append("&key=" + getString(R.string.api_key_directions));

        System.out.println(stringBuilder);
        return stringBuilder.toString();
    }



}
