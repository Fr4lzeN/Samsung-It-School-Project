package com.example.bubble.mainMenu;

import android.app.Activity;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.example.bubble.registration.HobbyRecyclerView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.GenericTypeIndicator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ChangeHobbyFragmentDialogModel {
    public static void getCurrentHobbies(String uid, List<String> hobbyList, ArrayList<Boolean> currentHobbies, MutableLiveData<Boolean> hobbiesDownloaded) {
        FirebaseActions.getHobbies(uid).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                ArrayList<Boolean> tempHobbies = new ArrayList<>(Collections.nCopies(hobbyList.size(), false));
                ArrayList<String> temp = task.getResult().getValue(new GenericTypeIndicator<ArrayList<String>>() {});
                if (temp!=null)
                for (String i : temp){
                    tempHobbies.set(hobbyList.indexOf(i), true);
                }
                Collections.copy(currentHobbies, tempHobbies);

                hobbiesDownloaded.setValue(true);
            }
        });
    }


    public static void createAdapter(Activity activity, List<String> hobbyList, ArrayList<Boolean> hobbies, MutableLiveData<HobbyRecyclerView> adapter) {
        adapter.setValue(new HobbyRecyclerView(activity, hobbyList, hobbies, (position, isChecked) -> {
            adapter.getValue().notifyItemChanged(position);
            Log.d("Hobby", hobbyList.get(position)+" "+position+" "+ isChecked);
            Log.d("Hobby", hobbies.get(position)+" "+position+" "+ isChecked);
        }));
    }

    public static void changeHobbies(List<String> hobbyList, ArrayList<Boolean> currentHobbies, MutableLiveData<Boolean> result) {
        Tasks.whenAll(FirebaseActions.setHobbies(hobbyList, currentHobbies))
                .addOnCompleteListener(task -> result.setValue(task.isSuccessful()));
    }
}
