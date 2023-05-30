package com.example.bubble.mainMenu;

import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.bubble.JSON.FriendInfo;
import com.example.bubble.JSON.MessageJSON;
import com.example.bubble.JSON.UserInfoJSON;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.Query;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GroupMessageFragmentViewModel extends ViewModel {

    String chatName;
    String chatId;
    Uri picture;
    ArrayList<FriendInfo> users;
    MutableLiveData<FirebaseGroupMessageAdapter> adapter = new MutableLiveData<>();
    MutableLiveData<Integer> itemCount = new MutableLiveData<>();
    MutableLiveData<Integer> allItemCount = new MutableLiveData<>();


    public void setOptions(String chatName, String chatId, Uri picture){
        this.chatName =chatName;
        this.chatId = chatId;
        this.picture = picture;
    }

    public void createAdapter(Fragment fragment){
        FirebaseDatabase.getInstance().getReference("groupChat").child(chatId).child("usersUid").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                HashMap<String, FriendInfo> users = new HashMap<>();
                ArrayList<Task<Void>> tasks = new ArrayList<>();
                for (String i : task.getResult().getValue(new GenericTypeIndicator<ArrayList<String>>() {})){
                    FriendInfo user = new FriendInfo(i);
                    tasks.add(Tasks.whenAll(FirebaseActions.downloadUserInfo(i).addOnCompleteListener(task1 -> user.setUserData(task1.getResult().getValue(UserInfoJSON.class))),
                    FirebaseStorage.getInstance().getReference(i).child("1").getDownloadUrl().addOnCompleteListener(task12 -> user.setPicture(task12.getResult()))).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            users.put(i,user);
                        }
                    }));
                }
                Tasks.whenAll(tasks).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        GroupMessageFragmentViewModel.this.users = new ArrayList<>(users.values());
                        Log.d("GroupChat", users.values().toString());
                        Log.d("GroupChat", chatId);
                        Query query = FirebaseDatabase.getInstance().getReference("groupChat").child(chatId).child("messages");
                        FirebaseRecyclerOptions<MessageJSON> options = new FirebaseRecyclerOptions
                                .Builder<MessageJSON>()
                                .setQuery(query, MessageJSON.class)
                                .setLifecycleOwner(fragment.getViewLifecycleOwner()).build();
                        adapter.setValue(new FirebaseGroupMessageAdapter(options, users, itemCount, allItemCount));
                    }
                });
            }
        });
    }


    public ArrayList<FriendInfo> getUsers() {
        return  users;
    }
}
