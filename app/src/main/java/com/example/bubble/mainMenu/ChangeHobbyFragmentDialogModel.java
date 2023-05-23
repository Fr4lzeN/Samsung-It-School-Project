package com.example.bubble.mainMenu;

import androidx.lifecycle.MutableLiveData;

import com.example.bubble.registration.HobbyRecyclerView;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.GenericTypeIndicator;

import java.util.ArrayList;
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


    public static void createAdapter(MutableLiveData<HobbyRecyclerView> adapter, MutableLiveData<Boolean> chipChecked) {
        FirebaseActions.getHobbyList().addOnCompleteListener(task -> {
            ArrayList<String> hobby=task.getResult().getValue(new GenericTypeIndicator<ArrayList<String>>() {});

            FirebaseActions.getHobbies(FirebaseAuth.getInstance().getUid()).addOnCompleteListener(task1 -> {
                ArrayList<Boolean> checked = new ArrayList<>(Collections.nCopies(hobby.size(),false));
                for (String i : task1.getResult().getValue(new GenericTypeIndicator<ArrayList<String>>() {})){
                    checked.set(hobby.indexOf(i), true);
                }
                adapter.setValue(new HobbyRecyclerView(hobby,checked, chipChecked::setValue));
            });
        });
    }

    public static void changeHobbies(HobbyRecyclerView hobbiesAdapter, MutableLiveData<Boolean> result) {
        Task<Void> voidTask = Tasks.whenAll(FirebaseActions.setHobbies(hobbiesAdapter.getData(), hobbiesAdapter.getChecked()))
                .addOnCompleteListener(task -> result.setValue(task.isSuccessful()));
    }
}
