package com.linh.wiinav.models;

import java.io.Serializable;
import java.util.ArrayList;

public class AskHelp implements Serializable {
    private String id;
    private User poster;
    private String postDate;
    private String title;
    private String content;
    private boolean isCompleted;
    private double latitude, longtitude;
    private ArrayList<Comment> comments ;

    public AskHelp() {
    }

    public AskHelp(String id, User poster, String postDate, String title, String content, boolean isCompleted, double latitude, double longtitude, ArrayList<Comment> comments) {
        this.id = id;
        this.poster = poster;
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

    public User getPoster() {
        return poster;
    }

    public void setPoster(User poster) {
        this.poster = poster;
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
