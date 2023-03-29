package com.example.bubble.mainMenu;

import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.bubble.databinding.FragmentProfileBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.internal.StorageReferenceUri;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ProfileFragment extends Fragment {

    String login;
    FirebaseAuth mAuth;
    FirebaseUser user;
    StorageReference reference;
    FirebaseStorage storage;

    FragmentProfileBinding binding;
    ArrayList<StorageReference> data;

    ProfilePictureRecyclerView adapter;

    public ProfileFragment(String login) {
        this.login = login;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        mAuth = FirebaseAuth.getInstance();
        storage=FirebaseStorage.getInstance();
        binding.signOutButton.setOnClickListener(v -> mAuth.signOut());
        return binding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();
        user = mAuth.getCurrentUser();
        storage = FirebaseStorage.getInstance();
        binding.name.setText(user.getDisplayName());
        binding.info.setText(user.getUid());
        downloadAllItems();
    }

    public void downloadAllItems(){
        reference = storage.getReference().child(user.getUid());
        reference.listAll().addOnCompleteListener(task -> {
            data = new ArrayList<>(task.getResult().getItems());
            adapter = new ProfilePictureRecyclerView(data);
            binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
            binding.recyclerView.setAdapter(adapter);
        });
    }
}