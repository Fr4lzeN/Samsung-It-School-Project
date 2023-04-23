package com.example.bubble.mainMenu;

import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.example.bubble.JSON.UserInfoJSON;
import com.example.bubble.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PeopleByHobbyFragmentViewModel extends ViewModel {

    MutableLiveData<List<String>> uids = new MutableLiveData<>();
    MutableLiveData<Map<String, UserInfoJSON>> data = new MutableLiveData<>();
    MutableLiveData<PeopleListRecyclerView> adapter = new MutableLiveData<>();
    Fragment fragment;

    public void createAdapter(Fragment fragment, String hobby){
        this.fragment=fragment;
        PeopleByHobbyFragmentModel.getData(fragment, hobby, uids, data, adapter);

    }

}
