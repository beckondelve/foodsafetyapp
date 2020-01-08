package com.munish.saferfoodproject.Service.Models;

import org.json.JSONArray;

import java.util.List;

public class User {

    String fullName;
    String emailAddress;

    public User() {
        //Empty Constructor For Firebase
    }

    public User(String fullName, String emailAddress) {
        this.fullName = fullName;
        this.emailAddress = emailAddress;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }
}