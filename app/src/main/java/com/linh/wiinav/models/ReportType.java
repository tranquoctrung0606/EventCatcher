package com.linh.wiinav.models;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class ReportType
{
    private String id;
    private String name;
    private Long duration;

    public ReportType()
    {
    }

    public ReportType(final String id, final String name, final Long duration)
    {
        this.id = id;
        this.name = name;
        this.duration = duration;
    }

    public String getId()
    {
        return id;
    }

    public void setId(final String id)
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(final String name)
    {
        this.name = name;
    }

    public Long getDuration()
    {
        return duration;
    }

    public void setDuration(final Long duration)
    {
        this.duration = duration;
    }

    @Override
    public String toString()
    {
        return "ReportType{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", duration=" + duration +
                '}';
    }
}
