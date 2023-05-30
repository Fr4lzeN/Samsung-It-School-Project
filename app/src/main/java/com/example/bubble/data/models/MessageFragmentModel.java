package com.example.bubble.data.models;

import android.annotation.SuppressLint;
import android.net.Uri;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;

import com.example.bubble.UI.adapter.FirebaseMessageAdapter;
import com.example.bubble.data.JSONModels.MessageJSON;
import com.example.bubble.data.firebase.FirebaseActions;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import io.reactivex.subjects.PublishSubject;

public class MessageFragmentModel {
    public static FirebaseMessageAdapter createAdapter(Fragment fragment, String messageId, MutableLiveData<Integer> itemCount, MutableLiveData<Integer> allItemCount) {
        Query query = FirebaseDatabase.getInstance().getReference("chat").child(messageId);
        FirebaseRecyclerOptions<MessageJSON> options = new FirebaseRecyclerOptions
                .Builder<MessageJSON>()
                .setQuery(query, MessageJSON.class)
                .setLifecycleOwner(fragment.getViewLifecycleOwner()).build();
        FirebaseMessageAdapter adapter = new FirebaseMessageAdapter(options, itemCount, allItemCount);
        return adapter;
    }

    public static void sendMessage(String messageId, String message) {
        FirebaseActions.sendMessage(message, messageId);
    }

    @SuppressLint("CheckResult")
    public static void getMessageId(String uid, MutableLiveData<String> messageId) {
        PublishSubject<String> messageSubject = PublishSubject.create();
        messageSubject.subscribe(
                messageId::setValue
        );
        FirebaseActions.getMessageId(uid, messageSubject);
    }


    @SuppressLint("CheckResult")
    public static void createMessage(String uid, MutableLiveData<String> messageId, String message) {
        PublishSubject<String> messageSubject = PublishSubject.create();
        messageSubject.subscribe(
                messageId::setValue,
                throwable -> {

                },
                ()->{
                    sendMessage(messageId.getValue(), message);
                }
        );
        FirebaseActions.createMessageId(uid, messageSubject);
    }

    public static void downloadPicture(String uid, MutableLiveData<Uri> picture) {
        FirebaseActions.downloadPicture(uid, picture);
    }
}
