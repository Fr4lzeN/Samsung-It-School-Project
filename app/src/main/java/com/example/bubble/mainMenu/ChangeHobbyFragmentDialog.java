package com.example.bubble.mainMenu;

import android.app.Dialog;
import android.content.res.Resources;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.bubble.R;
import com.example.bubble.databinding.FragmentChangeHobbyDialogBinding;
import com.example.bubble.registration.HobbyRecyclerView;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class ChangeHobbyFragmentDialog extends BottomSheetDialogFragment {

    FragmentChangeHobbyDialogBinding binding;
    ChangeHobbyFragmentDialogViewModel viewModel;
    BottomSheetDialog dialog;
    BottomSheetBehavior<View> bottomSheetBehavior;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        dialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        return dialog;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentChangeHobbyDialogBinding.inflate(inflater,container,false);
        viewModel = new ViewModelProvider(this).get(ChangeHobbyFragmentDialogViewModel.class);
        viewModel.getCurrentHobbies();
        viewModel.hobbiesDownloaded.observe(getViewLifecycleOwner(), aBoolean -> {
            viewModel.createAdapter(requireActivity());
            binding.progressBar.setVisibility(View.GONE);
            binding.layout.setVisibility(View.VISIBLE);
        });
        binding.recyclerView.setLayoutManager(new GridLayoutManager(getContext(),4, GridLayoutManager.HORIZONTAL, false));
        viewModel.adapter.observe(getViewLifecycleOwner(), hobbyRecyclerView -> {
            binding.recyclerView.setAdapter(hobbyRecyclerView);
        });
        binding.closeImage.setOnClickListener(v -> dismiss());
        binding.checkImage.setOnClickListener(v -> {
            binding.progressBar.setVisibility(View.VISIBLE);
            binding.layout.setVisibility(View.GONE);
            viewModel.changeHobbies();
        });
        viewModel.result.observe(getViewLifecycleOwner(), aBoolean -> {
            if (aBoolean){
                dismiss();
            }
            else{
                binding.progressBar.setVisibility(View.GONE);
                binding.layout.setVisibility(View.VISIBLE);
                Toast.makeText(requireContext(), "Ошибка", Toast.LENGTH_SHORT).show();
            }
        });
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        bottomSheetBehavior = BottomSheetBehavior.from((View) view.getParent());
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        ConstraintLayout layout = binding.getRoot();
        //layout.setMinimumHeight((int) (Resources.getSystem().getDisplayMetrics().heightPixels*0.8));
        ViewGroup.LayoutParams params = layout.getLayoutParams();
        params.height= (int) (Resources.getSystem().getDisplayMetrics().heightPixels*0.9);
        layout.setLayoutParams(params);
    }

}