package com.example.mobile_w01_07_5;


import android.media.ExifInterface;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;


/**
 * @author Jiale Zhang
 */
public class PhotoAttrsUtil {

    private static final String CACHED_FILE_NAME ="cached_data";

    public static PictureAttrs getPhotoAttrs(String path) {
        if (path == null) {
            return null;
        }

        String TAG_APERTURE = null;
        //光圈值
        String TAG_DATETIME = null;
        //拍照时间
        String TAG_EXPOSURE_TIME = null;
        //曝光时间
        String TAG_FLASH = null;
        //闪光灯
        String TAG_FOCAL_LENGTH = null;
        //焦距
        String TAG_IMAGE_LENGTH = null;
        //图片高度
        String TAG_IMAGE_WIDTH = null;
        //图片宽度
        String TAG_ISO = null;
        //ISO
        String TAG_MAKE = null;
        //设备品牌
        String TAG_MODEL = null;
        //设备型号，整形表示
        String TAG_ORIENTATION = null;
        //旋转角度
        String TAG_WHITE_BALANCE = null;
        //白平衡
        String TAG_GPS_LATITUDE_REF = null;
        //纬度 [南S/北N]
        String TAG_GPS_LONGITUDE_REF = null;
        //经度 [东E/西W]
        String TAG_GPS_LATITUDE = null;
        //纬度
        String TAG_GPS_LONGITUDE = null;
        //经度


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

        return new PictureAttrs(TAG_DATETIME, TAG_MODEL, latLng.latitude, latLng.longitude, null);
    }

    //将经纬度String转换成double

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
        private String model;//手机型号
        private double latitude;//纬度
        private double longitude;//经度
        private String seaLevel;//海拔高度

        public PictureAttrs(String time, String model, double latitude, double longitude, String seaLevel) {
            this.time = time;
            this.model = model;
            this.latitude = latitude;
            this.longitude = longitude;
            this.seaLevel = seaLevel;
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
