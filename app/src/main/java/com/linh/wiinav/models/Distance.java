package com.linh.wiinav.models;

public class Distance implements Comparable<Distance>{
    private String text;
    private int value;

    public Distance(String text, int value) {
        this.text = text;
        this.value = value;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "Distance{" +
                "text='" + text + '\'' +
                ", value=" + value +
                '}';
    }

    @Override
    public int compareTo(Distance o) {
        if (this.value > o.getValue())
            return 1;
        if (this.value == o.getValue())
            return 0;
        return -1;
    }
}
