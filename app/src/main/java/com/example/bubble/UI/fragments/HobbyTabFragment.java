package com.example.bubble.UI.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.bubble.databinding.FragmentHobbyTabBinding;
import com.example.bubble.UI.viewModels.ProfileFragmentViewModel;
import com.example.bubble.UI.adapter.ProfileHobbyAdapter;

public class HobbyTabFragment extends Fragment {

    FragmentHobbyTabBinding binding;
    ProfileFragmentViewModel viewModel;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentHobbyTabBinding.inflate(inflater,container,false);
        viewModel = new ViewModelProvider(requireParentFragment()).get(ProfileFragmentViewModel.class);
        viewModel.hobbies.observe(getViewLifecycleOwner(), strings -> {
            if (strings!=null){
                binding.hobbyRecyclerView.setLayoutManager(new GridLayoutManager(requireContext(), 2, GridLayoutManager.VERTICAL, false));
                binding.hobbyRecyclerView.setAdapter(new ProfileHobbyAdapter(strings));
            }
        });
        return binding.getRoot();
    }
}