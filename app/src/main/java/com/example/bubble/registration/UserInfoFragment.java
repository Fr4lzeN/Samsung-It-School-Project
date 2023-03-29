package com.example.bubble.registration;

import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
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

        binding.name.setText(FillDataActivity.getName());
        binding.info.setText(FillDataActivity.getInfo());
        int[] temp = FillDataActivity.getDateOfBirth();
        if (temp!=null) {
            binding.dateOfBirth.setText(String.valueOf(temp[2]) + "/" + String.valueOf(temp[1] + 1) + "/" + String.valueOf(temp[0]));
        }
        buttonColor();

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

        datePicker = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                FillDataActivity.setDateOfBirth(year, month, dayOfMonth);
                binding.dateOfBirth.setText(String.valueOf(dayOfMonth)+"/"+String.valueOf(month+1)+"/"+String.valueOf(year));
                buttonColor();
            }
        };

        binding.name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                FillDataActivity.setName(binding.name.getText().toString());
                buttonColor();
            }
        });

        binding.info.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                FillDataActivity.setInfo(binding.info.getText().toString());
                buttonColor();
            }
        });

        binding.nextButton.setOnClickListener(view -> {
                if (nextFragment){
                    Navigation.findNavController(binding.getRoot()).navigate(R.id.action_userInfoFragment_to_hobbyFragment);
                }
                else{
                    if (TextUtils.isEmpty(FillDataActivity.getName()) || FillDataActivity.getName().length()<2){
                        binding.name.requestFocus();
                        Toast.makeText(getContext(), "Необходимо ввести имя", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (TextUtils.isEmpty(FillDataActivity.getInfo())){
                        binding.info.requestFocus();
                        Toast.makeText(getContext(), "Необходимо заполнить описание", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (FillDataActivity.getDateOfBirth().length==0){
                        binding.dateOfBirth.requestFocus();
                        Toast.makeText(getContext(), "Необходимо выбрать дату рождения", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
        });

        return binding.getRoot();
    }

    void buttonColor(){
        if (!TextUtils.isEmpty(FillDataActivity.getInfo())&&!TextUtils.isEmpty(FillDataActivity.getName())&&FillDataActivity.getName().length()>=2 && FillDataActivity.getDateOfBirth()!=null){
            binding.nextButton.setBackgroundColor(getResources().getColor(R.color.green));
            nextFragment=true;
        }else{
            binding.nextButton.setBackgroundColor(getResources().getColor(R.color.purple_500));
            nextFragment=false;
        }
    }

}