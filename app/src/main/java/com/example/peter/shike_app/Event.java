package com.example.peter.shike_app;

public class Event {

    public int eventID;
    public int publisherID;
    public String title;
    public int locationID;
    public double locationX, locationY;
    public String beginDate, beginTime, endDate, endTime;
    public int type;
    public String description;
    public int outdate;
    public String username;
    public int isHelped;
    public int helper;

    public void setEventID(int eventID) {
        this.eventID = eventID;
    }
    public void setPublisherID(int publisherID){
        this.publisherID = publisherID;
    }
    public void setTitle(String title) {
        this.title = title;
    }


    public void setType(String type) {
        switch (type) {
            case "实时":
                this.type = 0;
                break;
            case "活动预告":
                this.type = 1;
                break;
            case "求助":
                this.type = 2;
                break;
        }
    }
    public void setType(int type) {
        this.type = type;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public void setOutdate(int outdate) {
        this.outdate = outdate;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public void setIshelped(int ishelped) {
        this.isHelped = ishelped;
    }
    public void setHelper(int helper) {
        this.helper = helper;
    }

    public int getEventId() {
        return eventID;
    }
    public int getPublisherID() {
        return publisherID;
    }
    public String getTitle() {
        return title;
    }
    public int getLocationID() {
        return locationID;
    }
    public double getLocationX() {
        return locationX;
    }
    public double getLocationY() {
        return locationY;
    }
    public int getType() {
        return type;
    }
    public String getBeginTime() {
        return beginTime;
    }
    public String getEndTime() {
        return endTime;
    }
    public String getDescription() {
        return description;
    }
    public int getOutdate() {
        return outdate;
    }
    public String getUsername() {
        return username;
    }
    public int getIshelped() {
        return isHelped;
    }
    public int getHelper() {
        return helper;
    }
}
