package com.example.ourproject.ALL_Notification;

public class A_Notification {

    private String title;
    private String body;
    private int icon;
//    private String sound;
//    private String image;

    public A_Notification() {

    }

    public A_Notification(String title, String body, int icon) {
        this.title = title;
        this.body = body;
        this.icon=icon;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }
}
