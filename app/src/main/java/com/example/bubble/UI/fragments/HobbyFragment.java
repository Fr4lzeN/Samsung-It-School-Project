package com.example.bubble.UI.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;

import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.bubble.R;
import com.example.bubble.databinding.FragmentHobbyBinding;
import com.example.bubble.UI.viewModels.FillDataViewModel;
import com.example.bubble.tools.textWatchers.MyTextWatcher;

public class HobbyFragment extends Fragment{

    FragmentHobbyBinding binding;
    FillDataViewModel viewModel;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentHobbyBinding.inflate(inflater,container, false);
        viewModel = new ViewModelProvider(requireActivity()).get(FillDataViewModel.class);
        binding.backButton.setOnClickListener(view -> {
                Navigation.findNavController(binding.getRoot()).popBackStack();
        });


        binding.nextButton.setOnClickListener(view -> {
            Navigation.findNavController(binding.getRoot()).navigate(R.id.action_hobbyFragment_to_loadPictureFragment);
        });
        binding.recyclerView.setLayoutManager(new GridLayoutManager(getContext(),6, GridLayoutManager.HORIZONTAL, false));
        viewModel.hobbiesAdapter.observe(getViewLifecycleOwner(), hobbyRecyclerView -> {
            if (hobbyRecyclerView==null){
                viewModel.createHobbyAdapter();
            }else {
                binding.recyclerView.setAdapter(hobbyRecyclerView);
            }
        });
        viewModel.chipChecked.observe(getViewLifecycleOwner(), aBoolean -> {
            binding.nextButton.setEnabled(aBoolean);
        });
        binding.search.addTextChangedListener(new MyTextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                viewModel.getAdapter().getFilter().filter(s);
            }
        });
        return binding.getRoot();
    }

    void buttonColor(){

    }

}