package com.example.bubble.registration;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.bubble.R;
import com.example.bubble.databinding.FragmentFillDataLoadingBinding;
import com.example.bubble.mainMenu.MainActivity;


public class FillDataLoadingFragment extends Fragment {

    FragmentFillDataLoadingBinding binding;
    FillDataViewModel viewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentFillDataLoadingBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(requireActivity()).get(FillDataViewModel.class);
        viewModel.firebaseResult.observe(getViewLifecycleOwner(), aBoolean -> {
            if (aBoolean){
                Intent intent = new Intent(requireContext(), MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }else{
                Toast.makeText(requireContext(), "Ошибка отправки данных", Toast.LENGTH_LONG).show();
                Navigation.findNavController(binding.getRoot()).popBackStack();
            }
        });
        return binding.getRoot();
    }

}