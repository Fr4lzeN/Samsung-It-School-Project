package com.example.bubble.data.models;

import androidx.lifecycle.MutableLiveData;

import com.example.bubble.data.firebase.FirebaseActions;
import com.example.bubble.UI.adapter.HobbyAdapter;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.GenericTypeIndicator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ChangeHobbyFragmentDialogModel {


    public static void createAdapter(MutableLiveData<HobbyAdapter> adapter, MutableLiveData<Boolean> chipChecked) {
        FirebaseActions.getHobbyList().addOnCompleteListener(task -> {
            ArrayList<String> hobby=task.getResult().getValue(new GenericTypeIndicator<ArrayList<String>>() {});

            FirebaseActions.getHobbies(FirebaseAuth.getInstance().getUid()).addOnCompleteListener(task1 -> {
                ArrayList<Boolean> checked = new ArrayList<>(Collections.nCopies(hobby.size(),false));
                for (String i : task1.getResult().getValue(new GenericTypeIndicator<ArrayList<String>>() {})){
                    checked.set(hobby.indexOf(i), true);
                }
                adapter.setValue(new HobbyAdapter(hobby,checked, chipChecked::setValue));
            });
        });
    }

    public static void changeHobbies(HobbyAdapter hobbiesAdapter, MutableLiveData<Boolean> result) {
        Task<Void> voidTask = Tasks.whenAll(FirebaseActions.setHobbies(hobbiesAdapter.getData(), hobbiesAdapter.getChecked()))
                .addOnCompleteListener(task -> result.setValue(task.isSuccessful()));
    }
}
