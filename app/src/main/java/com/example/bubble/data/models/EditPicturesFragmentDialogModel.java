package com.example.bubble.data.models;

import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.MutableLiveData;

import com.example.bubble.R;
import com.example.bubble.data.firebase.FirebaseActions;
import com.example.bubble.UI.adapter.TakePictureAdapter;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class EditPicturesFragmentDialogModel {

    static final int deletePicture = 3;
    static final int changePicture = 2;
    static final int addPicture = 1;

    static List<Uri>  newData = new ArrayList<>(Collections.nCopies(5, Uri.EMPTY));


    static TakePictureAdapter adapter;


    public static  void createAdapter(FragmentActivity activity, List<Uri> data){
        adapter = new TakePictureAdapter(data, (data1, position) -> {
            if (position==0){
                pickImage(activity,position,addPicture);
            }
            else{
                dialog(activity, position);
            }
        });
    }



    public static void deletePicture(int position){
        adapter.deleteData(position);
    }

    static public void dialog(FragmentActivity activity, int position){
        final  String[] actions = {"Изменить фото", "Удалить фото"};
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setItems(actions, (dialog, which) -> {
            switch (which){
                case 0:
                    pickImage(activity, position, changePicture);
                    break;
                case 1:
                    deletePicture(position);
                    break;
            }
        }).show();
    }

    public static void pickImage(FragmentActivity activity, int position, int action){
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        activity.startActivityForResult(intent, action*10+position);

    }

    public static TakePictureAdapter getAdapter() {
        return adapter;
    }

    public static void uploadData(List<Uri> data, MutableLiveData<Boolean> taskResult) {
       List<UploadTask> tasks = FirebaseActions.uploadPictures(FirebaseStorage.getInstance().getReference(), FirebaseAuth.getInstance().getUid(), data.subList(0, data.size()));
        Task<Void> userTask = FirebaseActions.updateUserPicture(data.get(0));
       if (userTask!=null) {
           Tasks.whenAll(Tasks.whenAll(tasks), userTask).addOnCompleteListener(task -> {
               taskResult.setValue(task.isSuccessful());
           });
       }
       else{
           Tasks.whenAll(Tasks.whenAll(tasks)).addOnCompleteListener(task -> {
               taskResult.setValue(task.isSuccessful());
           });
       }
    }


    public static void getPictures(MutableLiveData<List<Uri>> data) {
        FirebaseActions.downloadPicture(FirebaseAuth.getInstance().getCurrentUser().getUid()).addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                List<Task<Uri>> allTasks = new ArrayList<>();
                List<Uri> tempData = new ArrayList<>(Collections.nCopies(task.getResult().getItems().size(), Uri.EMPTY));
                for (StorageReference i : task.getResult().getItems()){
                    Log.d("Task",String.valueOf(task.getResult().getItems().indexOf(i)));
                    allTasks.add(i.getDownloadUrl().addOnCompleteListener(task1 -> tempData.set(task.getResult().getItems().indexOf(i), task1.getResult())));
                }
                Tasks.whenAll(allTasks).addOnCompleteListener(task12 -> {
                    Log.d("Task", "all");
                    tempData.removeAll(Collections.nCopies(1,Uri.EMPTY));
                    data.setValue(tempData);
                });
            }
        });
    }
}
