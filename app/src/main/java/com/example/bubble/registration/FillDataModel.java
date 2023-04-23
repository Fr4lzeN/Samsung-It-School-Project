package com.example.bubble.registration;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;

import com.example.bubble.JSON.UserInfoJSON;
import com.example.bubble.PictureActions;
import com.example.bubble.R;
import com.example.bubble.mainMenu.FirebaseActions;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.List;

public class FillDataModel {

    public final static int changePicture = 1;
    final static int addPicture = 0;
    static PictureActions pictureActions;

    public static Uri createAdapterData(Activity activity) {
        pictureActions = new PictureActions(activity);
        return new Uri.Builder()
                .scheme(ContentResolver.SCHEME_ANDROID_RESOURCE)
                .authority(activity.getResources().getResourcePackageName(R.drawable.add_picture))
                .appendPath(activity.getResources().getResourceTypeName(R.drawable.add_picture))
                .appendPath(activity.getResources().getResourceEntryName(R.drawable.add_picture))
                .build();
    }

    public static TakePictureRecyclerView createAdapter(List<Uri> data){
        TakePictureRecyclerView adapter = new TakePictureRecyclerView(data, listener );
        pictureActions.setAdapter(adapter);
        return adapter;
    }

    static TakePictureRecyclerView.OnItemClickListener listener = (data, position) -> {
        if (position==addPicture){
            pictureActions.createIntent(position, addPicture);
        }
        else{
            pictureActions.Dialog(position);
        }
    };

    public static List<Uri> addPictureToAdapter(Intent data) {
        pictureActions.add(data);
        return pictureActions.getData();
    }

    public static List<Uri> changeAdapterPicture(Intent data, int position) {
        pictureActions.change(data,position%10);
        return pictureActions.getData();
    }

    public static Task<Void> createUser(FirebaseUser user, String name, List<Uri> data) {
        return FirebaseActions.updateUserProfile(user, name, data.get(1));
    }

    public static Task<Void> createUserDB(DatabaseReference database, String uid, String name, String info, String gender, Integer[] dateOfBirth) {
        return FirebaseActions.createUserDatabase(database, uid, new UserInfoJSON(name, info, gender, dateOfBirth[0], dateOfBirth[1], dateOfBirth[2]));
    }

    public static List<UploadTask> sendPictures(StorageReference storage, String uid, List<Uri> data) {
        return FirebaseActions.uploadPictures(storage, uid, data.subList(1, data.size()));
    }

    public static FirebaseResultEnum checkSuccess(List<Boolean> results, List<Uri> data) {
        if (results.size()!=data.size()+1) return  FirebaseResultEnum.WAITING;
        for (boolean i : results){
            if (!i) return FirebaseResultEnum.FAILTURE;
        }
        return FirebaseResultEnum.SUCCESS;
    }

    public static List<Uri> deleteAdapterPicture(int position) {
        return pictureActions.delete(position);
    }

    public static void setHobbies(List<String> hobbies, List<Boolean> checked) {
        FirebaseActions.setHobbies(hobbies, checked);
    }
}
