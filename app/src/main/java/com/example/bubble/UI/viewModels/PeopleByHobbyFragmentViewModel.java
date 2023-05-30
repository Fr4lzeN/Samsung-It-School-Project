package com.example.bubble.UI.viewModels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.bubble.data.JSONModels.FriendInfo;
import com.example.bubble.data.models.PeopleByHobbyFragmentModel;

import java.util.ArrayList;

import io.reactivex.disposables.Disposable;

public class PeopleByHobbyFragmentViewModel extends ViewModel {

    public MutableLiveData<ArrayList<FriendInfo>> data = new MutableLiveData<>();
    String hobby;
    ArrayList<Disposable> disposables;

    public void getUsers(){
        disposables = PeopleByHobbyFragmentModel.getUsers(hobby, data);
    }

    public  void setHobby(String hobby){
        this.hobby = hobby;
    }

    public boolean isDisposalesExist(){
        return disposables!=null;
    }

    public void disable(){
        PeopleByHobbyFragmentModel.disableDisposables(disposables);
        PeopleByHobbyFragmentModel.deleteListeners(hobby);
    }

}
