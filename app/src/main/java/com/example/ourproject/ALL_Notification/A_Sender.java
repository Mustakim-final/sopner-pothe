package com.example.ourproject.ALL_Notification;

public class A_Sender {
    public String to;
    public A_Data data;
    public A_Notification notification;

    public A_Sender(A_Data data, A_Notification notification) {
        this.data = data;
        this.notification = notification;
    }
}
