package com.example.bubble.mainMenu;

import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;

import com.example.bubble.JSON.FriendInfo;
import com.example.bubble.JSON.MessageJSON;
import com.example.bubble.JSON.MessageListItem;
import com.example.bubble.JSON.UserInfoJSON;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import io.reactivex.subjects.PublishSubject;

public class FirebaseActions {

    static  ChildEventListener friendListener;
    static  ChildEventListener uidByHobbyListener;


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

    public static Task<Void> uploadUserPictures(StorageReference storage, String uid, List<Uri> data) {
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
        return Tasks.whenAll(results);
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



    public static Task<DataSnapshot> downloadUserInfo(String uid){
        return FirebaseDatabase.getInstance().getReference("userData").child(uid).child("userInfo").get();
    }


    private static void setUserValue(MutableLiveData<UserInfoJSON> userInfo, DataSnapshot result) {
        userInfo.setValue(result.getValue(UserInfoJSON.class));
    }


    public static void downloadPicture(String uid, MutableLiveData<Uri> picture) {
       FirebaseStorage.getInstance().getReference(uid).child("1").getDownloadUrl().addOnCompleteListener(task -> {
           picture.setValue(task.getResult());
       });
    }

    public static Task<Uri> downloadFirstPicture(String uid){
        return FirebaseStorage.getInstance().getReference(uid).child("1").getDownloadUrl();
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
        tasks.add(FirebaseDatabase.getInstance().getReference("userData").child(myUid).child("friendList").child(userUid).setValue(FriendStatusEnum.FRIENDS.toString()));
        tasks.add(FirebaseDatabase.getInstance().getReference("userData").child(userUid).child("friendList").child(myUid).setValue(FriendStatusEnum.FRIENDS.toString()));
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
        tasks.add(FirebaseDatabase.getInstance().getReference("userData").child(myUid).child("friendList").child(userUid).setValue(FriendStatusEnum.OUTGOING_REQUEST));
        tasks.add(FirebaseDatabase.getInstance().getReference("userData").child(userUid).child("friendList").child(myUid).setValue(FriendStatusEnum.INCOMING_REQUEST));
        return Tasks.whenAll(tasks);
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

    public static void getChatIds(String uid, PublishSubject<Map.Entry<String, String>> messageSubject){
        FirebaseDatabase.getInstance().getReference("userData")
                .child(uid)
                .child("chat")
                .addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                        Map.Entry<String,String> snapshotValue = new AbstractMap.SimpleEntry<>(snapshot.getKey(),snapshot.getValue(String.class));
                        messageSubject.onNext(snapshotValue);
                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                    }

                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot snapshot) {

                    }

                    @Override
                    public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
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

    public static void getMessageFromUser(String uid, String chatId, PublishSubject<MessageListItem> messages) {
        Task<DataSnapshot> userTask = FirebaseDatabase.getInstance().getReference("userData")
                .child(uid)
                .child("userInfo")
                .get();
        Task<Uri> pictureTask = FirebaseStorage.getInstance().getReference(uid).child("1").getDownloadUrl();
        MessageListItem message = new MessageListItem();
        message.messageId=chatId;
        message.uid=uid;
        Log.d("Message", chatId);
        FirebaseDatabase.getInstance().getReference("chat").child(chatId).limitToLast(1).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                message.setMessage(snapshot.getValue(MessageJSON.class));
                Log.d("Message", message.message.toString());
                Log.d("Message", snapshot.toString());
                Log.d("Message", snapshot.getKey());
                Log.d("Message", "_________");
                userTask.addOnCompleteListener(task -> message.setUserInfo(task.getResult().getValue(UserInfoJSON.class)));
                pictureTask.addOnCompleteListener(task -> message.setPicture(task.getResult()));
                Tasks.whenAll(userTask,pictureTask).addOnCompleteListener(task -> {
                    messages.onNext(message);
                });
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public static void getFriends(String uid, PublishSubject<Map.Entry<FriendInfo, FriendStatusEnum>> friendStatus, PublishSubject<Map.Entry<String, FriendStatusEnum>> changeFriend, PublishSubject<Map.Entry<String, FriendStatusEnum>> deleteFriend) {
        friendListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                FriendInfo friend = new FriendInfo(snapshot.getKey());
                Task<DataSnapshot> userTask = downloadUserInfo(snapshot.getKey()).addOnCompleteListener(task -> {
                    friend.setUserData(task.getResult().getValue(UserInfoJSON.class));
                });
                Task<Uri> pictureTask = downloadFirstPicture(snapshot.getKey()).addOnCompleteListener(task -> {
                    friend.setPicture(task.getResult());
                });

                Tasks.whenAll(userTask,pictureTask).addOnCompleteListener(task -> {
                    friendStatus.onNext(new AbstractMap.SimpleEntry<>(friend,snapshot.getValue(FriendStatusEnum.class)));
                });
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                changeFriend.onNext(new AbstractMap.SimpleEntry<>(snapshot.getKey(),snapshot.getValue(FriendStatusEnum.class)));
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                deleteFriend.onNext(new AbstractMap.SimpleEntry<>(snapshot.getKey(), snapshot.getValue(FriendStatusEnum.class)));
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        FirebaseDatabase.getInstance().getReference("userData").child(uid).child("friendList").addChildEventListener(friendListener);
    }

    public static void removeFriendListener(String uid) {
        FirebaseDatabase.getInstance().getReference("userData").child(uid).child("friendList").removeEventListener(friendListener);
    }

    public static Task<DataSnapshot> getHobbyList() {
        return FirebaseDatabase.getInstance().getReference().child("hobbyList").get();
    }

    public static void getUserUidByHobby(String hobby, PublishSubject<String> uid) {
        uidByHobbyListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                uid.onNext(snapshot.getKey());
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        FirebaseDatabase.getInstance().getReference("hobbies").child(hobby).addChildEventListener(uidByHobbyListener);
    }

    public static void getUser(String userUid, PublishSubject<FriendInfo> userData) {
        FriendInfo user = new FriendInfo(userUid);
        Task<DataSnapshot> userTask = FirebaseDatabase.getInstance().getReference("userData").child(userUid).child("userInfo").get().addOnCompleteListener(task -> {
            user.setUserData(task.getResult().getValue(UserInfoJSON.class));
        });
        Task<Uri> pictureTask = FirebaseStorage.getInstance().getReference(userUid).child("1").getDownloadUrl().addOnCompleteListener(task -> {
           user.setPicture(task.getResult());
        });
        Tasks.whenAll(userTask,pictureTask).addOnCompleteListener(task -> {
           userData.onNext(user);
        });
    }

    public static void userUidByHobbyDelete(String hobby) {
        FirebaseDatabase.getInstance().getReference("hobbies").child(hobby).removeEventListener(uidByHobbyListener);
    }
}
