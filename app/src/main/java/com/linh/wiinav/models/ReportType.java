package com.linh.wiinav.models;

import java.io.Serializable;

public class ReportType implements Serializable{

    private String id,name;
    private int minute;

    public ReportType() {
    }

    public ReportType(String id, String name, int minute) {
        this.id = id;
        this.name = name;
        this.minute = minute;
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

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    @Override
    public String toString() {
        return "ReportType{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", minute=" + minute +
                '}';
    }
}
