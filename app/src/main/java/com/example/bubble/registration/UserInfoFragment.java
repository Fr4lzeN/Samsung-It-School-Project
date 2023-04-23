package com.example.bubble.registration;

import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.navigation.Navigation;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.Toast;

import com.example.bubble.R;
import com.example.bubble.databinding.FragmentUserInfoBinding;
import com.example.bubble.registration.FillDataActivity;

import java.util.Calendar;
import java.util.Date;

public class UserInfoFragment extends Fragment {

    FragmentUserInfoBinding binding;
    DatePickerDialog.OnDateSetListener datePicker;
    boolean nextFragment = false;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentUserInfoBinding.inflate(inflater, container, false);
        binding.name.setText(FillDataActivity.getViewModel().getName());
        binding.info.setText(FillDataActivity.getViewModel().getInfo());
        FillDataActivity.getViewModel().dateOfBirth.observe(getViewLifecycleOwner(), temp ->{
            binding.dateOfBirth.setText(temp[2] + "/" + temp[1] + "/" + temp[0]);
            buttonColor();
        });

        binding.dateOfBirth.setOnClickListener(view -> {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), datePicker,
                        year,
                        month,
                        day);
                datePickerDialog.getDatePicker().setMaxDate(new Date().getTime());
                datePickerDialog.show();
        });

        datePicker = (view, year, month, dayOfMonth) -> {
            FillDataActivity.getViewModel().setDateOfBirth(new Integer[]{year,month,dayOfMonth});
        };

        binding.name.addTextChangedListener(new MyTextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                FillDataActivity.getViewModel().setName(s.toString());
                buttonColor();
            }
        });

        binding.info.addTextChangedListener(new MyTextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                FillDataActivity.getViewModel().setInfo(s.toString());
                buttonColor();
            }
        });


        binding.nextButton.setOnClickListener(view -> {
                if (nextFragment){
                    Navigation.findNavController(binding.getRoot()).navigate(R.id.action_userInfoFragment_to_hobbyFragment);
                }
                else{
                    if (TextUtils.isEmpty(binding.name.getText().toString()) || binding.name.getText().toString().length()<2){
                        binding.name.requestFocus();
                        Toast.makeText(getContext(), "Необходимо ввести имя", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (TextUtils.isEmpty(binding.info.getText().toString())){
                        binding.info.requestFocus();
                        Toast.makeText(getContext(), "Необходимо заполнить описание", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (TextUtils.isEmpty(binding.dateOfBirth.getText().toString())){
                        binding.dateOfBirth.requestFocus();
                        Toast.makeText(getContext(), "Необходимо выбрать дату рождения", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
        });

        return binding.getRoot();
    }

    void buttonColor(){
        if (!TextUtils.isEmpty(binding.info.getText().toString()) &&
                !TextUtils.isEmpty(binding.name.getText().toString()) &&
                binding.name.getText().toString().length()>=2 &&
                !TextUtils.isEmpty(binding.dateOfBirth.getText().toString())){
            binding.nextButton.setBackgroundColor(getResources().getColor(R.color.green));
            nextFragment=true;
        }else{
            binding.nextButton.setBackgroundColor(getResources().getColor(R.color.purple_500));
            nextFragment=false;
        }
    }

}