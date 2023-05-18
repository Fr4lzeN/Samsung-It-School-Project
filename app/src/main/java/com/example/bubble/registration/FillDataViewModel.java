package com.example.bubble.registration;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.List;

public class FillDataViewModel extends ViewModel{

    FillDataModel model;


    //User info
    MutableLiveData<String> name = new MutableLiveData<>();
    MutableLiveData<String> info = new MutableLiveData<>();
    MutableLiveData<String> gender = new MutableLiveData<>();
    MutableLiveData<Integer[]> dateOfBirth = new MutableLiveData<>();

    //Recycler view data
    MutableLiveData<List<Uri>> data = new MutableLiveData<>(new ArrayList<>());
    //Recycler view action class
    MutableLiveData<HobbyRecyclerView> hobbiesAdapter = new MutableLiveData<>(null);
    MutableLiveData<Boolean> chipChecked = new MutableLiveData<>(false);

    MutableLiveData<Boolean> firebaseResult = new MutableLiveData<>();


    public void createModel(){
        model = new FillDataModel();
    }

    public String getName() {
        return name.getValue();
    }

    public String getInfo() {
        return info.getValue();
    }


    public void setName(String name) {
        this.name.setValue(name);
    }

    public void setGender(String gender) {
        this.gender.setValue(gender);
    }

    public void setInfo(String info) {
        this.info.setValue(info);
    }

    public void setDateOfBirth(Integer[] dateOfBirth) {
        dateOfBirth[1]+=1;
        this.dateOfBirth.setValue(dateOfBirth);
    }

    public void createHobbyAdapter(){
        model.createHobbyAdapter(hobbiesAdapter, chipChecked);
    }

    public String getGender() {
        return gender.getValue();
    }

    public boolean userInfoCompleted() {
        if (name.getValue()==null || name.getValue().length()<2){
            return false;
        }
        if (dateOfBirth.getValue()==null){
            return false;
        }
        if (gender.getValue()==null){
            return false;
        }
        return true;
    }

    public HobbyRecyclerView getAdapter() {
        return hobbiesAdapter.getValue();
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

    public void sendData() {
        Tasks.whenAll(
        model.createUser(name.getValue(), info.getValue(), gender.getValue(), dateOfBirth.getValue()),
        model.changeUserProfilePicture(name.getValue() ,data.getValue().get(0)),
        model.uploadHobby(),
        model.uploadUserPictures(data.getValue())).addOnCompleteListener(task -> firebaseResult.setValue(task.isSuccessful()));
    }
}
