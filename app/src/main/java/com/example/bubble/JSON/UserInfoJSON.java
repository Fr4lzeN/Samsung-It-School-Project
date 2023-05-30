package com.example.bubble.JSON;

import android.net.Uri;
import android.widget.ImageView;

public class UserInfoJSON {

    public String name;
    public String info;
    public String gender;
    public DateOfBirth dateOfBirth;

    public UserInfoJSON() {
    }

    public UserInfoJSON(String name, String info, String gender, int year, int month, int day) {
        this.name = name;
        this.info = info;
        this.dateOfBirth = new DateOfBirth(year, month, day);
        this.gender=gender;
    }

    @Override
    public String toString() {
        return "UserInfoJSON{" +
                "name='" + name + '\'' +
                ", info='" + info + '\'' +
                ", gender='" + gender + '\'' +
                ", dateOfBirth=" + dateOfBirth +
                '}';
    }

    public static class DateOfBirth{
        public int year;
        public int month;
        public int day;

        public DateOfBirth() {
        }

        public DateOfBirth(int year, int month, int day) {
            this.year = year;
            this.month = month;
            this.day = day;
        }
    }


}
