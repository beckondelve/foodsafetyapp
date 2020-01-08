package com.munish.saferfoodproject.Service.Models;

import java.util.ArrayList;

public class OcFoodSafetyModel {
    String name,comment,time;
    ArrayList<String> selectedItem;

    public OcFoodSafetyModel(String name, String comment, String time, ArrayList<String> strings) {
        this.name = name;
        this.comment = comment;
        this.time = time;
        this.selectedItem = strings;
    }

    public String getName() {
        return name;
    }

    public String getComment() {
        return comment;
    }

    public String getTime() {
        return time;
    }

    public ArrayList<String> getStrings() {
        return selectedItem;
    }
}
