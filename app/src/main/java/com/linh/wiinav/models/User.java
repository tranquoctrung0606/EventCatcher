package com.linh.wiinav.models;

import com.google.firebase.database.IgnoreExtraProperties;

import java.io.Serializable;
import java.util.Date;

@IgnoreExtraProperties
public class User implements Serializable {
    private String email, username, phoneNumber;
    private String birthday;
    private Long identifyCard;
    private boolean verify, ban;
    private int numberAsk;;

    public User() {
    }

    public User(String email, String username, String phoneNumber, String birthday, Long identifyCard, boolean verify, boolean ban, int numberAsk) {
        this.email = email;
        this.username = username;
        this.phoneNumber = phoneNumber;
        this.birthday = birthday;
        this.identifyCard = identifyCard;
        this.verify = verify;
        this.ban = ban;
        this.numberAsk = numberAsk;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public Long getIdentifyCard() {
        return identifyCard;
    }

    public void setIdentifyCard(Long identifyCard) {
        this.identifyCard = identifyCard;
    }

    public boolean isVerify() {
        return verify;
    }

    public void setVerify(boolean verify) {
        this.verify = verify;
    }

    public boolean isBan() {
        return ban;
    }

    public void setBan(boolean ban) {
        this.ban = ban;
    }

    public int getNumberAsk() {
        return numberAsk;
    }

    public void setNumberAsk(int numberAsk) {
        this.numberAsk = numberAsk;
    }

    @Override
    public String toString() {
        return "User{" +
                "email='" + email + '\'' +
                ", username='" + username + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", birthday=" + birthday +
                ", identifyCard=" + identifyCard +
                ", verify=" + verify +
                ", ban=" + ban +
                ", numberAsk=" + numberAsk +
                '}';
    }
}
