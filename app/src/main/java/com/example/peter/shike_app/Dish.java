package com.example.peter.shike_app;

/**
 * Created by Peter on 2018/5/28.
 */

public class Dish {
    private int ID;
    private String name;
    private int canteenID;
    private String description;
    private String pictureURL;
    private int category;
    private int publisherID;
    private String publisherName;

    public void setID(int ID) {
        this.ID = ID;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCanteenID(int canteenID) {
        this.canteenID = canteenID;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPictureURL(String pictureURL) {
        this.pictureURL = pictureURL;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    public void setPublisherID(int publisherID) {
        this.publisherID = publisherID;
    }

    public void setPublisherName(String publisherName) {
        this.publisherName = publisherName;
    }

    public int getID() {
        return ID;
    }

    public String getName() {
        return name;
    }

    public int getCanteenID() {
        return canteenID;
    }

    public String getDescription() {
        return description;
    }

    public String getPictureURL() {
        return pictureURL;
    }

    public int getCategory() {
        return category;
    }

    public int getPublisherID() {
        return publisherID;
    }

    public String getPublisherName() {
        return publisherName;
    }
}
