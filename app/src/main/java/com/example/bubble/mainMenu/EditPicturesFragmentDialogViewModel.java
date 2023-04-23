package com.example.bubble.mainMenu;

import android.app.Activity;
import android.net.Uri;
import android.util.Log;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.bubble.registration.TakePictureRecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class EditPicturesFragmentDialogViewModel extends ViewModel {

   List<Uri> data = new ArrayList<>(Collections.nCopies(6,Uri.EMPTY));
   MutableLiveData<Boolean> taskResult = new MutableLiveData<>();
   MutableLiveData<Boolean> downloadResult = new MutableLiveData<>();

   public void createAdapterData(FragmentActivity activity){
       EditPicturesFragmentDialogModel.createAdapterData(activity, data, downloadResult);
   }

   public void onResult(int requestCode, Uri data) {
      Log.d("Picture", "2");
      EditPicturesFragmentDialogModel.onResult(this.data, data, requestCode);
   }

   public TakePictureRecyclerView getAdapter(){
      return EditPicturesFragmentDialogModel.getAdapter();
   }

   public void uploadData() {
      EditPicturesFragmentDialogModel.uploadData(data, taskResult);
   }
}
