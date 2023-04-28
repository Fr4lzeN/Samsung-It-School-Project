package com.example.bubble.JSON;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class MessageJSON {

    public String message;
    public String uid;
    public long date;
    public MessageJSON(String message, String uid) {
        this.message = message;
        this.uid = uid;
        date = new GregorianCalendar().getTimeInMillis();
    }

    public MessageJSON() {
    }

    public void setDate(long date) {
        this.date = date;
    }
}
