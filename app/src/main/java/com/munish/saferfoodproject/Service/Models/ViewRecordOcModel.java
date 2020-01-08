package com.munish.saferfoodproject.Service.Models;

import org.json.JSONArray;

import java.util.ArrayList;

public class ViewRecordOcModel {
    String name, date,comment;
    JSONArray items;

    public ViewRecordOcModel() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public JSONArray getItems() {
        return items;
    }

    public void setItems(JSONArray items) {
        this.items = items;
    }
}
