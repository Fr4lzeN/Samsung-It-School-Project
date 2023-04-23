package com.example.bubble.mainMenu;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class FriendsFragmentViewModel extends ViewModel {

    PeopleListRecyclerView friendsAdapter;
    PeopleListRecyclerView outcomingAdapter;
    PeopleListRecyclerView incomingAdapter;

    MutableLiveData<Boolean> allDataDownloaded = new MutableLiveData<>();

    public void  createAdapters(){
        FriendsFragmentModel.createAdapters();
    }

}
