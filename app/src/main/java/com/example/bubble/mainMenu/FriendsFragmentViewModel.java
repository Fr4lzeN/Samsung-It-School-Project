package com.example.bubble.mainMenu;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class FriendsFragmentViewModel extends ViewModel {

    MutableLiveData<PeopleListRecyclerView> friendsAdapter = new MutableLiveData<>();
    MutableLiveData<PeopleListRecyclerView> outcomingAdapter = new MutableLiveData<>();
    MutableLiveData<PeopleListRecyclerView> incomingAdapter = new MutableLiveData<>();



    public void  createAdapters(Fragment fragment){
        friendsAdapter.setValue(null);
        outcomingAdapter.setValue(null);
        incomingAdapter.setValue(null);
        FriendsFragmentModel.createAdapters(fragment, friendsAdapter, outcomingAdapter, incomingAdapter);
    }

}
