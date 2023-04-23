package com.example.bubble;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.bubble.databinding.FragmentSettingsBinding;
import com.example.bubble.mainMenu.ChangeHobbyFragmentDialog;
import com.example.bubble.mainMenu.EditPicturesFragmentDialog;
import com.example.bubble.mainMenu.MyProfileFragment;
import com.example.bubble.mainMenu.SettingsFragmentViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SettingsFragment extends Fragment {

    FragmentSettingsBinding binding;
    FirebaseUser user;

    SettingsFragmentViewModel viewModel;
    EditProfileInfoFragmentDialog dialogProfile;
    EditPicturesFragmentDialog dialogPicture;
    ChangeHobbyFragmentDialog dialogHobby;

    public SettingsFragment(FirebaseUser user) {
        this.user = user;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSettingsBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(this).get(SettingsFragmentViewModel.class);

        viewModel.user.observe(getViewLifecycleOwner(), firebaseUser -> {
            binding.name.setText(firebaseUser.getDisplayName());
            binding.phoneNumber.setText(firebaseUser.getPhoneNumber());
        });
        viewModel.picture.observe(getViewLifecycleOwner(), storageReference -> {
            Glide.with(requireContext()).load(storageReference).diskCacheStrategy(DiskCacheStrategy.ALL).into(binding.profileImage);
        });
        binding.name.setOnClickListener(v -> {
            replaceFragment(new MyProfileFragment());
        });

        binding.editProfileButton.setOnClickListener(v -> {
            replaceFragment(new ProfileEditFragment());
        });

        binding.editProfileText.setOnClickListener(v -> {
            dialogProfile = new EditProfileInfoFragmentDialog(() -> viewModel.refreshUser());
            dialogProfile.show(getParentFragmentManager(), "TAG");

        });
        binding.changeImagesText.setOnClickListener(v -> {
            dialogPicture = new EditPicturesFragmentDialog(() -> viewModel.refreshUser());
            dialogPicture.show(getParentFragmentManager(), "TAG");
        });

        binding.chooseHobbyText.setOnClickListener(v -> {
            dialogHobby = new ChangeHobbyFragmentDialog();
            dialogHobby.show(getParentFragmentManager(), null);
        });

        binding.logOutText.setOnClickListener(v -> FirebaseAuth.getInstance().signOut());


        return binding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();
        viewModel.downloadPicture();
    }

    private void replaceFragment(Fragment fragment) {
        FragmentTransaction ft = getParentFragmentManager().beginTransaction();
        ft.replace(R.id.nav_host_fragment, fragment);
        ft.addToBackStack(null);
        ft.commit();
    }



}