package com.example.bubble;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;

import com.example.bubble.registration.FillDataActivity;
import com.example.bubble.registration.FillDataModel;
import com.example.bubble.registration.TakePictureRecyclerView;

import java.util.List;

public class PictureActions {

    TakePictureRecyclerView adapter;
    Context activity;
    public PictureActions(Activity activity) {
        this.activity =  activity;
    }

    public void setAdapter(TakePictureRecyclerView adapter) {
        this.adapter = adapter;
    }

    public void createIntent(int position, int action){
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        ((Activity)activity).startActivityForResult(intent, action*10+position);
    }

    public List<Uri> getData(){
        return adapter.getData();
    }

    public void add(Intent data){
        this.adapter.addData(data.getData());
    }
    public void change(Intent data, int i) {
        adapter.changeData(data.getData(), i);
    }
    public List<Uri> delete(int position){
        adapter.deleteData(position);
        return adapter.getData();
    }
    public void Dialog(int position){
        final  String[] actions = {"Изменить фото", "Удалить фото"};
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setItems(actions, (dialog, which) -> {
            switch (which){
                case 0:
                    createIntent(position, FillDataModel.changePicture);
                    break;
                case 1:
                    FillDataActivity.getViewModel().deleteAdapterPicture(position);
                    break;
            }
        }).show();
    }

}
