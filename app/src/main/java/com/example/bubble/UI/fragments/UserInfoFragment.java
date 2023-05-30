package com.example.bubble.UI.fragments;

import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.text.Editable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.bubble.R;
import com.example.bubble.databinding.FragmentUserInfoBinding;
import com.example.bubble.UI.FillDataActivity;
import com.example.bubble.UI.viewModels.FillDataViewModel;
import com.example.bubble.UI.bottomDialogFragment.SetGenderBottomSheetFragment;
import com.example.bubble.tools.textWatchers.MyTextWatcher;

import java.util.Calendar;
import java.util.Date;

public class UserInfoFragment extends Fragment {

    FragmentUserInfoBinding binding;
    DatePickerDialog.OnDateSetListener datePicker;


    FillDataViewModel viewModel;
    String otherGenderBase = "Свой вариант";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentUserInfoBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(requireActivity()).get(FillDataViewModel.class);
        binding.name.setText(FillDataActivity.getViewModel().getName());
        binding.info.setText(FillDataActivity.getViewModel().getInfo());
        viewModel.dateOfBirth.observe(getViewLifecycleOwner(), temp ->{
            binding.dateOfBirth.setText(temp[2] + "/" + temp[1] + "/" + temp[0]);
            enableButton();
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

        binding.maleChip.setOnClickListener(v -> {
            viewModel.setGender(binding.maleChip.getText().toString());
            binding.otherChip.setText(otherGenderBase);
            enableButton();
        });

        binding.femaleChip.setOnClickListener(v -> {
            viewModel.setGender(binding.femaleChip.getText().toString());
            binding.otherChip.setText(otherGenderBase);
            enableButton();
        });

        binding.otherChip.setOnClickListener(v -> {
            SetGenderBottomSheetFragment fragment = new SetGenderBottomSheetFragment(gender -> {
                if (!TextUtils.isEmpty(gender)) {
                    viewModel.setGender(gender);
                    binding.otherChip.setText(gender);
                    enableButton();
                }else{
                    binding.unknownChip.performClick();
                    binding.horizontalScrollView.scrollBy(400,0);
                }
            });
            fragment.show(getParentFragmentManager(), null);

        });

        binding.unknownChip.setOnClickListener(v -> {
            viewModel.setGender(binding.unknownChip.getText().toString());
            binding.otherChip.setText(otherGenderBase);
            enableButton();
        });


        datePicker = (view, year, month, dayOfMonth) -> {
            viewModel.setDateOfBirth(new Integer[]{year,month,dayOfMonth});
        };

        binding.name.addTextChangedListener(new MyTextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                viewModel.setName(s.toString());
                enableButton();
            }
        });

        binding.info.addTextChangedListener(new MyTextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                viewModel.setInfo(s.toString());
                enableButton();
            }
        });


        binding.nextButton.setOnClickListener(view -> {
            Navigation.findNavController(binding.getRoot()).navigate(R.id.action_userInfoFragment_to_hobbyFragment);
        });

        return binding.getRoot();
    }


    @Override
    public void onStart() {
        super.onStart();
        if (viewModel.getGender()!=null) {
            switch (viewModel.getGender()) {
                case "Мужчина":
                    binding.maleChip.performClick();
                    break;
                case "Женщина":
                    binding.femaleChip.performClick();
                    break;
                case "Не указан":
                    binding.unknownChip.performClick();
                default:
                    binding.otherChip.setEnabled(true);
                    binding.otherChip.setText(viewModel.getGender());
                    break;
            }
        }
    }

    void enableButton(){
        if (viewModel.userInfoCompleted()){
            binding.nextButton.setEnabled(true);
        }else{
            binding.nextButton.setEnabled(false);
        }
    }

}