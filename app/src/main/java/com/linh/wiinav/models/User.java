package com.linh.wiinav.models;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.Date;

@IgnoreExtraProperties
public class User {
    private String email, username, phone;
    private Date birthday;

    public User()
    {
    }

    public User(final String email, final String username, final String phone, final Date birthday)
    {
        this.email = email;
        this.username = username;
        this.phone = phone;
        this.birthday = birthday;
    }

    public String getEmail()
    {
        return email;
    }

    public String getUsername()
    {
        return username;
    }

    public String getPhone()
    {
        return phone;
    }

    public Date getBirthday()
    {
        return birthday;
    }

    public void setEmail(final String email)
    {
        this.email = email;
    }

    public void setUsername(final String username)
    {
        this.username = username;
    }

    public void setPhone(final String phone)
    {
        this.phone = phone;
    }

    public void setBirthday(final Date birthday)
    {
        this.birthday = birthday;
    }

}
