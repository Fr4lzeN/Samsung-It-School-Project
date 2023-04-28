package com.example.bubble.mainMenu;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.bubble.R;
import com.example.bubble.databinding.FragmentMessagesBinding;

public class MessagesFragment extends Fragment {

    FragmentMessagesBinding binding;
    MessagesFragmentViewModel viewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentMessagesBinding.inflate(inflater,container, false);
        viewModel = new ViewModelProvider(this).get(MessagesFragmentViewModel.class);
        viewModel.createAdapter(this);
        viewModel.adapter.observe(getViewLifecycleOwner(), messageListAdapter -> {
            binding.recyclerView.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false));
            binding.recyclerView.setAdapter(messageListAdapter);
        });
        return binding.getRoot();
    }
}