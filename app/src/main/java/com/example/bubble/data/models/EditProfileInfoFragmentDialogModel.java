package com.example.bubble.data.models;

import androidx.lifecycle.MutableLiveData;

import com.example.bubble.data.JSONModels.UserInfoJSON;
import com.example.bubble.data.firebase.FirebaseActions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class EditProfileInfoFragmentDialogModel {
    public static void downloadUserInfo(MutableLiveData<FirebaseUser> user, MutableLiveData<UserInfoJSON> userInfo) {
        FirebaseActions.downloadUserInfo(user.getValue().getUid(), userInfo);
    }

    public static void downloadUserInfo(String uid, MutableLiveData<UserInfoJSON> userInfo) {
        FirebaseActions.downloadUserInfo(uid, userInfo);
    }


    public static void updateInfo(MutableLiveData<Boolean> result, List<Boolean> sendResults, MutableLiveData<UserInfoJSON> userInfo, MutableLiveData<FirebaseUser> user, String name, String info, String[] dateOfBirth, String uid) {
        if (uid.equals(FirebaseAuth.getInstance().getUid())){
            FirebaseActions.updateUserProfile(result, sendResults, user.getValue(), name);
        }else{
            sendResults.add(true);
        }
        FirebaseActions.createUserDB(result, sendResults, userInfo, uid, name, info, dateOfBirth);
    }
}
