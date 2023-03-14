package com.example.bubble;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.bubble.databinding.FragmentAuthorizationModeBinding;


public class AuthorizationModeFragment extends Fragment {

    FragmentAuthorizationModeBinding binding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding=FragmentAuthorizationModeBinding.inflate(inflater, container, false);

        binding.loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation
                        .findNavController(binding.getRoot())
                        .navigate(R.id.action_authorizationMode_to_login);
            }
        });

        binding.registrationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation
                        .findNavController(binding.getRoot())
                        .navigate(R.id.action_authorizationMode_to_registration);
            }
        });

        return binding.getRoot();
    }
}