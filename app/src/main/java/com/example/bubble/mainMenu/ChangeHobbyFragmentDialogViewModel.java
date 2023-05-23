package com.example.bubble.mainMenu;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.bubble.registration.HobbyRecyclerView;

public class ChangeHobbyFragmentDialogViewModel extends ViewModel {

    MutableLiveData<Boolean> chipChecked = new MutableLiveData<>();
    MutableLiveData<HobbyRecyclerView> hobbiesAdapter = new MutableLiveData<>(null);
    public MutableLiveData<Boolean> result = new MutableLiveData<>();


    public  void createAdapter(){
        ChangeHobbyFragmentDialogModel.createAdapter( hobbiesAdapter, chipChecked);
    }


    public void changeHobbies(){
       ChangeHobbyFragmentDialogModel.changeHobbies(hobbiesAdapter.getValue(), result);
    }


    public HobbyRecyclerView getAdapter() {
        return hobbiesAdapter.getValue();
    }
}
