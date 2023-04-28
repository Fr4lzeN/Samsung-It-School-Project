package com.example.bubble.JSON;

import android.net.Uri;

import com.google.firebase.storage.StorageReference;

public class MessageListItem {

    public UserInfoJSON userInfo;
    public MessageJSON message;
    public Uri picture;

    public String messageId;
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
