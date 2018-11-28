package com.linh.wiinav.models;

public class Duration implements Comparable<Duration>{
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

    private String text;
    private int value;

    public Duration(String text, int value) {
        this.text = text;
        this.value = value;
    }

    public int compareTo(Duration duration){
        if (this.getValue() == duration.getValue())
            return 0;
        if (this.getValue() > duration.getValue())
            return 1;
        return -1;
    }
}
