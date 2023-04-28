package com.example.bubble.mainMenu;

import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bubble.FirebaseMessageAdapter;
import com.example.bubble.JSON.UserInfoJSON;

public class MessageFragmentViewModel extends ViewModel {

    MutableLiveData<FirebaseMessageAdapter> adapter = new MutableLiveData<>();
    MutableLiveData<String> messageId = new MutableLiveData<>();
    MutableLiveData<UserInfoJSON> userData = new MutableLiveData<>();
    MutableLiveData<Integer> itemCount = new MutableLiveData<>();
    MutableLiveData<Integer> allItemCount = new MutableLiveData<>();
    MutableLiveData<Uri> picture = new MutableLiveData<>();


    public void createAdapter(Fragment fragment){
        adapter.setValue(MessageFragmentModel.createAdapter(fragment, messageId.getValue(), itemCount, allItemCount));
    }
    public void sendMessage(String string) {
        MessageFragmentModel.sendMessage(messageId.getValue(), string);
    }
    public void setMessageId(String messageId) {
        this.messageId.setValue(messageId);
    }

    public void setUserData(UserInfoJSON userData){
        this.userData.setValue(userData);
    }
    public void getMessageId(String uid) {
        MessageFragmentModel.getMessageId(uid, messageId);
    }
    public void createNewMessage(String uid, String message) {
        MessageFragmentModel.createMessage(uid, messageId, message);
    }


    public void downloadPicture(String uid) {
        MessageFragmentModel.downloadPicture( uid ,picture);
    }
}
