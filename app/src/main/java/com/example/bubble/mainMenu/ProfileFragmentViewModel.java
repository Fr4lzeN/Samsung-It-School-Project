package com.example.bubble.mainMenu;

import android.content.Context;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.bubble.JSON.UserInfoJSON;
import com.google.firebase.storage.StorageReference;

import java.util.List;

public class ProfileFragmentViewModel extends ViewModel {

    MutableLiveData<List<StorageReference>> data = new MutableLiveData<>();
    MutableLiveData<String> uid = new MutableLiveData<>();
    MutableLiveData<FriendStatusEnum> friendStatus = new MutableLiveData<>();

    MutableLiveData<UserInfoJSON> userInfo = new MutableLiveData<>();
    MutableLiveData<List<String>> hobbies = new MutableLiveData<>();

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
            ProfileFragmentModel.changeFriendStatus(friendStatus, uid.getValue(), context);

    }
}
