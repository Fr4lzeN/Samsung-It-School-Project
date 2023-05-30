package com.example.bubble.UI.bottomDialogFragment;

import android.app.Dialog;
import android.content.res.Resources;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.widget.NestedScrollView;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;

import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.bubble.databinding.FragmentChangeHobbyDialogBinding;
import com.example.bubble.UI.viewModels.ChangeHobbyFragmentDialogViewModel;
import com.example.bubble.tools.textWatchers.MyTextWatcher;
import com.google.android.material.appbar.AppBarLayout;
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
        viewModel.hobbiesAdapter.observe(getViewLifecycleOwner(), hobbyRecyclerView -> {
                    if (hobbyRecyclerView == null) {
                        viewModel.createAdapter();
                    } else {
                        binding.recyclerView.setAdapter(hobbyRecyclerView);
                        binding.progressBar.setVisibility(View.GONE);
                        binding.layout.setVisibility(View.VISIBLE);
                    }
        });

        binding.recyclerView.setLayoutManager(new GridLayoutManager(getContext(),6, GridLayoutManager.HORIZONTAL, false));
        binding.toolBar.setNavigationOnClickListener(v -> dismiss());
        viewModel.chipChecked.observe(getViewLifecycleOwner(), aBoolean -> {
                binding.finish.setEnabled(aBoolean);
            });

        binding.search.addTextChangedListener(new MyTextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                viewModel.getAdapter().getFilter().filter(s);
            }
        });

        binding.finish.setOnClickListener(v -> {
            binding.progressBar.setVisibility(View.VISIBLE);
            binding.layout.setVisibility(View.INVISIBLE);
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
}