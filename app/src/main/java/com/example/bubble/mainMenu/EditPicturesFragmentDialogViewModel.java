package com.example.bubble.mainMenu;

import android.app.Activity;
import android.net.Uri;
import android.util.Log;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.bubble.registration.FillDataModel;
import com.example.bubble.registration.TakePictureRecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class EditPicturesFragmentDialogViewModel extends ViewModel {

    MutableLiveData<List<Uri>> data = new MutableLiveData<>(new ArrayList<>());
    FillDataModel model;
   MutableLiveData<Boolean> taskResult = new MutableLiveData<>();
   MutableLiveData<Boolean> downloadResult = new MutableLiveData<>();


   public TakePictureRecyclerView getAdapter(){
      return EditPicturesFragmentDialogModel.getAdapter();
   }

   public void uploadData() {
      EditPicturesFragmentDialogModel.uploadData(data.getValue(), taskResult);
   }

   public void downloadData(){
       EditPicturesFragmentDialogModel.getPictures(data);
   }

    public void addPictureToAdapter(List<Uri> data) {
        List<Uri> temp = this.data.getValue();
        for (Uri i: data) {
            temp.add(i);
            if (temp.size()==5) break;
        }
        this.data.setValue(temp);
    }

    public void changeAdapterPicture(Uri data, int position) {
        List<Uri> temp = this.data.getValue();
        temp.set(position,data);
        this.data.setValue(temp);
    }

    public void deleteAdapterPicture(int position){
        List<Uri> temp = this.data.getValue();
        temp.remove(position);
        this.data.setValue(temp);
    }

    public void createModel(){
        model = new FillDataModel();
    }
}
