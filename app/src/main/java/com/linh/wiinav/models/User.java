package com.linh.wiinav.models;


import com.google.firebase.database.IgnoreExtraProperties;

import java.io.Serializable;

@IgnoreExtraProperties
public class User implements Serializable {
    private String id, email, username, phoneNumber, birthday, password;
    private Long identifyCard;
    private boolean verifiedEmail, ban, verifiedPhoneNumber;
    private int numberAsk;

    public User() {
    }

    public User(String id, String email, String username,
                String phoneNumber, String birthday, Long identifyCard,
                boolean verifiedEmail, boolean ban, boolean verifiedPhoneNumber,
                int numberAsk, String password) {
        this.id = id;
        this.email = email;
        this.username = username;
        this.phoneNumber = phoneNumber;
        this.birthday = birthday;
        this.identifyCard = identifyCard;
        this.verifiedEmail = verifiedEmail;
        this.ban = ban;
        this.verifiedPhoneNumber = verifiedPhoneNumber;
        this.numberAsk = numberAsk;
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public boolean isVerifiedEmail() {
        return verifiedEmail;
    }

    public void setVerifiedEmail(boolean verifiedEmail) {
        this.verifiedEmail = verifiedEmail;
    }

    public boolean isBan() {
        return ban;
    }

    public void setBan(boolean ban) {
        this.ban = ban;
    }

    public boolean isVerifiedPhoneNumber() {
        return verifiedPhoneNumber;
    }

    public void setVerifiedPhoneNumber(boolean verifiedPhoneNumber) {
        this.verifiedPhoneNumber = verifiedPhoneNumber;
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
                "id='" + id + '\'' +
                ", email='" + email + '\'' +
                ", username='" + username + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", birthday='" + birthday + '\'' +
                ", password='" + password + '\'' +
                ", identifyCard=" + identifyCard +
                ", verifiedEmail=" + verifiedEmail +
                ", ban=" + ban +
                ", verifiedPhoneNumber=" + verifiedPhoneNumber +
                ", numberAsk=" + numberAsk +
                '}';
    }
}