package com.example.grouphfinalproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

public class NoteDetails extends AppCompatActivity {


    public static final int REQUEST_CODE = 1;
    EditText title_ET, description_ET, catagory_ET;
    DatabaseHelper databaseHelper;
    String categoryName;

    // location manager and listener
    LocationManager locationManager;
    LocationListener locationListener;

    Location CurrentLocation;
    Boolean noteExists = false;
    Note noteData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temp_note);




        databaseHelper = new DatabaseHelper(this);

        title_ET = findViewById(R.id.titleID);
        description_ET = findViewById(R.id.descriptionID);
        catagory_ET = findViewById(R.id.catagoryID);

        //save_BTN = findViewById(R.id.saveBTN);

        Intent intent = getIntent();
        noteData = (Note) intent.getSerializableExtra(NotesActivity.SELECTED_NOTE);

        if (noteData != null) {

            noteExists = true;
            title_ET.setText(noteData.getTitle());
            description_ET.setText(noteData.getDescription());
            catagory_ET.setText(noteData.getCategory());

        }else{
            categoryName = intent.getStringExtra(MainActivity.CATEGORY_KEY);
            catagory_ET.setText(categoryName);

        }


        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

                if (location != null){
                    CurrentLocation = location;
                }
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };


        if(!checkPermission()){
            requestPermission();
        }else {

            startup();
        }




    }

    // LOCATION FUNCTIONS

    @SuppressLint("MissingPermission")
    private void startup(){
            reqLocationUpdate();
            CurrentLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

    }

    private boolean checkPermission() {
        int permissionState = ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        return permissionState == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
    }

    @SuppressLint("MissingPermission")
    private void reqLocationUpdate() {

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 100, locationListener);
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startup();

            }else{
                Toast.makeText(this, "Permission is required to access location", Toast.LENGTH_SHORT).show();
            }
        }
    }


    // NOTES RELATED FUNCTION
    public void saveNotes(View view) {

        // add to database.
        String title = title_ET.getText().toString();
        String description = description_ET.getText().toString();
        categoryName = catagory_ET.getText().toString();




        if (noteExists){

            Boolean updated = databaseHelper.updateNote(noteData.getId(), title, description, categoryName);

            Toast.makeText(this, updated ? "Note Updated" : "Error in updating Note Data", Toast.LENGTH_SHORT).show();

            updateNoteDataVar(); // from old data to new data

        }else{

            SimpleDateFormat currentTimeStamp = new SimpleDateFormat("dd-MM-yyyy hh-mm-ss");

            noteExists = databaseHelper.addNote(title, description, categoryName,
                    currentTimeStamp.format(new Date()),CurrentLocation.getLatitude(), CurrentLocation.getLongitude());


            Toast.makeText(this,noteExists ? "Note Saved !!" : "Error in saving Note Data !!", Toast.LENGTH_SHORT).show();


            updateNoteDataVar(); // from null to not null
        }
        

        // at the end of the function while saving data

        System.out.println(MainActivity.catList);
        if (!MainActivity.catList.contains(categoryName)){
            MainActivity.catList.add(categoryName);
        }
        System.out.println(MainActivity.catList);

    }

    private void updateNoteDataVar() {

        Cursor cursor = databaseHelper.getAllNotes(categoryName);

        if(cursor.moveToLast()){

            // here we are updating noteData variable
            noteData = new Note(
                    cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getString(3),
                    cursor.getString(4),
                    cursor.getDouble(5),
                    cursor.getDouble(6)
            );
        }
    }
}
