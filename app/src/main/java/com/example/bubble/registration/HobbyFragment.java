package com.example.bubble.registration;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.bubble.R;
import com.example.bubble.databinding.FragmentHobbyBinding;

import java.util.Arrays;
import java.util.List;

public class HobbyFragment extends Fragment implements View.OnClickListener{

    FragmentHobbyBinding binding;
    boolean nextFragment = false;
    String gender;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentHobbyBinding.inflate(inflater,container, false);
        FillDataActivity.getViewModel().gender.observe(getViewLifecycleOwner(), s -> {
            gender=s;
            switch (s){
                case "male":
                    binding.male.setChecked(true);
                    break;
                case "female":
                    binding.female.setChecked(true);
                    break;
                case "other":
                    binding.other.setChecked(true);
                    break;

            }
            buttonColor();
        });
        binding.backButton.setOnClickListener(view -> {
                Navigation.findNavController(binding.getRoot()).popBackStack();
        });
        binding.male.setOnClickListener(this);

        binding.female.setOnClickListener(this);

        binding.other.setOnClickListener(this);

        binding.nextButton.setOnClickListener(view -> {
                if (nextFragment){
                    FillDataActivity.getViewModel().sendUser();
                    Navigation.findNavController(binding.getRoot()).navigate(R.id.action_hobbyFragment_to_loadPictureFragment);
                }
                else{
                    if (TextUtils.isEmpty(gender)){
                        Toast.makeText(getContext(), "Выберите свой пол", Toast.LENGTH_SHORT).show();
                    }
                }
        });
        binding.recyclerView.setLayoutManager(new GridLayoutManager(getContext(),4, GridLayoutManager.HORIZONTAL, false));
        FillDataActivity.getViewModel().createHobbyAdapter(getActivity());
        FillDataActivity.getViewModel().hobbiesAdapter.observe(getViewLifecycleOwner(), hobbyRecyclerView -> {
            binding.recyclerView.setAdapter(hobbyRecyclerView);
        });
        return binding.getRoot();
    }

    void buttonColor(){
        if (!TextUtils.isEmpty(gender)){
            binding.nextButton.setBackgroundColor(getResources().getColor(R.color.green));
            nextFragment=true;
        }
        else{
            binding.nextButton.setBackgroundColor(getResources().getColor(R.color.purple_500));
            nextFragment=false;
        }
    }

    @Override
    public void onClick(View v) {
        if(binding.male.isChecked()){
            FillDataActivity.getViewModel().setGender("male");
        }
        if(binding.female.isChecked()){
            FillDataActivity.getViewModel().setGender("female");
        }
        if (binding.other.isChecked()){
            FillDataActivity.getViewModel().setGender("other");
        }
    }
}