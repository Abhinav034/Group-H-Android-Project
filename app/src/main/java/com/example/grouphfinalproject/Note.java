package com.example.grouphfinalproject;

import java.io.Serializable;

public class Note implements Serializable {

    private int id;
    private String title, description, category, createdTimeStamp;
    private double latitude, longitude;


    public Note(int id, String title, String description, String category, String createdTimeStamp, double latitude, double longitude) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.category = category;
        this.createdTimeStamp = createdTimeStamp;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public int getId() {
        return id;
    }



    public String getTitle() {
        return title;
    }



    public String getDescription() {
        return description;
    }



    public String getCategory() {
        return category;
    }


    public String getCreatedTimeStamp() {
        return createdTimeStamp;
    }



    public double getLatitude() {
        return latitude;
    }



    public double getLongitude() {
        return longitude;
    }




}
