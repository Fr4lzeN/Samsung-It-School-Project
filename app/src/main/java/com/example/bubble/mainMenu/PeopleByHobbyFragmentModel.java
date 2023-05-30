package com.example.bubble.mainMenu;

import android.annotation.SuppressLint;

import androidx.lifecycle.MutableLiveData;

import com.example.bubble.JSON.FriendInfo;

import java.util.ArrayList;

import io.reactivex.disposables.Disposable;
import io.reactivex.subjects.PublishSubject;

public class PeopleByHobbyFragmentModel {


    @SuppressLint("CheckResult")
    public static ArrayList<Disposable> getUsers(String hobby, MutableLiveData<ArrayList<FriendInfo>> data) {

        PublishSubject<String>  uid = PublishSubject.create();
        PublishSubject<FriendInfo> userData = PublishSubject.create();
        FirebaseActions.getUserUidByHobby(hobby, uid);

        Disposable uidDisposable = uid.subscribe(
                userUid->{
                    FirebaseActions.getUser(userUid, userData);
                }
        );

       Disposable userDisposable = userData.subscribe(
                user->{
                    ArrayList<FriendInfo> temp = new ArrayList<>();
                    if (data.getValue()!=null)
                        temp = data.getValue();
                    temp.add(user);
                    data.setValue(temp);
                }
        );

       ArrayList<Disposable> disposables = new ArrayList<>();
       disposables.add(uidDisposable);
       disposables.add(userDisposable);
       return disposables;
    }

    public static void disableDisposables(ArrayList<Disposable> disposables) {
        for (Disposable i : disposables){
            i.dispose();
        }
        disposables = null;
    }

    public static void deleteListeners(String hobby) {
        FirebaseActions.userUidByHobbyDelete(hobby);
    }
}
