package com.example.bubble.registration;

import android.net.Uri;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.bubble.JSON.UserInfoJSON;
import com.example.bubble.mainMenu.FirebaseActions;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FillDataModel {

    FirebaseAuth auth;
    FirebaseUser user;
    DatabaseReference database;
    StorageReference storage;
    ArrayList<String> hobby;
    ArrayList<Boolean> checked;

    public FillDataModel() {
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        database = FirebaseDatabase.getInstance().getReference();
        storage = FirebaseStorage.getInstance().getReference();
    }

    public void createHobbyAdapter(MutableLiveData<HobbyRecyclerView> hobbiesAdapter, MutableLiveData<Boolean> chipChecked) {

        FirebaseActions.getHobbyList().addOnCompleteListener(task -> {
            hobby=task.getResult().getValue(new GenericTypeIndicator<ArrayList<String>>() {});
            checked = new ArrayList<>(Collections.nCopies(hobby.size(),false));
            hobbiesAdapter.setValue(new HobbyRecyclerView(hobby, checked, chipChecked::setValue));
        });

    }

    public Task<Void> createUser(String name, String info, String gender, Integer[] dateOfBirth) {
        UserInfoJSON userInfoJSON = new UserInfoJSON(name, info, gender, dateOfBirth[0],dateOfBirth[1],dateOfBirth[2]);
        return FirebaseActions.createUserDatabase(database, user.getUid(), userInfoJSON);
    }

    public Task<Void> changeUserProfilePicture(String name, Uri uri) {
        return FirebaseActions.updateUserProfile(user, name, uri);
    }

    public Task<Void> uploadUserPictures(List<Uri> uris) {
       return FirebaseActions.uploadUserPictures(storage, user.getUid(), uris);
    }

    public Task<Void> uploadHobby(){
        return FirebaseActions.setHobbies(hobby,checked);
    }

}
