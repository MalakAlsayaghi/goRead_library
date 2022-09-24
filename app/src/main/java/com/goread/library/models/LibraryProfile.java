package com.goread.library.models;

public class LibraryProfile {
    String id, name, img_url, phone, location;
    boolean isEnabled;
    double rating;

    public LibraryProfile(String id, String name, String img_url, String phone, String location, boolean isEnabled, double rating) {
        this.id = id;
        this.name = name;
        this.img_url = img_url;
        this.phone = phone;
        this.location = location;
        this.isEnabled = isEnabled;
        this.rating = rating;
    }

    public LibraryProfile(){
        
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public void setEnabled(boolean enabled) {
        isEnabled = enabled;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }


}
