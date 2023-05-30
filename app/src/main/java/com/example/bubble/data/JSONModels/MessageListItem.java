package com.example.bubble.data.JSONModels;

import android.net.Uri;

import androidx.annotation.NonNull;

public class MessageListItem {

    public UserInfoJSON userInfo;
    public MessageJSON message;
    public Uri picture;

    public String messageId;

    @NonNull
    @Override
    public String toString() {
        return messageId+" "+uid;
    }

    public String uid;

    public void setPicture(Uri picture) {
        this.picture = picture;
    }

    public void setMessage(MessageJSON message) {
        this.message = message;
    }

    public void setUserInfo(UserInfoJSON userInfo) {
        this.userInfo = userInfo;
    }
}
