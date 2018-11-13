package com.linh.wiinav.models;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.UUID;

@IgnoreExtraProperties
public class Report {
    private String reporter, inspector, reporterId, inspectorId, content, reportType, reportTypeId, verifyDate, postDate;
    private double latitude, longitude;

    private UUID id = UUID.randomUUID();

    public Report(String reporter, String inspector, String reporterId,
                  String inspectorId, String content, String reportType,
                  String reportTypeId, String verifyDate, String postDate,
                  float latitude, float longitude) {
        this.reporter = reporter;
        this.inspector = inspector;
        this.reporterId = reporterId;
        this.inspectorId = inspectorId;
        this.content = content;
        this.reportType = reportType;
        this.reportTypeId = reportTypeId;
        this.verifyDate = verifyDate;
        this.postDate = postDate;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Report() {
    }

    public UUID getId() {
        return id;
    }

    public String getReporter() {
        return reporter;
    }

    public void setReporter(String reporter) {
        this.reporter = reporter;
    }

    public String getInspector() {
        return inspector;
    }

    public void setInspector(String inspector) {
        this.inspector = inspector;
    }

    public String getReporterId() {
        return reporterId;
    }

    public void setReporterId(String reporterId) {
        this.reporterId = reporterId;
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

    public String getReportTypeId() {
        return reportTypeId;
    }

    public void setReportTypeId(String reportTypeId) {
        this.reportTypeId = reportTypeId;
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

    @Override
    public String toString() {
        return "Report{" +
                "reporter='" + reporter + '\'' +
                ", inspector='" + inspector + '\'' +
                ", reporterId='" + reporterId + '\'' +
                ", inspectorId='" + inspectorId + '\'' +
                ", content='" + content + '\'' +
                ", reportType='" + reportType + '\'' +
                ", reportTypeId='" + reportTypeId + '\'' +
                ", verifyDate='" + verifyDate + '\'' +
                ", postDate='" + postDate + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }
}
