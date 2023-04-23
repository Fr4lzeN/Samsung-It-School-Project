package com.example.bubble.registration;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import com.example.bubble.PictureActions;
import com.example.bubble.R;

import java.util.ArrayList;
import java.util.List;

public class FillDataActivity extends AppCompatActivity {

    static FillDataViewModel viewModel;

    public static FillDataViewModel getViewModel() {
        return viewModel;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fill_data);
        viewModel= new ViewModelProvider(this).get(FillDataViewModel.class);
        viewModel.initFirebase();
        viewModel.createAdapter(this);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("ActivityRes", "FillData");
        if(resultCode==RESULT_OK && data!=null) {
            switch (requestCode/10){
                case FillDataModel.addPicture:
                    viewModel.addPictureToAdapter(data);
                    break;
                case FillDataModel.changePicture:
                    viewModel.changeAdapterPicture(data, requestCode);
                    break;
            }
        }

    }
}