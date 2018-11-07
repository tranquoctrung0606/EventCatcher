package com.linh.wiinav.models;

import java.io.Serializable;

public class Comment implements Serializable{
    private String commentId;
    private User commentator;
    private String comment;
    private String commentDate;

    public Comment() {
    }

    public Comment(String commentId, User commentator, String comment, String commentDate) {
        this.commentId = commentId;
        this.commentator = commentator;
        this.comment = comment;
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

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getCommentDate() {
        return commentDate;
    }

    public void setCommentDate(String commentDate) {
        this.commentDate = commentDate;
    }
}
