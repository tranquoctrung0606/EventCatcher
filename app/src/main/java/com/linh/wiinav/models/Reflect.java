package com.linh.wiinav.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class Reflect implements Serializable{

    private String id,title,content;
    private User poster, inspector;
    private Date postDate,verifyDate;
    private ArrayList<Comment> comments;
    private ArrayList<Image> images;

    public Reflect() {
    }

    public Reflect(String id, String title, String content, User poster, User inspector,
                   Date postDate, Date verifyDate, ArrayList<Comment> comments) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.poster = poster;
        this.inspector = inspector;
        this.postDate = postDate;
        this.verifyDate = verifyDate;
        this.comments = comments;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public User getPoster() {
        return poster;
    }

    public void setPoster(User poster) {
        this.poster = poster;
    }

    public User getInspector() {
        return inspector;
    }

    public void setInspector(User inspector) {
        this.inspector = inspector;
    }

    public Date getPostDate() {
        return postDate;
    }

    public void setPostDate(Date postDate) {
        this.postDate = postDate;
    }

    public Date getVerifyDate() {
        return verifyDate;
    }

    public void setVerifyDate(Date verifyDate) {
        this.verifyDate = verifyDate;
    }

    public ArrayList<Comment> getComments() {
        return comments;
    }

    public void setComments(ArrayList<Comment> comments) {
        this.comments = comments;
    }

    @Override
    public String toString() {
        return "Reflect{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", poster=" + poster +
                ", inspector=" + inspector +
                ", postDate=" + postDate +
                ", verifyDate=" + verifyDate +
                ", comments=" + comments +
                '}';
    }
}
