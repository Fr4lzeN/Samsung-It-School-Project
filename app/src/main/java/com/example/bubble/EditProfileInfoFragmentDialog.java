package com.example.bubble;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.lifecycle.ViewModelProvider;

import com.example.bubble.JSON.UserInfoJSON;
import com.example.bubble.databinding.FragmentDialogEditProfileInfoBinding;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;


public class EditProfileInfoFragmentDialog extends BottomSheetDialogFragment {

    interface DismissListener {
        void dismiss();
    }

    private FragmentDialogEditProfileInfoBinding binding;
    DatePickerDialog.OnDateSetListener datePicker;
    EditProfileInfoFragmentDialogViewModel viewModel;
    BottomSheetDialog dialog;
    BottomSheetBehavior<View> bottomSheetBehavior;

    DismissListener listener;

    public EditProfileInfoFragmentDialog(DismissListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        dialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentDialogEditProfileInfoBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(this).get(EditProfileInfoFragmentDialogViewModel.class);
        viewModel.downloadUserInfo();
        viewModel.userInfo.observe(getViewLifecycleOwner(), userInfoJSON -> {
            displayInfo(userInfoJSON);
            binding.progressBar.setVisibility(View.GONE);
            binding.layout.setVisibility(View.VISIBLE);
        });
        binding.dateOfBirthEditText.setOnClickListener(v -> {
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
            binding.dateOfBirthEditText.setText(dayOfMonth+"/"+(month+1)+"/"+year);
        };

        binding.closeImage.setOnClickListener(v -> {
            dismiss();
        });

        binding.checkImage.setOnClickListener(v -> {
            binding.layout.setVisibility(View.GONE);
            binding.progressBar.setVisibility(View.VISIBLE);
            viewModel.updateInfo(binding.nameEditText.getText().toString(),
                    binding.infoEditText.getText().toString(),
                    binding.dateOfBirthEditText.getText().toString());
        });

        viewModel.result.observe(getViewLifecycleOwner(), aBoolean -> {
            if (aBoolean){
                dismiss();
            }
            else{
                Toast.makeText(requireContext(), "Ошибка", Toast.LENGTH_SHORT).show();
            }
        });


        return binding.getRoot();

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        bottomSheetBehavior = BottomSheetBehavior.from((View) view.getParent());
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        CoordinatorLayout layout = binding.getRoot();
        //layout.setMinimumHeight((int) (Resources.getSystem().getDisplayMetrics().heightPixels*0.8));
        ViewGroup.LayoutParams params = layout.getLayoutParams();
        params.height= (int) (Resources.getSystem().getDisplayMetrics().heightPixels*0.9);
        layout.setLayoutParams(params);
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        listener.dismiss();
    }

    private void displayInfo(UserInfoJSON userInfoJSON) {
        binding.nameEditText.setText(userInfoJSON.name);
        binding.infoEditText.setText(userInfoJSON.info);
        binding.dateOfBirthEditText.setText(userInfoJSON.dateOfBirth.day+"/"+userInfoJSON.dateOfBirth.month+"/"+userInfoJSON.dateOfBirth.year);
    }


}