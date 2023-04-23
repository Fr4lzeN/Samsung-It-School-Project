package com.example.bubble.mainMenu;

import static android.app.Activity.RESULT_OK;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.media.Session2Command;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.bubble.R;
import com.example.bubble.databinding.FragmentEditPicturesDialogBinding;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class EditPicturesFragmentDialog extends BottomSheetDialogFragment {

    public interface DismissListener{
        void dismiss();
    }



   FragmentEditPicturesDialogBinding binding;
    DismissListener listener;
    static EditPicturesFragmentDialogViewModel viewModel;
    BottomSheetDialog dialog;
    BottomSheetBehavior<View> bottomSheetBehavior;


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        dialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        return dialog;
    }

    public EditPicturesFragmentDialog(DismissListener listener) {
        this.listener = listener;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this).get(EditPicturesFragmentDialogViewModel.class);
    }

    public static EditPicturesFragmentDialogViewModel getViewModel() {
        return viewModel;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentEditPicturesDialogBinding.inflate(inflater, container, false);
        viewModel.createAdapterData(requireActivity());
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        binding.checkImage.setOnClickListener(v -> {
            if (viewModel.getAdapter().getData().size()>1) {
                viewModel.uploadData();
                binding.progressBar.setVisibility(View.VISIBLE);
                binding.layout.setVisibility(View.GONE);
            }
            else{
                Toast.makeText(requireContext(), "Необходима хотя бы 1 фотография", Toast.LENGTH_SHORT).show();
            }
        });
        viewModel.downloadResult.observe(getViewLifecycleOwner(), aBoolean -> {
            binding.recyclerView.setAdapter(viewModel.getAdapter());
            binding.progressBar.setVisibility(View.GONE);
            binding.layout.setVisibility(View.VISIBLE);
        });
        binding.closeImage.setOnClickListener(v -> dismiss());
        viewModel.taskResult.observe(getViewLifecycleOwner(), aBoolean -> dismiss());
        // Inflate the layout for this fragment
        return binding.getRoot();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("Picture", "d");
        if (data!=null && resultCode==RESULT_OK)
            viewModel.onResult(requestCode, data.getData());
    }
}