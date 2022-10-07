package com.goread.library.models;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;

public class LocationUpload implements Serializable {
   double latitude,longitude;

    public LocationUpload(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public LocationUpload() {
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
