package com.linh.wiinav.models;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@IgnoreExtraProperties
public class Report {
    private String id, content, title;
    private Date postDate, closeDate;
    private double latitude, longitude;
    private User reporter;
    private ReportType reportType;
    private int upVote, downVote;
    private Long remainingTime;
    private List<String> imageName;

    public Report() {
    }

    public Report(final String id, final String content, final String title,
                  final Date postDate, final Date closeDate, final double latitude,
                  final double longitude, final User reporter, final ReportType reportType,
                  final int upVote, final int downVote, final Long remainingTime)
    {
        this.id = id;
        this.content = content;
        this.title = title;
        this.postDate = postDate;
        this.closeDate = closeDate;
        this.latitude = latitude;
        this.longitude = longitude;
        this.reporter = reporter;
        this.reportType = reportType;
        this.upVote = upVote;
        this.downVote = downVote;
        this.remainingTime = remainingTime;
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

    public String getTitle()
    {
        return title;
    }

    public void setTitle(final String title)
    {
        this.title = title;
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

    public Long getRemainingTime()
    {
        return remainingTime;
    }

    public void setRemainingTime(final Long remainingTime)
    {
        this.remainingTime = remainingTime;
    }

    public List<String> getImageName() {
        return imageName;
    }

    public void setImageName(List<String> imageName) {
        this.imageName = imageName;
    }
}