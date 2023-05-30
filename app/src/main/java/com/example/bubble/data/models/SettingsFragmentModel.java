package com.example.bubble.data.models;

import android.net.Uri;

import androidx.lifecycle.MutableLiveData;

import com.example.bubble.data.firebase.FirebaseActions;
import com.google.firebase.auth.FirebaseUser;

public class SettingsFragmentModel {



    public static void downloadPicture(MutableLiveData<FirebaseUser> user, MutableLiveData<Uri> picture) {
        FirebaseActions.downloadPicture(user.getValue().getUid(), picture);
    }
}
