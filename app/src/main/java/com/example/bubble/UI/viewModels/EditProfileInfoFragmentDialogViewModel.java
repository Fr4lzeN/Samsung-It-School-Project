package com.example.bubble.UI.viewModels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.bubble.data.models.EditProfileInfoFragmentDialogModel;
import com.example.bubble.data.JSONModels.UserInfoJSON;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

public class EditProfileInfoFragmentDialogViewModel extends ViewModel {

    MutableLiveData<FirebaseUser> user = new MutableLiveData<>(FirebaseAuth.getInstance().getCurrentUser());
    public MutableLiveData<UserInfoJSON> userInfo = new MutableLiveData<>();

    List<Boolean> sendResults = new ArrayList<>();
    public  MutableLiveData<Boolean> result = new MutableLiveData<>();


    public void downloadUserInfo(){
        EditProfileInfoFragmentDialogModel.downloadUserInfo(user, userInfo);
    }


    public void updateInfo(String name, String info, String dateOfBirth) {
        EditProfileInfoFragmentDialogModel.updateInfo(result, sendResults, userInfo, user, name, info, dateOfBirth.split("/"));
    }

    public void setGender(String gender) {
        UserInfoJSON tempUser = userInfo.getValue();
        tempUser.gender=gender;
        userInfo.setValue(tempUser);
    }
}
