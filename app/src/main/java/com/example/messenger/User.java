package com.example.messenger;

import java.io.Serializable;

public class User implements Serializable {
    private String id = "";
    private String full_name = "";
    private String email = "";
    private String profile_image = "";
    private String phone_number = "";
    private String birthdate = "";
    private String pwd = "";

    private boolean online = false;

    public User(){

    }

    public User(String email,String pwd){
        this.email = email;
        this.pwd = pwd;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getProfile_image() {
        return profile_image;
    }

    public void setProfile_image(String profile_image) {
        this.profile_image = profile_image;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(String birthdate) {
        this.birthdate = birthdate;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public boolean getOnline(){return online;}
    public void setOnline(boolean online){this.online = online;}

}
