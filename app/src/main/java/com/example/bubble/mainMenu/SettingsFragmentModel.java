package com.example.bubble.mainMenu;

import android.net.Uri;

import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.StorageReference;

public class SettingsFragmentModel {



    public static void downloadPicture(MutableLiveData<FirebaseUser> user, MutableLiveData<Uri> picture) {
        FirebaseActions.downloadPicture(user.getValue().getUid(), picture);
    }
}
