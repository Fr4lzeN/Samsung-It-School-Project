package com.example.bubble.mainMenu;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.bubble.JSON.FriendInfo;
import com.example.bubble.JSON.MessageListItem;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;


public class MainActivityViewModel extends ViewModel {


    FirebaseAuth mAuth;
    MutableLiveData<FirebaseUser> user = new MutableLiveData<>();
    MutableLiveData<ArrayList<MessageListItem>> allMessagesData = new MutableLiveData<>();

    MutableLiveData<ArrayList<String>> hobbyList = new MutableLiveData<>();



    MutableLiveData<List<FriendInfo>> friends = new MutableLiveData<>();
    MutableLiveData<Boolean> friendRecyclerIsClicked = new MutableLiveData<>();
    MutableLiveData<List<FriendInfo>> outgoingRequests = new MutableLiveData<>();
    MutableLiveData<Boolean> outgoingRecyclerIsClicked = new MutableLiveData<>();
    MutableLiveData<List<FriendInfo>> incomingRequests = new MutableLiveData<>();
    MutableLiveData<Boolean> incomingRecyclerIsClicked = new MutableLiveData<>();




    public void auth() {
        mAuth = FirebaseAuth.getInstance();
        user.setValue(mAuth.getCurrentUser());
    }

    public void getMessageData() {
        MainActivityModel.getMessageData(mAuth.getUid(),allMessagesData);
    }

    public void getFriendList(){
        MainActivityModel.getFriendList(mAuth.getUid(),friends,outgoingRequests,incomingRequests);
    }



    public void setRecyclerState(Boolean visibility, Boolean visibility1, Boolean visibility2) {
        friendRecyclerIsClicked.setValue(visibility);
        incomingRecyclerIsClicked.setValue(visibility1);
        outgoingRecyclerIsClicked.setValue(visibility2);
    }

    public void getHobbies(){
        MainActivityModel.getHobbies(hobbyList);
    }

    public String getUid() {
        return mAuth.getUid();
    }
}
