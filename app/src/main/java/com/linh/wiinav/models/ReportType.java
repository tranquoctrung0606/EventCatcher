package com.linh.wiinav.models;

import android.graphics.drawable.Drawable;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class ReportType
{
    private String id;
    private String name;
    private Long duration;
    private int reportIcon;

    public ReportType()
    {
    }

    public ReportType(String id, String name, Long duration, int reportIcon) {
        this.id = id;
        this.name = name;
        this.duration = duration;
        this.reportIcon = reportIcon;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getDuration() {
        return duration;
    }

    public void setDuration(Long duration) {
        this.duration = duration;
    }

    public int getReportIcon() {
        return reportIcon;
    }

    public void setReportIcon(int reportIcon) {
        this.reportIcon = reportIcon;
    }
}
