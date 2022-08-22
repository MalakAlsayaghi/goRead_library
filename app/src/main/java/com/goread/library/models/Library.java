package com.goread.library.models;

public class Library {
    String id, name,img_url,phone,location;


    public Library(String id, String name, String img_url, String phone) {
        this.id = id;
        this.name = name;
        this.img_url = img_url;
        this.phone = phone;
    }

    public Library() {
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
}
