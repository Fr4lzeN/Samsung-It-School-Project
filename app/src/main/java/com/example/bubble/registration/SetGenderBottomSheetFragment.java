package com.example.bubble.registration;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.bubble.R;
import com.example.bubble.databinding.FragmentSetGenderBottomSheetBinding;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class SetGenderBottomSheetFragment extends BottomSheetDialogFragment {

   FragmentSetGenderBottomSheetBinding binding;
   String gender;
   public interface OnDismiss{
       void onDismiss(String gender);
   }
   OnDismiss onDismiss;

    public SetGenderBottomSheetFragment(OnDismiss onDismiss) {
        this.onDismiss=onDismiss;
        gender=null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSetGenderBottomSheetBinding.inflate(inflater,container,false);
        binding.closeImage.setOnClickListener(v -> {
            dismiss();
        });
        binding.checkImage.setOnClickListener(v -> {
            gender = binding.name.getText().toString();
            dismiss();
        });
        return binding.getRoot();
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        onDismiss.onDismiss(gender);
    }
}