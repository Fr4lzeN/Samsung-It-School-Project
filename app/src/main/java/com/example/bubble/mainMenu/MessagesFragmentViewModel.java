package com.example.bubble.mainMenu;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.bubble.JSON.UserInfoJSON;

public class MessagesFragmentViewModel extends ViewModel {

    MutableLiveData<MessageListAdapter> adapter = new MutableLiveData<>();


    public void createAdapter(Fragment fragment) {
        MessagesFragmentModel.createAdapter(fragment, adapter);
    }


}
