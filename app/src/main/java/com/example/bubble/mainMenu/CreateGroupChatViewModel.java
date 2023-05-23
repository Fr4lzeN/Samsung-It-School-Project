package com.example.bubble.mainMenu;

import android.net.Uri;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.bubble.JSON.FriendInfo;

import java.util.ArrayList;
import java.util.List;

public class CreateGroupChatViewModel extends ViewModel {

    MutableLiveData<List<FriendInfo>> friendList = new MutableLiveData<>();
    List<String> selectedPeople = new ArrayList<>();

    Uri picture;

    public void setFriends(List<FriendInfo> friendList) {
        this.friendList.setValue(friendList);
    }

    public void addUid(String uid) {
        if (selectedPeople.contains(uid)){
            selectedPeople.remove(uid);
        }
        else{
            selectedPeople.add(uid);
        }
    }

    public void createChat(String s){
        FirebaseActions.createGroupChat(s,selectedPeople, picture);
    }

    public void setUri(Uri uri) {
        this.picture=uri;
    }
}
