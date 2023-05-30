package com.example.bubble.JSON;

import android.net.Uri;

public class GroupMessageListItem extends MessageListItem{

    public String groupChatId;
    public String chatName;
    public String userName;

    public GroupMessageListItem() {
    }

    public GroupMessageListItem(String groupChatId) {
        this.groupChatId = groupChatId;
    }

    public void setMessage(MessageJSON message) {
        this.message = message;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setChatName(String chatName) {
        this.chatName = chatName;
    }

    public void setPicture(Uri picture) {
        this.picture = picture;
    }
}
