package com.linh.wiinav.models;

import java.io.Serializable;

public class UserType implements Serializable {

    private String id,name;
    private float price;
    private int numberAsk;

    public UserType() {
    }

    public UserType(String id, String name, float price, int numberAsk) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.numberAsk = numberAsk;
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

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public int getNumberAsk() {
        return numberAsk;
    }

    public void setNumberAsk(int numberAsk) {
        this.numberAsk = numberAsk;
    }

    @Override
    public String toString() {
        return "UserType{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", numberAsk=" + numberAsk +
                '}';
    }
}
