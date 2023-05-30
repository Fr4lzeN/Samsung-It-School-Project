package com.example.bubble.data.models;

import android.app.AlertDialog;
import android.content.Context;

import androidx.lifecycle.MutableLiveData;

import com.example.bubble.data.JSONModels.UserInfoJSON;
import com.example.bubble.data.firebase.FirebaseActions;
import com.example.bubble.tools.ENUMs.FriendStatusEnum;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.storage.StorageReference;

import java.util.List;

public class ProfileFragmentModel {


    public static void downloadPictures(MutableLiveData<String> uid, MutableLiveData<List<StorageReference>> data) {
        FirebaseActions.downloadAllPictures(uid.getValue(), data);
    }

    public static void downloadUserData(MutableLiveData<String> uid, MutableLiveData<UserInfoJSON> userInfo) {
        FirebaseActions.downloadUserInfo(uid.getValue(),userInfo);
    }

    public static void getHobbies(MutableLiveData<String> uid, MutableLiveData<List<String>> hobbies) {
        FirebaseActions.getHobbies(uid.getValue()).addOnCompleteListener(task -> {
          if (task.isSuccessful()){
              hobbies.setValue(task.getResult().getValue(new GenericTypeIndicator<List<String>>() {}));
          }
        });
    }

    public static void checkFriendStatus(MutableLiveData<FriendStatusEnum> friendStatus, String uid) {
        FirebaseActions.getFriendStatus(FirebaseAuth.getInstance().getUid(), uid).addOnCompleteListener(task -> {
           if (task.isSuccessful()){
               friendStatus.setValue(task.getResult().getValue(FriendStatusEnum.class));
           }
        });
    }

    public static void changeFriendStatus(MutableLiveData<FriendStatusEnum> friendStatus, String userUid, Context context) {
        String myUid = FirebaseAuth.getInstance().getUid();
        if (friendStatus.getValue()!=null && friendStatus.getValue()!= FriendStatusEnum.NO_STATUS) {
            switch (friendStatus.getValue()) {
                case FRIENDS:
                    new AlertDialog.Builder(context)
                            .setMessage("Вы точно хотите удалить этого пользователя из списка друзей?").
                            setPositiveButton("Да", (dialog1, which) -> {
                                FirebaseActions.deleteFriend(myUid, userUid).addOnCompleteListener(task -> {
                                    friendStatus.setValue(FriendStatusEnum.NO_STATUS);
                                });
                                dialog1.dismiss();
                            })
                            .setNegativeButton("Нет", (dialog2, which) -> {
                                dialog2.dismiss();
                            }).show();

                    break;
                case OUTGOING_REQUEST:
                    new AlertDialog.Builder(context)
                            .setMessage("Вы точно хотите отменить заявку в друзья?").
                            setPositiveButton("Да", (dialog1, which) -> {
                                FirebaseActions.deleteFriendRequest(myUid, userUid).addOnCompleteListener(task -> {
                                    friendStatus.setValue(FriendStatusEnum.NO_STATUS);
                                });
                                dialog1.dismiss();
                            })
                            .setNegativeButton("Нет", (dialog2, which) -> {
                                dialog2.dismiss();
                            }).show();
                    break;
                case INCOMING_REQUEST:
                    new AlertDialog.Builder(context)
                            .setMessage("Вы хотите принять заявку в друзья?").
                            setPositiveButton("Да", (dialog1, which) -> {
                                FirebaseActions.acceptFriendRequest(myUid, userUid).addOnCompleteListener(task -> {
                                    friendStatus.setValue(FriendStatusEnum.FRIENDS);
                                });
                                dialog1.dismiss();
                            })
                            .setNegativeButton("Нет", (dialog2, which) -> {
                                FirebaseActions.declineFriendRequest(myUid, userUid).addOnCompleteListener(task -> {
                                   friendStatus.setValue(FriendStatusEnum.NO_STATUS);
                                });
                                dialog2.dismiss();
                            }).show();
                    break;
            }
        }else{
            FirebaseActions.sendFriendRequest(myUid, userUid).addOnCompleteListener(task -> {
                friendStatus.setValue(FriendStatusEnum.OUTGOING_REQUEST);
            });
        }
    }
}
