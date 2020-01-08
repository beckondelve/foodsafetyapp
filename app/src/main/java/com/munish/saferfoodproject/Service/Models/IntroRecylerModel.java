package com.munish.saferfoodproject.Service.Models;

public class IntroRecylerModel {
    String name, url, date;

    public IntroRecylerModel(String name, String url, String date) {
        this.name = name;
        this.url = url;
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }

    public String getDate() {
        return date;
    }
}
