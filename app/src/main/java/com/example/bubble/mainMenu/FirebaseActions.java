package com.example.bubble.mainMenu;

import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.example.bubble.JSON.MessageJSON;
import com.example.bubble.JSON.MessageListItem;
import com.example.bubble.JSON.UserInfoJSON;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import io.reactivex.subjects.PublishSubject;

public class FirebaseActions {

    public static Task<Void> createUserDatabase(DatabaseReference database, String uid, UserInfoJSON userInfoJSON) {
        return database.child("userData").child(uid).child("userInfo").setValue(userInfoJSON);
    }

    public  static  void createUserDB(MutableLiveData<Boolean> result, List<Boolean> sendResults, MutableLiveData<UserInfoJSON> userInfo, FirebaseUser user, String name, String info, String[] dateOfBirth){
        FirebaseDatabase.getInstance()
                .getReference("userData")
                .child(user.getUid()).child("userInfo")
                .setValue(new UserInfoJSON(
                        name, info,
                        userInfo.getValue().gender, Integer.valueOf(dateOfBirth[2]),
                        Integer.valueOf(dateOfBirth[1]), Integer.valueOf(dateOfBirth[0])))
                .addOnCompleteListener(task -> {
                    sendResults.add(task.isSuccessful());
                    if (sendResults.size()==2){
                        result.setValue(sendResults.get(0)&&sendResults.get(1));
                    }
                });
    }


    public static Task<Void> updateUserProfile(FirebaseUser user, String name, Uri uri) {
        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(name)
                .setPhotoUri(uri).build();
        return user.updateProfile(profileUpdates);
    }

    public static  void updateUserProfile(MutableLiveData<Boolean> result, List<Boolean> sendResults, FirebaseUser user, String name){
        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(name)
                .build();
        user.updateProfile(profileUpdates).addOnCompleteListener(task -> {
            sendResults.add(task.isSuccessful());
            if (sendResults.size()==2){
                result.setValue(sendResults.get(0)&&sendResults.get(1));
            }
        });
    }

    public static List<UploadTask> uploadPictures(StorageReference storage, String uid, List<Uri> data) {
        List<UploadTask> results = new ArrayList<>();
        for (int i=0; i<6; i++){
            Log.d("Storage", "Picture sent "+i);
            try {
                results.add(storage.child(uid + "/" + (i + 1)).putFile(data.get(i)));
            }
            catch (Throwable t){
                if (t instanceof java.lang.IndexOutOfBoundsException){
                    storage.child(uid + "/" + (i + 1)).delete();
                }
            }

        }
        return results;
    }

    public static void downloadUserInfo(String uid, MutableLiveData<UserInfoJSON> userInfo) {
        FirebaseDatabase.getInstance().getReference("userData").child(uid).child("userInfo").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()){
               setUserValue(userInfo, task.getResult());
            }
            else{
                Log.d("UserInfo", "error");
            }
        });
    }

    public static void downloadUserInfo(List<String> uids, MutableLiveData<Map<String,UserInfoJSON>> map){
        DatabaseReference userReference = FirebaseDatabase.getInstance().getReference("userData");
        List<Task<DataSnapshot>> tasks = new ArrayList<>();
        HashMap<String, UserInfoJSON> tempMap = new HashMap<>();
        for (String i : uids){
            tasks.add(userReference.child(i)
                    .child("userInfo")
                    .get()
                    .addOnCompleteListener(task -> tempMap.put(i, task.getResult().getValue(UserInfoJSON.class))));
        }
        Tasks.whenAll(tasks).addOnCompleteListener(task -> {
            map.setValue(tempMap);
        });
    }

    private static void setUserValue(MutableLiveData<UserInfoJSON> userInfo, DataSnapshot result) {
        userInfo.setValue(result.getValue(UserInfoJSON.class));
    }


    public static void downloadPicture(String uid, MutableLiveData<Uri> picture) {
       FirebaseStorage.getInstance().getReference(uid).child("1").getDownloadUrl().addOnCompleteListener(task -> {
           picture.setValue(task.getResult());
       });
    }

    public static Task<ListResult> downloadPicture(String uid){
        return FirebaseStorage.getInstance().getReference(uid).listAll();
    }

    public static void downloadAllPictures(String uid, MutableLiveData<List<StorageReference>> data) {
        FirebaseStorage.getInstance().getReference(uid).listAll().addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                data.setValue(task.getResult().getItems());
            }
        });
    }

    public static Task<Void> updateUserPicture(Uri uri){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setPhotoUri(uri).build();
        return user.updateProfile(profileUpdates);
    }

    public static Task<Void> setHobbies(List<String> hobbies, List<Boolean> checked){
        String uid = FirebaseAuth.getInstance().getUid();
        DatabaseReference userData = FirebaseDatabase.getInstance()
                .getReference("userData")
                .child(uid)
                .child("hobbies");
        DatabaseReference hobbiesData = FirebaseDatabase.getInstance().getReference("hobbies");
        List<String> setHobbies = new ArrayList<>();
        List<Task<Void>> tasks = new ArrayList<>();
        for (int i=0; i<hobbies.size(); i++){
            if (checked.get(i)){
                setHobbies.add(hobbies.get(i));
                tasks.add(hobbiesData.child(hobbies.get(i)).updateChildren(Collections.singletonMap(uid, uid)));
            }else{
                tasks.add(hobbiesData.child(hobbies.get(i)).child(uid).removeValue());
            }
        }
        tasks.add(userData.setValue(setHobbies));
        return Tasks.whenAll(tasks);
    }

    public static Task<DataSnapshot> getHobbies(String uid) {
        DatabaseReference userHobbies = FirebaseDatabase.getInstance()
                .getReference("userData")
                .child(uid)
                .child("hobbies");
        return userHobbies.get();
    }

    public static void getFriends(MutableLiveData<Map<String,FriendStatus>> map){

        String uid = FirebaseAuth.getInstance().getUid();

        FirebaseDatabase.getInstance().getReference("userData").child(uid).child("friendList").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                try {
                    map.setValue(snapshot.getValue(new GenericTypeIndicator<Map<String, FriendStatus>>() {}));
                } catch (Throwable t){
                    Log.wtf("Friends", t.toString());
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


    public static void getUserUidByHobby(String hobby, MutableLiveData<List<String>> uids){
        DatabaseReference hobbyReference = FirebaseDatabase.getInstance().getReference("hobbies").child(hobby);
        List<String> tempList = new ArrayList<>();
        hobbyReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                HashMap<String,String> tempMap = snapshot.getValue(new GenericTypeIndicator<HashMap<String, String>>() {});
                if (tempMap!=null) {
                    for (Map.Entry<String, String> i : tempMap.entrySet()) {
                        tempList.add(i.getKey());
                    }
                }
                uids.setValue(tempList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }



    public static Task<DataSnapshot> getFriendStatus(String myUid, String userUid) {
        return FirebaseDatabase.getInstance().getReference("userData").child(myUid).child("friendList").child(userUid).get();
    }

    public static Task<Void> deleteFriend(String myUid, String userUid) {
        List<Task<Void>> tasks = new ArrayList<>();
        tasks.add(FirebaseDatabase.getInstance().getReference("userData").child(myUid).child("friendList").child(userUid).removeValue());
        tasks.add(FirebaseDatabase.getInstance().getReference("userData").child(userUid).child("friendList").child(myUid).removeValue());
        return Tasks.whenAll(tasks);
    }

    public static Task<Void> deleteFriendRequest(String myUid, String userUid) {
        List<Task<Void>> tasks = new ArrayList<>();
        tasks.add(FirebaseDatabase.getInstance().getReference("userData").child(myUid).child("friendList").child(userUid).removeValue());
        tasks.add(FirebaseDatabase.getInstance().getReference("userData").child(userUid).child("friendList").child(myUid).removeValue());
        return Tasks.whenAll(tasks);
    }

    public static Task<Void> acceptFriendRequest(String myUid, String userUid) {
        List<Task<Void>> tasks = new ArrayList<>();
        tasks.add(FirebaseDatabase.getInstance().getReference("userData").child(myUid).child("friendList").child(userUid).setValue(FriendStatus.FRIENDS.toString()));
        tasks.add(FirebaseDatabase.getInstance().getReference("userData").child(userUid).child("friendList").child(myUid).setValue(FriendStatus.FRIENDS.toString()));
        return Tasks.whenAll(tasks);
    }

    public static Task<Void> declineFriendRequest(String myUid, String userUid){
        List<Task<Void>> tasks = new ArrayList<>();
        tasks.add(FirebaseDatabase.getInstance().getReference("userData").child(myUid).child("friendList").child(userUid).removeValue());
        tasks.add(FirebaseDatabase.getInstance().getReference("userData").child(userUid).child("friendList").child(myUid).removeValue());
        return Tasks.whenAll(tasks);
    }

    public static Task<Void> sendFriendRequest(String myUid, String userUid) {
        List<Task<Void>> tasks = new ArrayList<>();
        tasks.add(FirebaseDatabase.getInstance().getReference("userData").child(myUid).child("friendList").child(userUid).setValue(FriendStatus.OUTGOING_REQUEST));
        tasks.add(FirebaseDatabase.getInstance().getReference("userData").child(userUid).child("friendList").child(myUid).setValue(FriendStatus.INCOMING_REQUEST));
        return Tasks.whenAll(tasks);
    }

    public static void getUserMessages(String uid, PublishSubject<Map<String, String>> messageMap){
        Map<String,String> map = new HashMap<>();
           FirebaseDatabase.getInstance().getReference("userData").child(uid).child("chat").get().addOnCompleteListener(task -> {
               if(task.getResult().getValue()!=null) {
                   messageMap.onNext(task.getResult().getValue(new GenericTypeIndicator<Map<String, String>>() {
                   }));
               }else{
                   messageMap.onComplete();
               }
            });
    }


    public static void setMap(Map<String,String> map1, Map<String,String> map2){
        map1 = map2;
    }

    public static void getMessagesList(Map<String, String> map, PublishSubject<MessageListItem> messageData){
        DatabaseReference chatReference = FirebaseDatabase.getInstance().getReference("chat");
        DatabaseReference userReference = FirebaseDatabase.getInstance().getReference("userData");
        StorageReference storageReference = FirebaseStorage.getInstance().getReference();
        List<Task<Void>> allTasks = new ArrayList<>();
        for (Map.Entry<String,String> i : map.entrySet()){
            MessageListItem item = new MessageListItem();
            item.messageId=i.getValue();
            item.uid = i.getKey();
            allTasks.add(Tasks.whenAll(
                    chatReference.child(i.getValue()).limitToLast(1).get().addOnCompleteListener(task ->{
                            Map<String,MessageJSON> tempMap = task.getResult().getValue(new GenericTypeIndicator<Map<String, MessageJSON>>() {});
                            for (Map.Entry<String, MessageJSON> j : tempMap.entrySet()){
                                item.message=j.getValue();
                            }
                    }),
                    userReference.child(i.getKey()).child("userInfo").get().addOnCompleteListener(task ->
                            item.setUserInfo(task.getResult().getValue(UserInfoJSON.class))),
                    storageReference.child(i.getKey()).child("1").getDownloadUrl().addOnCompleteListener(task ->
                            item.setPicture(task.getResult()))).addOnCompleteListener(task -> messageData.onNext(item)));
        }
        Tasks.whenAll(allTasks).addOnCompleteListener(task -> {
            messageData.onComplete();
        });
    }

    public static void sendMessage(String message, String messageId){
        MessageJSON messageJSON = new MessageJSON(message, FirebaseAuth.getInstance().getUid());
        FirebaseDatabase.getInstance().getReference("chat").child(messageId).updateChildren(Collections.singletonMap(String.valueOf(messageJSON.date), messageJSON));
    }


    public static void getMessageId(String uid, PublishSubject<String> messageSubject) {
        FirebaseDatabase.getInstance().getReference("userData")
                .child(FirebaseAuth.getInstance().getUid())
                .child("chat").child(uid).get()
                .addOnCompleteListener(task -> {
                    if (task.getResult().getValue()!=null) {
                        messageSubject.onNext(task.getResult().getValue(String.class));
                    }
                    messageSubject.onComplete();
        });
    }

    public static void createMessageId(String uid, PublishSubject<String> messageSubject) {
        DatabaseReference userReference = FirebaseDatabase.getInstance().getReference("userData");
        String key = userReference.push().getKey();
        String mUid = FirebaseAuth.getInstance().getUid();
        Tasks.whenAll(userReference.child(mUid).child("chat").child(uid).setValue(key),
                userReference.child(uid).child("chat").child(mUid).setValue(key))
                .addOnCompleteListener(task -> {
                    FirebaseActions.getMessageId(uid,messageSubject);
                });
    }
}
