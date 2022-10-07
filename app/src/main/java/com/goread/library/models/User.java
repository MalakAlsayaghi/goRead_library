package com.goread.library.models;

import java.io.Serializable;

public class User implements Serializable {
    String id, name, email, phone, user_type, token;
    boolean isBlocked,isNew;

    public User(String id, String name, String phone, String email, String user_type, String token) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.user_type = user_type;
        this.token = token;
        this.email = email;
        this.isBlocked = false;
        this.isNew = true;
    }

    public boolean isNew() {
        return isNew;
    }

    public void setNew(boolean aNew) {
        isNew = aNew;
    }

    public boolean isBlocked() {
        return isBlocked;
    }

    public void setBlocked(boolean blocked) {
        isBlocked = blocked;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public User() {
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUser_type() {
        return user_type;
    }

    public void setUser_type(String user_type) {
        this.user_type = user_type;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
