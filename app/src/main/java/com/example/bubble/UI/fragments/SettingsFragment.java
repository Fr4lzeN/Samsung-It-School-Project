package com.example.bubble.UI.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.bubble.UI.fragmentDialogs.EditProfileInfoFragmentDialog;
import com.example.bubble.R;
import com.example.bubble.UI.bottomDialogFragment.ChangeHobbyFragmentDialog;
import com.example.bubble.UI.bottomDialogFragment.EditPicturesFragmentDialog;
import com.example.bubble.UI.viewModels.SettingsFragmentViewModel;
import com.example.bubble.databinding.FragmentSettingsBinding;
import com.firebase.ui.auth.AuthUI;
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

    public SettingsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSettingsBinding.inflate(inflater, container, false);

        viewModel = new ViewModelProvider(this).get(SettingsFragmentViewModel.class);

        viewModel.getPrivacy();

        viewModel.user.observe(getViewLifecycleOwner(), firebaseUser -> {
            binding.name.setText(firebaseUser.getDisplayName());
            binding.phoneNumberEditText.setText(firebaseUser.getPhoneNumber());
        });
        viewModel.picture.observe(getViewLifecycleOwner(), storageReference -> {
            Glide.with(requireContext()).load(storageReference).diskCacheStrategy(DiskCacheStrategy.ALL).into(binding.profileImage);
        });

        viewModel.privacy.observe(getViewLifecycleOwner(), aBoolean -> {
            if (aBoolean!=null) {
                binding.privacySwitcher.setChecked(aBoolean);
            }else{
                binding.privacySwitcher.setChecked(false);
            }
        });

        binding.openProfile.setOnClickListener(v -> replaceFragment(new MyProfileFragment()));

        binding.editProfile.setOnClickListener(v -> {
            dialogProfile = new EditProfileInfoFragmentDialog(() -> viewModel.refreshUser());
            dialogProfile.show(getParentFragmentManager(), "TAG");

        });

        binding.privacySwitcher.setOnClickListener(v -> viewModel.setPrivacy(binding.privacySwitcher.isChecked()));

        binding.editPictures.setOnClickListener(v -> {
            dialogPicture = new EditPicturesFragmentDialog(() -> {
                viewModel.refreshUser();
                viewModel.downloadPicture();
            });
            dialogPicture.show(getParentFragmentManager(), "TAG");
        });

        binding.editHobby.setOnClickListener(v -> {
            dialogHobby = new ChangeHobbyFragmentDialog();
            dialogHobby.show(getParentFragmentManager(), null);
        });

        binding.logout.setOnClickListener(v -> {
            AuthUI.getInstance()
                    .signOut(requireActivity().getApplicationContext())
                    .addOnCompleteListener(task -> restartApp());
        });

        binding.messageUs.setOnClickListener(v -> {
            sendMessage();
        });


        return binding.getRoot();
    }


    private void sendMessage() {
        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("message/rfc822");
        i.putExtra(Intent.EXTRA_EMAIL  , new String[]{"friended.app.java@gmail.com"});
        i.putExtra(Intent.EXTRA_SUBJECT, "");
        i.putExtra(Intent.EXTRA_TEXT   , "");
        try {
            startActivity(Intent.createChooser(i, "Send mail..."));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(requireContext(), "There are no email clients installed.", Toast.LENGTH_SHORT).show();
        }
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