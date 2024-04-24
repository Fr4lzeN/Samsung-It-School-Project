package com.example.bubble.UI.viewModels;

import android.net.Uri;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.bubble.data.models.EditPicturesFragmentDialogModel;
import com.example.bubble.data.models.FillDataModel;
import com.example.bubble.UI.adapter.TakePictureAdapter;

import java.util.ArrayList;
import java.util.List;

public class EditPicturesFragmentDialogViewModel extends ViewModel {

    public MutableLiveData<List<Uri>> data = new MutableLiveData<>(new ArrayList<>());
    FillDataModel model;
   public MutableLiveData<Boolean> taskResult = new MutableLiveData<>();
   public MutableLiveData<Boolean> downloadResult = new MutableLiveData<>();


   public TakePictureAdapter getAdapter(){
      return EditPicturesFragmentDialogModel.getAdapter();
   }

   public void uploadData(String uid) {
      EditPicturesFragmentDialogModel.uploadData(data.getValue(), taskResult, uid);
   }

   public void downloadData(String uid){
       EditPicturesFragmentDialogModel.getPictures(data, uid);
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
