package com.example.bubble.mainMenu;

import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.example.bubble.JSON.FriendInfo;
import com.example.bubble.JSON.UserInfoJSON;
import com.example.bubble.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import io.reactivex.disposables.Disposable;

public class PeopleByHobbyFragmentViewModel extends ViewModel {

    MutableLiveData<ArrayList<FriendInfo>> data = new MutableLiveData<>();
    String hobby;
    ArrayList<Disposable> disposables;

    public void getUsers(){
        disposables = PeopleByHobbyFragmentModel.getUsers(hobby, data);
    }

    public  void setHobby(String hobby){
        this.hobby = hobby;
    }

    public void disable(){
        PeopleByHobbyFragmentModel.disableDisposables(disposables);
        PeopleByHobbyFragmentModel.deleteListeners(hobby);
    }

}
