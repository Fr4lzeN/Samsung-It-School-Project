package com.example.bubble.UI.viewModels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.bubble.UI.adapter.HobbyAdapter;
import com.example.bubble.data.models.ChangeHobbyFragmentDialogModel;

public class ChangeHobbyFragmentDialogViewModel extends ViewModel {

    public MutableLiveData<Boolean> chipChecked = new MutableLiveData<>();
    public MutableLiveData<HobbyAdapter> hobbiesAdapter = new MutableLiveData<>(null);
    public MutableLiveData<Boolean> result = new MutableLiveData<>();


    public  void createAdapter(){
        ChangeHobbyFragmentDialogModel.createAdapter( hobbiesAdapter, chipChecked);
    }


    public void changeHobbies(){
       ChangeHobbyFragmentDialogModel.changeHobbies(hobbiesAdapter.getValue(), result);
    }


    public HobbyAdapter getAdapter() {
        return hobbiesAdapter.getValue();
    }
}
