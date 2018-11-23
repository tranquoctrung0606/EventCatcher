package com.linh.wiinav.models;

import java.util.ArrayList;

public class AskHelp {
    private String id;
    private String posterId;
    private String postDate;
    private String title;
    private String content;
    private boolean isCompleted;
    private double latitude, longtitude;
    private ArrayList<Comment> comments;

    public AskHelp() {
    }

    public AskHelp(String id, String posterId, String postDate, String title, String content, boolean isCompleted, double latitude, double longtitude, ArrayList<Comment> comments) {
        this.id = id;
        this.posterId = posterId;
        this.postDate = postDate;
        this.title = title;
        this.content = content;
        this.isCompleted = isCompleted;
        this.latitude = latitude;
        this.longtitude = longtitude;
        this.comments = comments;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPosterId() {
        return posterId;
    }

    public void setPosterId(String posterId) {
        this.posterId = posterId;
    }

    public String getPostDate() {
        return postDate;
    }

    public void setPostDate(String postDate) {
        this.postDate = postDate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongtitude() {
        return longtitude;
    }

    public void setLongtitude(double longtitude) {
        this.longtitude = longtitude;
    }
    public ArrayList<Comment> getComments() {
        return comments;
    }

    public void setComments(ArrayList<Comment> comments) {
        this.comments = comments;
    }
}
