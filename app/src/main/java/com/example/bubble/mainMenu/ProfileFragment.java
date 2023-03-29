package com.example.bubble.mainMenu;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.bubble.JSON.UserInfoJSON;
import com.example.bubble.databinding.FragmentProfileBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Calendar;

public class ProfileFragment extends Fragment {

    FirebaseAuth mAuth;
    FirebaseUser user;
    UserInfoJSON myUser;
    FirebaseStorage storage;
    FirebaseDatabase database;
    FragmentProfileBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        mAuth = FirebaseAuth.getInstance();
        storage=FirebaseStorage.getInstance();
        database = FirebaseDatabase.getInstance();
        binding.signOutButton.setOnClickListener(v -> mAuth.signOut());
        return binding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();
        user = mAuth.getCurrentUser();
        downloadUserData();
        binding.name.setText(user.getDisplayName());
        downloadPictures();
    }

    private void downloadUserData() {
        DatabaseReference databaseReference=database.getReference("userData").child(user.getUid());
        databaseReference.keepSynced(true);
        databaseReference.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                myUser = task.getResult().getValue(UserInfoJSON.class);
                binding.info.setText(myUser.info);
                displayAge(myUser.dateOfBirth);
            }
            else{
                Log.d("DB", "errpr");
            }
        });
    }

    public void downloadPictures(){
        StorageReference reference = storage.getReference().child(user.getUid());
        reference.listAll().addOnCompleteListener(task -> {
            ArrayList<StorageReference> data = new ArrayList<>(task.getResult().getItems());
            ProfilePictureRecyclerView adapter = new ProfilePictureRecyclerView(data);
            binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
            binding.recyclerView.setAdapter(adapter);
        });
    }

    public void displayAge(UserInfoJSON.DateOfBirth dateOfBirth){
        Calendar today = Calendar.getInstance();
        int year = today.get(Calendar.YEAR)- dateOfBirth.year;
        if ((dateOfBirth.month>today.get(Calendar.MONTH)) ||((dateOfBirth.month==today.get(Calendar.MONTH)) && dateOfBirth.day>today.get(Calendar.DAY_OF_MONTH)) ){
            year--;
        }
        binding.name.setText(binding.name.getText().toString()+", "+String.valueOf(year));
    }

}