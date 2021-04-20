package com.example.restaurantapp;

public class User {

    String name, phNo, password;

    public User() {
    }

    public User(String name, String phNo, String password) {
        this.name = name;
        this.phNo = phNo;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhNo() {
        return phNo;
    }

    public void setEmail(String phNo) {
        this.phNo = phNo;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
