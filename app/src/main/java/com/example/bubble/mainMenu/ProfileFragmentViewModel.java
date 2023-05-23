package com.example.bubble.mainMenu;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.bubble.JSON.UserInfoJSON;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

import java.util.List;

public class ProfileFragmentViewModel extends ViewModel {

    MutableLiveData<List<StorageReference>> data = new MutableLiveData<>();
    MutableLiveData<String> uid = new MutableLiveData<>();
    MutableLiveData<FriendStatusEnum> friendStatus = new MutableLiveData<>();

    public MutableLiveData<UserInfoJSON> userInfo = new MutableLiveData<>();
    public MutableLiveData<List<String>> hobbies = new MutableLiveData<>();

    public void setUid(String uid) {
        this.uid.setValue(uid);
    }

    public void downloadPictures(){
        ProfileFragmentModel.downloadPictures(uid, data);
    }

    public void downloadUserData() {
        ProfileFragmentModel.downloadUserData(uid, userInfo);
    }

    public  void getHobbies(){
        ProfileFragmentModel.getHobbies(uid, hobbies);
    }

    public void checkFriendStatus() {
        ProfileFragmentModel.checkFriendStatus(friendStatus, uid.getValue());
    }

    public void addFriendButton(Context context) {
            ProfileFragmentModel.changeFriendStatus(friendState, uid.getValue(), context);

    }

    MutableLiveData<FriendStatusEnum> friendState = new MutableLiveData<>();

    public void getFriendState() {
        FirebaseActions.subscribeOnFriendStatus(FirebaseAuth.getInstance().getUid(), uid.getValue()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                friendState.setValue(snapshot.getValue(FriendStatusEnum.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public UserInfoJSON getUserData() {
        return userInfo.getValue();
    }
}
