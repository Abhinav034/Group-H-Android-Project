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

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCreatedTimeStamp() {
        return createdTimeStamp;
    }

    public void setCreatedTimeStamp(String createdTimeStamp) {
        this.createdTimeStamp = createdTimeStamp;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }


}
