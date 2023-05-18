package com.example.bubble;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.window.SplashScreen;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.bubble.databinding.FragmentSettingsBinding;
import com.example.bubble.mainMenu.ChangeHobbyFragmentDialog;
import com.example.bubble.mainMenu.EditPicturesFragmentDialog;
import com.example.bubble.mainMenu.MainActivity;
import com.example.bubble.mainMenu.MyProfileFragment;
import com.example.bubble.mainMenu.SettingsFragmentViewModel;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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
        try {
            binding = FragmentSettingsBinding.inflate(inflater, container, false);
        }catch (Exception e){
            Log.d("Error", e.toString());
        }

        viewModel = new ViewModelProvider(this).get(SettingsFragmentViewModel.class);

        viewModel.user.observe(getViewLifecycleOwner(), firebaseUser -> {
            binding.name.setText(firebaseUser.getDisplayName());
            binding.phoneNumberEditText.setText(firebaseUser.getPhoneNumber());
        });
        viewModel.picture.observe(getViewLifecycleOwner(), storageReference -> {
            Glide.with(requireContext()).load(storageReference).diskCacheStrategy(DiskCacheStrategy.ALL).into(binding.profileImage);
        });
        binding.name.setOnClickListener(v -> {
            replaceFragment(new MyProfileFragment());
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

        binding.logOutText.setOnClickListener(v -> {
            AuthUI.getInstance()
                    .signOut(requireActivity().getApplicationContext())
                    .addOnCompleteListener(task -> restartApp());
        });


        return binding.getRoot();
    }

    private void restartApp() {
        Context ctx = getActivity().getApplicationContext();
        PackageManager pm = ctx.getPackageManager();
        Intent intent = pm.getLaunchIntentForPackage(ctx.getPackageName());
        Intent mainIntent = Intent.makeRestartActivityTask(intent.getComponent());
        ctx.startActivity(mainIntent);
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