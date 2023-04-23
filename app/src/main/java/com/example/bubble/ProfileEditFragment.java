package com.example.bubble;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.bubble.databinding.FragmentProfileEditBinding;

public class ProfileEditFragment extends Fragment {

    FragmentProfileEditBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentProfileEditBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }
}