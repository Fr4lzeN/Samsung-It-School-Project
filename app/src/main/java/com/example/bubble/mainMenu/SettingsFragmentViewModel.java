package com.example.bubble.mainMenu;

import android.net.Uri;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.bubble.JSON.UserInfoJSON;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.StorageReference;

import java.util.List;

public class SettingsFragmentViewModel extends ViewModel {

    public MutableLiveData<FirebaseUser> user = new MutableLiveData<>(FirebaseAuth.getInstance().getCurrentUser());
    public MutableLiveData<Uri> picture = new MutableLiveData<>();

    public void downloadPicture(){
        SettingsFragmentModel.downloadPicture(user, picture);
    }


    public String getUid() {
        return user.getValue().getUid();
    }

    public void refreshUser(){
        user.setValue(FirebaseAuth.getInstance().getCurrentUser());
    }
}
