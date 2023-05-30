package com.example.bubble.UI.viewModels;

import android.net.Uri;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.bubble.UI.adapter.FirebaseMessageAdapter;
import com.example.bubble.data.JSONModels.UserInfoJSON;
import com.example.bubble.data.models.MessageFragmentModel;

public class MessageFragmentViewModel extends ViewModel {

    public MutableLiveData<FirebaseMessageAdapter> adapter = new MutableLiveData<>();
    public MutableLiveData<String> messageId = new MutableLiveData<>();
    public MutableLiveData<UserInfoJSON> userData = new MutableLiveData<>();
    public MutableLiveData<Integer> itemCount = new MutableLiveData<>();
    public MutableLiveData<Integer> allItemCount = new MutableLiveData<>();
    public MutableLiveData<Uri> picture = new MutableLiveData<>();


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
