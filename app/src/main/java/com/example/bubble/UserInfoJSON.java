package com.example.bubble;

import android.widget.ImageView;

public class UserInfoJSON {

    String login;
    public String name;
    public String info;
    public ImageView profilePicture;

    UserInfoJSON(String login){
        this.login=login;
    }

}
