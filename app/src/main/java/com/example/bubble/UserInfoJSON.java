package com.example.bubble;

import android.widget.ImageView;

public class UserInfoJSON {

    public String name;
    public String info;
    public String gender;
    public DateOfBirth dateOfBirth;

    public UserInfoJSON(String name, String info, String gender, int year, int month, int day) {
        this.name = name;
        this.info = info;
        this.dateOfBirth = new DateOfBirth(year, month, day);
        this.gender=gender;
    }

    public class DateOfBirth{
        public int year;
        public int month;
        public int day;

        public DateOfBirth(int year, int month, int day) {
            this.year = year;
            this.month = month;
            this.day = day;
        }
    }


}
