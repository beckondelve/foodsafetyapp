package com.munish.saferfoodproject.Service.Models;

public class MainModel {
    User user;
    PermiseData permiseData;
    PdfDataModel pdfDataModel;

    public MainModel() {
    }

    public MainModel(User user, PermiseData permiseData) {
        this.user = user;
        this.permiseData = permiseData;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public PermiseData getPermiseData() {
        return permiseData;
    }

    public void setPermiseData(PermiseData permiseData) {
        this.permiseData = permiseData;
    }
}
