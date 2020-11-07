package com.example.mobile_w01_07_5.ui.uploadphoto;

public class Photohelper {



    private double locationX;
    private double locationY;
    private String stampID;
    private String userID;
    private String name;
    private int rate;
    private String description;
    private String photo;
    private Boolean HighlyRated;

    public Photohelper() {
    }

    public Photohelper(double locationX, double locationY, String stampID, String userID, String name, int rate, String description, String photo, Boolean highlyRated) {
        this.locationX = locationX;
        this.locationY = locationY;
        this.stampID = stampID;
        this.userID = userID;
        this.name = name;
        this.rate = rate;
        this.description = description;
        this.photo = photo;
        HighlyRated = highlyRated;
    }

    public double getLocationX() {
        return locationX;
    }

    public void setLocationX(double locationX) {
        this.locationX = locationX;
    }

    public double getLocationY() {
        return locationY;
    }

    public void setLocationY(double locationY) {
        this.locationY = locationY;
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
