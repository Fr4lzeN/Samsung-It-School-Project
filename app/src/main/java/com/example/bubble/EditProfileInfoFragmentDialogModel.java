package com.example.bubble;

import androidx.lifecycle.MutableLiveData;

import com.example.bubble.JSON.UserInfoJSON;
import com.example.bubble.mainMenu.FirebaseActions;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class EditProfileInfoFragmentDialogModel {
    public static void downloadUserInfo(MutableLiveData<FirebaseUser> user, MutableLiveData<UserInfoJSON> userInfo) {
        FirebaseActions.downloadUserInfo(user.getValue().getUid(), userInfo);
    }


    public static void updateInfo(MutableLiveData<Boolean> result, List<Boolean> sendResults, MutableLiveData<UserInfoJSON> userInfo, MutableLiveData<FirebaseUser> user, String name, String info, String[] dateOfBirth) {
        FirebaseActions.updateUserProfile(result, sendResults, user.getValue(), name);
        FirebaseActions.createUserDB(result, sendResults, userInfo, user.getValue(), name, info, dateOfBirth);
    }
}
