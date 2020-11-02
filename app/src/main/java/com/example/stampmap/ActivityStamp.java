package com.example.stampmap;

public class ActivityStamp {

    // If there are other things that need to be added to the
    // stamp on the map pls add here
    private String user_name;
    private String message;
    private double latitude;
    private double longitude;
    // should we include the photo in this data structure as well?

    public ActivityStamp(String user_name, double latitude, double longitude)
    {
        this.user_name = user_name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.message = "";
    }

    public ActivityStamp(String user_name, String message, double latitude, double longitude)
    {
        this.user_name = user_name;
        this.message = message;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public String getUser_name() {
        return user_name;
    }

    public String getMessage() {
        return message;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
