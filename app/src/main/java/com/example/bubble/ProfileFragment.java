package com.example.bubble;

import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.bubble.databinding.FragmentProfileBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ProfileFragment extends Fragment {

    String login;
    FirebaseAuth mAuth;
    FirebaseUser user;
    StorageReference reference;

    FragmentProfileBinding binding;
    ArrayList<Uri> data;

    ProfilePictureRecyclerView adapter;

    public ProfileFragment(String login) {
        this.login = login;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        mAuth = FirebaseAuth.getInstance();
        binding.recyclerView.setVisibility(View.GONE);
        user = mAuth.getCurrentUser();
        binding.name.setText(user.getDisplayName());
        binding.info.setText(user.getUid());
        downloadAllItems();
        binding.signOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
            }
        });
        return binding.getRoot();
    }

    public void downloadAllItems(){
        reference = FirebaseStorage.getInstance().getReference(user.getUid());
        reference.listAll().addOnCompleteListener(new OnCompleteListener<ListResult>() {
            @Override
            public void onComplete(@NonNull Task<ListResult> task) {
                int size = task.getResult().getItems().size();
                data = new ArrayList<>(Collections.nCopies(size,Uri.EMPTY));
                for (int i=0; i<size; i++){
                    Log.d("DownloadPicture", String.valueOf(i));
                    getItem(task.getResult().getItems().get(i), i, size);
                }
            }
        });
    }

    private void getItem(StorageReference storageReference, int i, int size) {
        storageReference.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
             data.set(i,task.getResult());
                Log.d("DownloadPicture", String.valueOf(i));
             if (data.size()==size){
                 adapter = new ProfilePictureRecyclerView(data);
                 binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
                 binding.recyclerView.setAdapter(adapter);
                 binding.recyclerView.setVisibility(View.VISIBLE);
             }
            }
        });
    }


}