package com.linh.wiinav.models;

import com.google.firebase.database.IgnoreExtraProperties;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

@IgnoreExtraProperties
public class Report implements Serializable{

    private String id,content;
    private User poster,inspector;
    private ReportType reportType;
    private int upVote,downVote;
    private Date postDate,verifyDate;
    private float longtitude, latitude;
    private ArrayList<Image> images;

    public Report() {
    }

    public Report(String id, String content, User poster, User inspector, ReportType reportType,
                  int upVote, int downVote, Date postDate, Date verifyDate, float longtitude,
                  float latitude, ArrayList<Image> images) {
        this.id = id;
        this.content = content;
        this.poster = poster;
        this.inspector = inspector;
        this.reportType = reportType;
        this.upVote = upVote;
        this.downVote = downVote;
        this.postDate = postDate;
        this.verifyDate = verifyDate;
        this.longtitude = longtitude;
        this.latitude = latitude;
        this.images = images;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public ReportType getReportType() {
        return reportType;
    }

    public void setReportType(ReportType reportType) {
        this.reportType = reportType;
    }

    public int getUpVote() {
        return upVote;
    }

    public void setUpVote(int upVote) {
        this.upVote = upVote;
    }

    public int getDownVote() {
        return downVote;
    }

    public void setDownVote(int downVote) {
        this.downVote = downVote;
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

    public float getLongtitude() {
        return longtitude;
    }

    public void setLongtitude(float longtitude) {
        this.longtitude = longtitude;
    }

    public float getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public ArrayList<Image> getImages() {
        return images;
    }

    public void setImages(ArrayList<Image> images) {
        this.images = images;
    }

    @Override
    public String toString() {
        return "Report{" +
                "id='" + id + '\'' +
                ", content='" + content + '\'' +
                ", poster=" + poster +
                ", inspector=" + inspector +
                ", reportType=" + reportType +
                ", upVote=" + upVote +
                ", downVote=" + downVote +
                ", postDate=" + postDate +
                ", verifyDate=" + verifyDate +
                ", longtitude=" + longtitude +
                ", latitude=" + latitude +
                ", images=" + images +
                '}';
    }
}
