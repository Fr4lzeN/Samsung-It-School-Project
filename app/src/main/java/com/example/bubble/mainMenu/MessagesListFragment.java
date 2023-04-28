package com.example.bubble.mainMenu;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.bubble.R;
import com.example.bubble.databinding.FragmentMessagesListBinding;

public class MessagesListFragment extends Fragment {

    FragmentMessagesListBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentMessagesListBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }
}