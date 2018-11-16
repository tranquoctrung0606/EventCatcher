package com.linh.wiinav.models;

import java.io.Serializable;

public class Image implements Serializable {

    private String id,url;
    private User user;

    public Image() {
    }

    public Image(String id, String url, User user) {
        this.id = id;
        this.url = url;
        this.user = user;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "Image{" +
                "id='" + id + '\'' +
                ", url='" + url + '\'' +
                ", user=" + user +
                '}';
    }
}
