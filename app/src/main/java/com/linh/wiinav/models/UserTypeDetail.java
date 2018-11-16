package com.linh.wiinav.models;

import java.io.Serializable;
import java.util.Date;

public class UserTypeDetail implements Serializable{

    private String id;
    private User user;
    private UserType userType;
    private Date registerDate, expirationDate;

    public UserTypeDetail() {
    }

    public UserTypeDetail(String id, User user, UserType userType, Date registerDate, Date expirationDate) {
        this.id = id;
        this.user = user;
        this.userType = userType;
        this.registerDate = registerDate;
        this.expirationDate = expirationDate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public UserType getUserType() {
        return userType;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }

    public Date getRegisterDate() {
        return registerDate;
    }

    public void setRegisterDate(Date registerDate) {
        this.registerDate = registerDate;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
    }

    @Override
    public String toString() {
        return "UserTypeDetail{" +
                "id='" + id + '\'' +
                ", user=" + user +
                ", userType=" + userType +
                ", registerDate=" + registerDate +
                ", expirationDate=" + expirationDate +
                '}';
    }
}
