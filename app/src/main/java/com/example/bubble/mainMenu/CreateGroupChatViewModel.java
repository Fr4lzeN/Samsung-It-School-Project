package com.example.bubble.mainMenu;

import android.net.Uri;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.bubble.JSON.FriendInfo;

import java.util.ArrayList;
import java.util.List;

public class CreateGroupChatViewModel extends ViewModel {

    MutableLiveData<List<FriendInfo>> friendList = new MutableLiveData<>();
    MutableLiveData<List<String>> selectedPeople = new MutableLiveData<>();
    MutableLiveData<String> chatName = new MutableLiveData<>();

    MutableLiveData<Uri> picture = new MutableLiveData<>();

    public void setFriends(List<FriendInfo> friendList) {
        this.friendList.setValue(friendList);
    }

    public void addUid(String uid) {
        List<String> temp = selectedPeople.getValue();
        if (temp==null){
            temp = new ArrayList<>();
            temp.add(uid);
        }else
        if (temp.contains(uid)){
            temp.remove(uid);
        }
        else{
            temp.add(uid);
        }
        selectedPeople.setValue(temp);
    }

    public void createChat(String s){
        Log.d("Chat", selectedPeople.toString());
        FirebaseActions.createGroupChat(s,selectedPeople.getValue(), picture.getValue());
    }

    public void setUri(Uri uri) {
        this.picture.setValue(uri);
    }

    public void setName(String toString) {
        chatName.setValue(toString);
    }
}
