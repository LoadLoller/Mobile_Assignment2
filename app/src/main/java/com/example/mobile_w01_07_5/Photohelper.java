package com.example.mobile_w01_07_5;

public class Photohelper {

    private double Latitude;
    private double Longitude;
    private String stampID;
    private String userID;
    private String name;
    private int rate;
    private String description;
    private String photo;
    private Boolean HighlyRated;


    public Photohelper(double latitude, double longitude, String stampID, String userID, String name, int rate, String description, String photo, Boolean highlyRated) {
        Latitude = latitude;
        Longitude = longitude;
        this.stampID = stampID;
        this.userID = userID;
        this.name = name;
        this.rate = rate;
        this.description = description;
        this.photo = photo;
        HighlyRated = highlyRated;
    }

    public Photohelper() {
    }

    public double getLatitude() {
        return Latitude;
    }

    public void setLatitude(double latitude) {
        Latitude = latitude;
    }

    public double getLongitude() {
        return Longitude;
    }

    public void setLongitude(double longitude) {
        Longitude = longitude;
    }

    public String getStampID() {
        return stampID;
    }

    public void setStampID(String stampID) {
        this.stampID = stampID;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public Boolean getHighlyRated() {
        return HighlyRated;
    }

    public void setHighlyRated(Boolean highlyRated) {
        HighlyRated = highlyRated;
    }
}
