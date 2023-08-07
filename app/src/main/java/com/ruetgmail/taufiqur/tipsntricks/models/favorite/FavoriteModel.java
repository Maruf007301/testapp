package com.ruetgmail.taufiqur.tipsntricks.models.favorite;

public class FavoriteModel {
    int id;
    String title;
    String subTitle;
    String details;
    String imageUrl;

    public FavoriteModel(int id, String title, String subTitle, String details, String imageUrl) {
        this.id = id;
        this.title = title;
        this.subTitle = subTitle;
        this.details = details;
        this.imageUrl = imageUrl;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDetails() {
        return details;
    }

    public String getImageUrl() {
        return imageUrl;
    }

}