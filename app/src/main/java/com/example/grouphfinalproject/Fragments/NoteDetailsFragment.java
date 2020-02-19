package com.example.grouphfinalproject.Fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.example.grouphfinalproject.DatabaseHandlers.DatabaseHelper;
import com.example.grouphfinalproject.DatabaseHandlers.ObjectSerializer;
import com.example.grouphfinalproject.Models.NoteModel;
import com.example.grouphfinalproject.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.example.grouphfinalproject.Activities.MainActivity.CATEGORY_LIST;
import static com.example.grouphfinalproject.Activities.MainActivity.catList;
import static com.example.grouphfinalproject.Activities.MainActivity.sharedPreferences;

public class NoteDetailsFragment extends Fragment {

    public static final String TAG = "Frag";
    EditText etTitle, etDescription, etcategory;
    Button btnsave;
    boolean noteExists = false;

    private NoteModel noteModel;
    private String category;
    private DatabaseHelper mDatabaseHelper;


    // location manager and listener

    public static final int REQUEST_CODE = 1;
    LocationManager locationManager;
    LocationListener locationListener;
    Location currentLocation;


    public NoteDetailsFragment(NoteModel noteModel) {
        this.noteModel = noteModel;
    }

    public NoteDetailsFragment(String category) {
        this.category = category;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_note_details, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        mDatabaseHelper = new DatabaseHelper(getContext());

        etTitle = view.findViewById(R.id.et_note_title);
        etDescription = view.findViewById(R.id.et_note_descp);
        etcategory = view.findViewById(R.id.et_note_category);
        btnsave = view.findViewById(R.id.btn_save_note);

        if (noteModel != null) {

            noteExists = true;
            etTitle.setText(noteModel.getTitle());
            etcategory.setText(noteModel.getCategory());
            etDescription.setText(noteModel.getDescription());
        } else if (category != null) {
            etcategory.setText(category);
        }

        locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

                Log.i(TAG, "onLocationChanged: ");
                Log.i(TAG, "onLocationChanged: " + location);
                if (location != null) {
                    currentLocation = location;
                    Log.i(TAG, "onLocationChanged: " + currentLocation.getLatitude());
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

        if (!checkPermission()) {
            requestPermission();
        } else {

            startup();
        }


        btnsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = etTitle.getText().toString().trim();
                String descp = etDescription.getText().toString().trim();
                String cat = etcategory.getText().toString().trim();

                if (noteExists) {
                    Log.i(TAG, "onClick: " + etTitle.getText().toString() + "...." + cat);
                    if (mDatabaseHelper.updateNote(noteModel.getId(), title, descp, cat))
                        Toast.makeText(getContext(), "Note updated successfully!!", Toast.LENGTH_SHORT).show();
                    else
                        Toast.makeText(getContext(), "Error in updating note.", Toast.LENGTH_SHORT).show();

                    updateNoteDataVar(cat);
                } else {
                    SimpleDateFormat currentTimeStamp = new SimpleDateFormat("dd-MM-yyyy hh-mm-ss");

                    Log.i(TAG, "onClick: " + etTitle.getText().toString() + "...." + currentLocation.getLatitude());
                    if (mDatabaseHelper.addNote(title, descp, cat,
                            currentTimeStamp.format(new Date()), currentLocation.getLatitude(), currentLocation.getLongitude()))

                        Toast.makeText(getContext(), "Notes saved!", Toast.LENGTH_SHORT).show();
                    else
                        Toast.makeText(getContext(), "Error while saving!", Toast.LENGTH_SHORT).show();

                    updateNoteDataVar(cat);
                }


                if (!catList.contains(cat)) {
                    catList.add(cat);
                    try {
                        sharedPreferences.edit().putString(CATEGORY_LIST, ObjectSerializer.serialize(catList)).apply();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }


            }
        });
    }

    private void updateNoteDataVar(String categoryName) {

        Cursor cursor = mDatabaseHelper.getAllNotes(categoryName);

        if (cursor.moveToLast()) {

            // here we are updating noteModelData variable
            noteModel = new NoteModel(
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

    @SuppressLint("MissingPermission")
    private void startup() {
        requestLocationUpdate();
        currentLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        Log.i(TAG, "startup: ");
        Log.i(TAG, "startup: " + currentLocation.getLatitude());

    }

    private boolean checkPermission() {
        int permissionState = ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION);
        return permissionState == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {
//        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
        requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
    }

    @SuppressLint("MissingPermission")
    private void requestLocationUpdate() {

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 100, locationListener);
    }

    public void setData(){
        startup();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.i(TAG, "onRequestPermissionsResult: ");
        if (requestCode == REQUEST_CODE) {
            Log.i(TAG, "onRequestPermissionsResult: " + REQUEST_CODE);
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.i(TAG, "onRequestPermissionsResult: IF ");
                startup();

            }else{
                Log.i(TAG, "onRequestPermissionsResult: ELSE");
                Toast.makeText(getContext(), "Requires permission to access location.", Toast.LENGTH_SHORT).show();
            }
        }
    }





}
