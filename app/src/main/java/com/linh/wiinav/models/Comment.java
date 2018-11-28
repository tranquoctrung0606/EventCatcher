package com.linh.wiinav.models;

import java.io.Serializable;
import java.util.Date;

public class Comment implements Serializable{
    private String commentId;
    private User commentator;
    private String content;
    private Date commentDate;

    public Comment() {
    }

    public Comment(String commentId, User commentator, String content, Date commentDate) {
        this.commentId = commentId;
        this.commentator = commentator;
        this.content = content;
        this.commentDate = commentDate;
    }

    public String getCommentId() {
        return commentId;
    }

    public void setCommentId(String commentId) {
        this.commentId = commentId;
    }

    public User getCommentator() {
        return commentator;
    }

    public void setCommentator(User commentator) {
        this.commentator = commentator;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getCommentDate() {
        return commentDate;
    }

    public void setCommentDate(Date commentDate) {
        this.commentDate = commentDate;
    }
}
