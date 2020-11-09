package com.example.mobile_w01_07_5.ui.uploadphoto;
import android.media.ExifInterface;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;


public class PhotoAttrsUtil {

    private static final String CACHED_FILE_NAME ="cached_data";

    public static PictureAttrs getPhotoAttrs(String path) {
        if (path == null) {
            return null;
        }

        String TAG_APERTURE = null;
        String TAG_DATETIME = null;
        String TAG_EXPOSURE_TIME = null;
        String TAG_FLASH = null;
        String TAG_FOCAL_LENGTH = null;
        String TAG_IMAGE_LENGTH = null;
        String TAG_IMAGE_WIDTH = null;
        String TAG_ISO = null;
        String TAG_MAKE = null;
        String TAG_MODEL = null;
        String TAG_ORIENTATION = null;
        String TAG_WHITE_BALANCE = null;
        String TAG_GPS_LATITUDE_REF = null;
        String TAG_GPS_LONGITUDE_REF = null;
        String TAG_GPS_LATITUDE = null;
        String TAG_GPS_LONGITUDE = null;


        try {

            ExifInterface exifInterface = new ExifInterface(path);
            TAG_APERTURE = exifInterface.getAttribute(ExifInterface.TAG_APERTURE);
            TAG_DATETIME = exifInterface.getAttribute(ExifInterface.TAG_DATETIME);
            TAG_EXPOSURE_TIME = exifInterface.getAttribute(ExifInterface.TAG_EXPOSURE_TIME);
            TAG_FLASH = exifInterface.getAttribute(ExifInterface.TAG_FLASH);
            TAG_FOCAL_LENGTH = exifInterface.getAttribute(ExifInterface.TAG_FOCAL_LENGTH);
            TAG_IMAGE_LENGTH = exifInterface.getAttribute(ExifInterface.TAG_IMAGE_LENGTH);
            TAG_IMAGE_WIDTH = exifInterface.getAttribute(ExifInterface.TAG_IMAGE_WIDTH);
            TAG_ISO = exifInterface.getAttribute(ExifInterface.TAG_ISO);
            TAG_MAKE = exifInterface.getAttribute(ExifInterface.TAG_MAKE);
            TAG_MODEL = exifInterface.getAttribute(ExifInterface.TAG_MODEL);
            TAG_ORIENTATION = exifInterface.getAttribute(ExifInterface.TAG_ORIENTATION);
            TAG_WHITE_BALANCE = exifInterface.getAttribute(ExifInterface.TAG_WHITE_BALANCE);
            TAG_GPS_LATITUDE_REF = exifInterface.getAttribute(ExifInterface.TAG_GPS_LATITUDE_REF);
            TAG_GPS_LONGITUDE_REF = exifInterface.getAttribute(ExifInterface.TAG_GPS_LONGITUDE_REF);
            TAG_GPS_LONGITUDE_REF = exifInterface.getAttribute(ExifInterface.TAG_GPS_LONGITUDE_REF);
            TAG_GPS_LONGITUDE_REF = exifInterface.getAttribute(ExifInterface.TAG_GPS_LONGITUDE_REF);
            TAG_GPS_LATITUDE = exifInterface.getAttribute(ExifInterface.TAG_GPS_LATITUDE);
            TAG_GPS_LONGITUDE = exifInterface.getAttribute(ExifInterface.TAG_GPS_LONGITUDE);
        } catch (IOException e) {
            e.printStackTrace();
        }


        LatLng latLng = new LatLng(getRationalLatLonToFloat(TAG_GPS_LATITUDE, TAG_GPS_LATITUDE_REF),
                getRationalLatLonToFloat(TAG_GPS_LONGITUDE, TAG_GPS_LONGITUDE_REF));

        //return the time, model, orientation and gps location
        return new PictureAttrs(TAG_DATETIME, TAG_MODEL,TAG_ORIENTATION, latLng.latitude, latLng.longitude, null);
    }


    private static double getRationalLatLonToFloat(String rationalString, String ref) {

        String south = "S";
        String west = "W";
        if (rationalString == null || ref == null) {
            return 0.0;
        }
        String[] parts = rationalString.split(",");
        String[] pair;
        pair = parts[0].split("/");
        double degrees = Double.parseDouble(pair[0].trim()) / Double.parseDouble(pair[1].trim());
        pair = parts[1].split("/");
        double minutes = Double.parseDouble(pair[0].trim()) / Double.parseDouble(pair[1].trim());
        pair = parts[2].split("/");
        double seconds = Double.parseDouble(pair[0].trim()) / Double.parseDouble(pair[1].trim());
        double result = degrees + (minutes / 60.0) + (seconds / 3600.0);
        if ((ref.equals(south) || ref.equals(west))) {
            return -result;
        }
        return result;
    }




    public static class PictureAttrs {
        private String time;
        private String model;
        private double latitude;
        private double longitude;
        private String seaLevel;
        private String orientation;

        public PictureAttrs( String time,  String model,String orientation, double latitude, double longitude, String seaLevel) {
            this.time = time;
            this.model = model;
            this.latitude = latitude;
            this.longitude = longitude;
            this.seaLevel = seaLevel;
            this.orientation=orientation;
        }


        public String getOrientation() {
            return orientation;
        }

        public void setOrientation(String orientation) {
            this.orientation = orientation;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getModel() {
            return model;
        }

        public void setModel(String model) {
            this.model = model;
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

        public String getSeaLevel() {
            return seaLevel;
        }

        public void setSeaLevel(String seaLevel) {
            this.seaLevel = seaLevel;
        }
    }

}
