package com.linh.wiinav.models;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.UUID;

@IgnoreExtraProperties
public class Report {
    private String id, inspector, inspectorId, content, reportType, verifyDate, postDate;
    private double latitude, longitude;
    private User reporter;

    public Report() {
    }

    public Report(String id, String inspector, String inspectorId,
                  String content, String reportType, String verifyDate,
                  String postDate, double latitude, double longitude, User reporter) {
        this.id = id;
        this.inspector = inspector;
        this.inspectorId = inspectorId;
        this.content = content;
        this.reportType = reportType;
        this.verifyDate = verifyDate;
        this.postDate = postDate;
        this.latitude = latitude;
        this.longitude = longitude;
        this.reporter = reporter;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getInspector() {
        return inspector;
    }

    public void setInspector(String inspector) {
        this.inspector = inspector;
    }

    public String getInspectorId() {
        return inspectorId;
    }

    public void setInspectorId(String inspectorId) {
        this.inspectorId = inspectorId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getReportType() {
        return reportType;
    }

    public void setReportType(String reportType) {
        this.reportType = reportType;
    }

    public String getVerifyDate() {
        return verifyDate;
    }

    public void setVerifyDate(String verifyDate) {
        this.verifyDate = verifyDate;
    }

    public String getPostDate() {
        return postDate;
    }

    public void setPostDate(String postDate) {
        this.postDate = postDate;
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

    public User getReporter() {
        return reporter;
    }

    public void setReporter(User reporter) {
        this.reporter = reporter;
    }

    @Override
    public String toString() {
        return "Report{" +
                "id='" + id + '\'' +
                ", inspector='" + inspector + '\'' +
                ", inspectorId='" + inspectorId + '\'' +
                ", content='" + content + '\'' +
                ", reportType='" + reportType + '\'' +
                ", verifyDate='" + verifyDate + '\'' +
                ", postDate='" + postDate + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", reporter=" + reporter +
                '}';
    }
}
