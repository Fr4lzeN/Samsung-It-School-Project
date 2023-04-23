package com.example.bubble.registration;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentFillDataLoadingBinding.inflate(inflater, container, false);

        FillDataActivity.getViewModel().allTasksSuccess.observe(getViewLifecycleOwner(), aBoolean -> {
            if (aBoolean){
                mainActivity();
            }
            else{
                FillDataActivity.getViewModel().clearResults();
                Toast.makeText(getContext(), "Ошибка отправки данных", Toast.LENGTH_SHORT).show();
                Navigation.findNavController(binding.getRoot()).popBackStack();
            }
        });

        return binding.getRoot();
    }

    void mainActivity(){
        Intent intent = new Intent(getContext(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}