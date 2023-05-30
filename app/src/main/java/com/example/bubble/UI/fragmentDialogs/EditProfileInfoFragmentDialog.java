package com.example.bubble.UI.fragmentDialogs;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.widget.NestedScrollView;
import androidx.lifecycle.ViewModelProvider;

import com.example.bubble.UI.viewModels.EditProfileInfoFragmentDialogViewModel;
import com.example.bubble.data.JSONModels.UserInfoJSON;
import com.example.bubble.databinding.FragmentDialogEditProfileInfoBinding;
import com.example.bubble.UI.bottomDialogFragment.SetGenderBottomSheetFragment;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;


public class EditProfileInfoFragmentDialog extends BottomSheetDialogFragment{

    public interface DismissListener {
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
        binding.dateOfBirth.setOnClickListener(v -> {
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
            clearChip();
        });

        binding.femaleChip.setOnClickListener(v -> {
            viewModel.setGender(binding.maleChip.getText().toString());
            clearChip();
        });

        binding.unknownChip.setOnClickListener(v -> {
            viewModel.setGender(binding.maleChip.getText().toString());
            clearChip();
        });

        binding.otherChip.setOnClickListener(v -> {
            SetGenderBottomSheetFragment fragment = new SetGenderBottomSheetFragment(gender -> {
                if (!TextUtils.isEmpty(gender)) {
                    viewModel.setGender(gender);
                    binding.otherChip.setText(gender);
                }else{
                    binding.unknownChip.performClick();
                    binding.horizontalScrollView.scrollBy(400,0);
                }
            });
            fragment.show(getParentFragmentManager(), null);

        });

        datePicker = (view, year, month, dayOfMonth) -> {
            binding.dateOfBirth.setText(dayOfMonth+"/"+(month+1)+"/"+year);
        };

        binding.toolBar.setNavigationOnClickListener(v -> {
            dismiss();
        });

        binding.finish.setOnClickListener(v -> {
            binding.layout.setVisibility(View.INVISIBLE);
            binding.progressBar.setVisibility(View.VISIBLE);
            viewModel.updateInfo(binding.name.getText().toString(),
                    binding.info.getText().toString(),
                    binding.dateOfBirth.getText().toString());
        });



        viewModel.result.observe(getViewLifecycleOwner(), aBoolean -> {
            if (aBoolean){
                dismiss();
            }
            else{
                Toast.makeText(requireContext(), "Ошибка", Toast.LENGTH_SHORT).show();
                binding.layout.setVisibility(View.VISIBLE);
                binding.progressBar.setVisibility(View.GONE);
            }
        });


        return binding.getRoot();

    }

    private void clearChip() {
        binding.otherChip.setSelected(false);
        binding.otherChip.setText("Свой вариант");
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        bottomSheetBehavior = BottomSheetBehavior.from((View) view.getParent());
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        NestedScrollView layout = binding.getRoot();
        //layout.setMinimumHeight((int) (Resources.getSystem().getDisplayMetrics().heightPixels*0.8));
        ViewGroup.LayoutParams params = layout.getLayoutParams();
        params.height= (int) (Resources.getSystem().getDisplayMetrics().heightPixels*0.9);
        binding.appBar.addOnOffsetChangedListener(
                (AppBarLayout.BaseOnOffsetChangedListener) (appBarLayout, verticalOffset) -> {
            bottomSheetBehavior.setDraggable((appBarLayout.getBottom() - verticalOffset) == appBarLayout.getBottom());
        });

        layout.setLayoutParams(params);
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        listener.dismiss();
    }

    private void displayInfo(UserInfoJSON userInfoJSON) {
        binding.name.setText(userInfoJSON.name);
        binding.info.setText(userInfoJSON.info);
        binding.dateOfBirth.setText(userInfoJSON.dateOfBirth.day+"/"+userInfoJSON.dateOfBirth.month+"/"+userInfoJSON.dateOfBirth.year);
        switch (userInfoJSON.gender){
            case "Мужчина":
                binding.maleChip.performClick();
                break;
            case "Женщина":
                binding.femaleChip.performClick();
                break;
            case "Не указан":
                binding.unknownChip.performClick();
                break;
            default:
                binding.otherChip.setSelected(true);
                binding.otherChip.setText(userInfoJSON.gender);
                break;
        }
        viewModel.userInfo.removeObservers(getViewLifecycleOwner());
    }


}