package com.example.bubble.UI.viewModels;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.bubble.data.JSONModels.FriendInfo;
import com.example.bubble.data.JSONModels.GroupMessageListItem;
import com.example.bubble.data.JSONModels.MessageListItem;
import com.example.bubble.data.models.MainActivityModel;
import com.example.bubble.tools.ENUMs.FriendStatusEnum;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;


public class MainActivityViewModel extends ViewModel {


    FirebaseAuth mAuth;
    public Boolean admin = false;
    public MutableLiveData<FirebaseUser> user = new MutableLiveData<>();
    public MutableLiveData<ArrayList<MessageListItem>> allMessagesData = new MutableLiveData<>(new ArrayList<>());
    public MutableLiveData<ArrayList<GroupMessageListItem>> allGroupMessagesData = new MutableLiveData<>(new ArrayList<>());

    public MutableLiveData<ArrayList<String>> hobbyList = new MutableLiveData<>();


    public MutableLiveData<List<FriendInfo>> friends = new MutableLiveData<>();
    public MutableLiveData<List<FriendInfo>> outgoingRequests = new MutableLiveData<>();
    public MutableLiveData<List<FriendInfo>> incomingRequests = new MutableLiveData<>();

    public MutableLiveData<FriendStatusEnum> friendStatusShowing = new MutableLiveData<>(FriendStatusEnum.FRIENDS);
    public MutableLiveData<String> searchText = new MutableLiveData<>();

    public MutableLiveData<Boolean> groupChatSelected = new MutableLiveData<>(false);




    public void auth() {
        mAuth = FirebaseAuth.getInstance();
        user.setValue(mAuth.getCurrentUser());
        if (mAuth.getUid()!=null){
            FirebaseDatabase.getInstance().getReference("userData").child(mAuth.getUid()).child("userInfo").child("admin").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    if (task.isSuccessful()){
                        admin = task.getResult().getValue(Boolean.class);
                    }
                }
            });
        }

    }

    public void getMessageData() {
        MainActivityModel.getMessageData(mAuth.getUid(),allMessagesData);
    }

    public void getGroupMessageData(){
        MainActivityModel.getGroupMessageData(mAuth.getUid(),allGroupMessagesData);
    }

    public void getFriendList(){
        MainActivityModel.getFriendList(mAuth.getUid(),friends,outgoingRequests,incomingRequests);
    }

    public void getHobbies(){
        MainActivityModel.getHobbies(hobbyList);
    }

    public String getUid() {
        return mAuth.getUid();
    }

    public void setShowingFriends(FriendStatusEnum friends) {
        friendStatusShowing.setValue(friends);
    }

    public FriendStatusEnum getFriendStatus() {
        return friendStatusShowing.getValue();
    }

    public void setSearchText(String search) {
        searchText.setValue(search);
    }

    public void setGroupChatSelected(boolean b) {
        groupChatSelected.setValue(b);
    }
}
