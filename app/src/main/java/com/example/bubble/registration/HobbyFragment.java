package com.example.bubble.registration;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.bubble.R;
import com.example.bubble.databinding.FragmentHobbyBinding;

public class HobbyFragment extends Fragment implements View.OnClickListener{

    FragmentHobbyBinding binding;
    boolean nextFragment = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentHobbyBinding.inflate(inflater,container, false);
        if (FillDataActivity.getGender()!=null) {
            switch (FillDataActivity.getGender()) {
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
        }
        buttonColor();
        binding.backButton.setOnClickListener(view -> {
                Navigation.findNavController(binding.getRoot()).popBackStack();
        });
        binding.male.setOnClickListener(this);

        binding.female.setOnClickListener(this);

        binding.other.setOnClickListener(this);

        binding.nextButton.setOnClickListener(view -> {
                if (nextFragment){
                    Navigation.findNavController(binding.getRoot()).navigate(R.id.action_hobbyFragment_to_loadPictureFragment);
                }
                else{
                    if (TextUtils.isEmpty(FillDataActivity.getGender())){
                        Toast.makeText(getContext(), "Выберите свой пол", Toast.LENGTH_SHORT).show();
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

    @Override
    public void onClick(View v) {
        if(binding.male.isChecked()){
            FillDataActivity.setGender("male");
        }
        if(binding.female.isChecked()){
            FillDataActivity.setGender("female");
        }
        if (binding.other.isChecked()){
            FillDataActivity.setGender("other");
        }
        buttonColor();
    }
}