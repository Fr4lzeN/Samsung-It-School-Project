package com.example.bubble;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.bubble.databinding.FragmentHobbyBinding;

public class HobbyFragment extends Fragment {

    FragmentHobbyBinding binding;
    boolean nextFragment = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentHobbyBinding.inflate(inflater,container, false);
        if (FillDataActivity.getGender()!=null)
        switch (FillDataActivity.getGender()){
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
        binding.backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(binding.getRoot()).popBackStack();
            }
        });
        binding.male.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (binding.male.isChecked()){
                    FillDataActivity.setGender("male");
                }
                else{
                    FillDataActivity.setGender("");
                }
                buttonColor();
            }
        });

        binding.female.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (binding.female.isChecked()){
                    FillDataActivity.setGender("female");;
                }
                else{
                    FillDataActivity.setGender("");;
                }
                buttonColor();
            }
        });

        binding.other.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (binding.other.isChecked()){
                    FillDataActivity.setGender("other");;
                }
                else{
                    FillDataActivity.setGender("");;
                }
                buttonColor();
            }
        });

        binding.nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (nextFragment){
                    Navigation.findNavController(binding.getRoot()).navigate(R.id.action_hobbyFragment_to_loadPictureFragment);
                }
                else{
                    if (TextUtils.isEmpty(FillDataActivity.getGender())){
                        Toast.makeText(getContext(), "Выберите свой пол", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        return binding.getRoot();
    }

    void buttonColor(){
        if (!TextUtils.isEmpty(FillDataActivity.getGender())){
            binding.nextButton.setBackgroundColor(getResources().getColor(R.color.green));
            nextFragment=true;
        }
        else{
            binding.nextButton.setBackgroundColor(getResources().getColor(R.color.purple_500));
            nextFragment=false;
        }
    }
}