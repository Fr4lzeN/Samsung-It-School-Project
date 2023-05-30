package com.example.bubble.mainMenu;

import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.bubble.JSON.UserInfoJSON;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.storage.StorageReference;

import java.util.List;

public class SettingsFragmentViewModel extends ViewModel {

    public MutableLiveData<FirebaseUser> user = new MutableLiveData<>(FirebaseAuth.getInstance().getCurrentUser());
    public MutableLiveData<Uri> picture = new MutableLiveData<>();
    public MutableLiveData<Boolean> privacy = new MutableLiveData<>();

    public void downloadPicture(){
        SettingsFragmentModel.downloadPicture(user, picture);
    }


    public String getUid() {
        return user.getValue().getUid();
    }

    public void refreshUser(){
        user.setValue(FirebaseAuth.getInstance().getCurrentUser());
    }

    public void getPrivacy() {
        FirebaseActions.getUserPrivacy(user.getValue().getUid()).addOnCompleteListener(task ->
                privacy.setValue(task.getResult().getValue(Boolean.class)));
    }

    public void setPrivacy(boolean checked) {
        privacy.setValue(checked);
        FirebaseActions.setPrivateAccount(checked);
    }
}
