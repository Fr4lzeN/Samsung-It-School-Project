package com.example.bubble.mainMenu;

import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.StorageReference;

public class SettingsFragmentModel {



    public static void downloadPicture(MutableLiveData<FirebaseUser> user, MutableLiveData<StorageReference> picture) {
        FirebaseActions.downloadPicture(user.getValue(), picture);
    }
}
