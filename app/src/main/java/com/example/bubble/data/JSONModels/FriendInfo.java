package com.example.bubble.data.JSONModels;

import android.net.Uri;

public class FriendInfo {

    UserInfoJSON userData;
    String uid;
    Uri picture;

    public FriendInfo(String uid) {
        this.uid = uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public FriendInfo() {
    }

    public void setUserData(UserInfoJSON userData) {
        this.userData = userData;
    }

    public void setPicture(Uri picture) {
        this.picture = picture;
    }

    public Uri getPicture() {
        return picture;
    }

    public UserInfoJSON getUserData() {
        return userData;
    }

    @Override
    public String toString() {
        return "FriendInfo{" +
                "userData=" + userData +
                ", uid='" + uid + '\'' +
                ", picture=" + picture +
                '}';
    }

    public String getUid() {
        return uid;
    }
}
