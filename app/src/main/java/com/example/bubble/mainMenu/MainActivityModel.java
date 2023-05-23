package com.example.bubble.mainMenu;

import android.annotation.SuppressLint;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.bubble.JSON.FriendInfo;
import com.example.bubble.JSON.MessageListItem;
import com.example.bubble.MessageListComparator;
import com.google.firebase.database.GenericTypeIndicator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.subjects.PublishSubject;

public class MainActivityModel {
    @SuppressLint("CheckResult")
    public static void getMessageData(String uid, MutableLiveData<ArrayList<MessageListItem>> allMessagesData) {
        PublishSubject<Map.Entry<String,String>> chatIds = PublishSubject.create();
        PublishSubject<Map.Entry<String,String>> groupChatIds = PublishSubject.create();
        PublishSubject<MessageListItem> message = PublishSubject.create();
        FirebaseActions.getChatIds(uid, chatIds);
        Map<String, MessageListItem> messages = new HashMap<>();
        chatIds.subscribe(
                map->{
                    FirebaseActions.getMessageFromUser(map.getKey(), map.getValue(), message);
                });

        message.subscribe(
                messageItem ->{
                    messages.put(messageItem.uid, messageItem);
                    ArrayList<MessageListItem> allMessages = new ArrayList<>(messages.values());
                    Collections.sort(allMessages, new MessageListComparator());
                    allMessagesData.setValue(allMessages);
                });

    }

    @SuppressLint("CheckResult")
    public static void getFriendList(String uid, MutableLiveData<List<FriendInfo>> friends, MutableLiveData<List<FriendInfo>> outgoingRequests, MutableLiveData<List<FriendInfo>> incomingRequests) {
        PublishSubject<Map.Entry<FriendInfo, FriendStatusEnum>> friendStatus = PublishSubject.create();
        PublishSubject<Map.Entry<String, FriendStatusEnum>> changeFriendStatus = PublishSubject.create();
        PublishSubject<Map.Entry<String, FriendStatusEnum>> deleteFriendStatus = PublishSubject.create();
        FirebaseActions.getFriends(uid,friendStatus,changeFriendStatus, deleteFriendStatus);
        friends.setValue(null);
        outgoingRequests.setValue(null);
        incomingRequests.setValue(null);
        friendStatus.subscribe(
                map->{
                 switch (map.getValue()){
                     case FRIENDS:
                         List<FriendInfo> tempFriendList;
                         if (friends.getValue()!=null)
                            tempFriendList = new ArrayList<>(friends.getValue());
                         else
                             tempFriendList = new ArrayList<>();
                        tempFriendList.add(map.getKey());
                        friends.setValue(tempFriendList);
                        break;
                     case INCOMING_REQUEST:
                         List<FriendInfo> tempIncomingList;
                         if (incomingRequests.getValue()!=null)
                             tempIncomingList = new ArrayList<>(incomingRequests.getValue());
                         else
                             tempIncomingList = new ArrayList<>();
                         tempIncomingList.add(map.getKey());
                         incomingRequests.setValue(tempIncomingList);
                         break;
                     case OUTGOING_REQUEST:
                         List<FriendInfo> tempOutgoingList;
                         if (outgoingRequests.getValue()!=null)
                            tempOutgoingList= new ArrayList<>(outgoingRequests.getValue());
                         else
                             tempOutgoingList = new ArrayList<>();
                         tempOutgoingList.add(map.getKey());
                         outgoingRequests.setValue(tempOutgoingList);
                         break;
                 }
                },
                throwable -> {
                    Log.d("AAAAF", throwable.toString());
                }
        );
        changeFriendStatus.subscribe(
                map ->{
                    if (!changeFriendStatus(friends,outgoingRequests, map.getKey()))
                        changeFriendStatus(friends,incomingRequests, map.getKey());
                },
                throwable -> Log.d("AAAAC", throwable.toString())
        );

        deleteFriendStatus.subscribe(
                map->{
                    switch (map.getValue()){
                        case FRIENDS:
                            deleteFriendStatus(friends, map.getKey());
                            break;
                        case INCOMING_REQUEST:
                            deleteFriendStatus(incomingRequests, map.getKey());
                            break;
                        case OUTGOING_REQUEST:
                            deleteFriendStatus(outgoingRequests, map.getKey());
                            break;
                    }
                },
                throwable -> Log.d("AAAAD", throwable.toString())
        );
    }


    static boolean changeFriendStatus(MutableLiveData<List<FriendInfo>> firstList, MutableLiveData<List<FriendInfo>> secondList, String uid){
        if (secondList.getValue()!=null) {
            for (FriendInfo i : secondList.getValue()) {
                if (i.getUid().equals(uid)) {
                    List<FriendInfo> temp = new ArrayList<>(secondList.getValue());
                    temp.remove(i);
                    secondList.setValue(temp);
                    temp = new ArrayList<>(firstList.getValue());
                    temp.add(i);
                    firstList.setValue(temp);
                    return true;
                }
            }
        }
        return false;
    }

    static void deleteFriendStatus(MutableLiveData<List<FriendInfo>> list, String uid){
        if (list.getValue()!=null) {
            List<FriendInfo> tempList = new ArrayList<>(list.getValue());
            for (FriendInfo i : tempList) {
                if (i.getUid().equals(uid)) {
                    tempList.remove(i);
                    list.setValue(tempList);
                    return;
                }
            }
        }
    }

    public static void getHobbies(MutableLiveData<ArrayList<String>> hobbyList) {
        FirebaseActions.getHobbyList().addOnCompleteListener(task -> {
            hobbyList.setValue(task.getResult().getValue(new GenericTypeIndicator<ArrayList<String>>() {}));
        });
    }
}
