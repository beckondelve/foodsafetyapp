package com.munish.saferfoodproject.Service.Models;

public class PermiseData {
    String name;
    String location;

    public PermiseData() {
        //Empty Constructor For Firebase
    }

    public PermiseData(String name, String location) {
        this.name = name;
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
