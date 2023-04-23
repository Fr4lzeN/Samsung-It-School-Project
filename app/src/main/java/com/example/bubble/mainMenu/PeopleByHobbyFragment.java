package com.example.bubble.mainMenu;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.bubble.R;
import com.example.bubble.databinding.FragmentPeopleByHobbyBinding;
import com.google.firebase.auth.FirebaseAuth;


public class PeopleByHobbyFragment extends Fragment {

   FragmentPeopleByHobbyBinding binding;
   PeopleByHobbyFragmentViewModel viewModel;
   String hobby;

    public PeopleByHobbyFragment(String hobby) {
        this.hobby = hobby;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentPeopleByHobbyBinding.inflate(inflater,container, false);
        viewModel = new ViewModelProvider(this).get(PeopleByHobbyFragmentViewModel.class);
        binding.textView.setText(hobby);
        viewModel.createAdapter(this, hobby);
        viewModel.adapter.observe(getViewLifecycleOwner(), adapter -> {
            binding.recyclerView.setLayoutManager(new LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false));
            binding.recyclerView.setAdapter(adapter);
        });
        return binding.getRoot();
    }

}