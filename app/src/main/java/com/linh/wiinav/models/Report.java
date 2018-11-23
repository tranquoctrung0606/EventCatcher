package com.linh.wiinav.models;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.Date;
import java.util.UUID;

@IgnoreExtraProperties
public class Report {
    private String id, content;
    private Date postDate, closeDate;
    private double latitude, longitude;
    private User reporter;
    private ReportType reportType;
    private int upVote, downVote;

    public Report() {
    }

    public Report(final String id, final String content, final Date postDate,
                  final Date closeDate, final double latitude, final double longitude,
                  final User reporter, final ReportType reportType, final int upVote,
                  final int downVote)
    {
        this.id = id;
        this.content = content;
        this.postDate = postDate;
        this.closeDate = closeDate;
        this.latitude = latitude;
        this.longitude = longitude;
        this.reporter = reporter;
        this.reportType = reportType;
        this.upVote = upVote;
        this.downVote = downVote;
    }

    public String getId()
    {
        return id;
    }

    public void setId(final String id)
    {
        this.id = id;
    }

    public String getContent()
    {
        return content;
    }

    public void setContent(final String content)
    {
        this.content = content;
    }

    public Date getPostDate()
    {
        return postDate;
    }

    public void setPostDate(final Date postDate)
    {
        this.postDate = postDate;
    }

    public Date getCloseDate()
    {
        return closeDate;
    }

    public void setCloseDate(final Date closeDate)
    {
        this.closeDate = closeDate;
    }

    public double getLatitude()
    {
        return latitude;
    }

    public void setLatitude(final double latitude)
    {
        this.latitude = latitude;
    }

    public double getLongitude()
    {
        return longitude;
    }

    public void setLongitude(final double longitude)
    {
        this.longitude = longitude;
    }

    public User getReporter()
    {
        return reporter;
    }

    public void setReporter(final User reporter)
    {
        this.reporter = reporter;
    }

    public ReportType getReportType()
    {
        return reportType;
    }

    public void setReportType(final ReportType reportType)
    {
        this.reportType = reportType;
    }

    public int getUpVote()
    {
        return upVote;
    }

    public void setUpVote(final int upVote)
    {
        this.upVote = upVote;
    }

    public int getDownVote()
    {
        return downVote;
    }

    public void setDownVote(final int downVote)
    {
        this.downVote = downVote;
    }
}
