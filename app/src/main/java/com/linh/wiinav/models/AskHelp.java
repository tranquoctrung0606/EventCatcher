package com.linh.wiinav.models;

import android.support.annotation.NonNull;

import java.io.Serializable;
import java.util.ArrayList;

public class AskHelp implements Serializable {
    private String id;
    private User poster;
    private String postDate;
    private String title;
    private String content;
    private boolean isCompleted;
    private double latitude, longitude;
    private ArrayList<Comment> comments ;

    public AskHelp() {
    }

    public AskHelp(String id, User poster, String postDate,
                   String title, String content, boolean isCompleted,
                   double latitude, double longitude, ArrayList<Comment> comments) {
        this.id = id;
        this.poster = poster;
        this.postDate = postDate;
        this.title = title;
        this.content = content;
        this.isCompleted = isCompleted;
        this.latitude = latitude;
        this.longitude = longitude;
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

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public ArrayList<Comment> getComments() {
        return comments;
    }

    public void setComments(ArrayList<Comment> comments) {
        this.comments = comments;
    }

    @Override
    public String toString() {
        return "AskHelp{" +
                "id='" + id + '\'' +
                ", poster=" + poster +
                ", postDate='" + postDate + '\'' +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", isCompleted=" + isCompleted +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", comments=" + comments +
                '}';
    }
}
