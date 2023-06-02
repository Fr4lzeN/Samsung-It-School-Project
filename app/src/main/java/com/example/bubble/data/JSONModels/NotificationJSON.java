package com.example.bubble.data.JSONModels;

public class NotificationJSON {
    String to;
    Notification notification;

    public NotificationJSON(String to, Notification notification) {
        this.to = to;
        this.notification = notification;
    }

    public NotificationJSON(String to, String body, String title){
        this.to=to;
        this.notification = new Notification(body,title);
    }

    public class Notification{
        String body;
        String title;

        public Notification(String body, String title) {
            this.body = body;
            this.title = title;
        }
    }
}
