package com.example.bubble.mainMenu;

import static android.app.Activity.RESULT_OK;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.media.Session2Command;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
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
import com.example.bubble.registration.FillDataViewModel;
import com.example.bubble.registration.RecyclerPictureBottomDialogFragment;
import com.example.bubble.registration.TakePictureEnum;
import com.example.bubble.registration.TakePictureRecyclerView;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class EditPicturesFragmentDialog extends BottomSheetDialogFragment {

    public interface DismissListener{
        void dismiss();
    }



   FragmentEditPicturesDialogBinding binding;
    DismissListener listener;
    EditPicturesFragmentDialogViewModel viewModel;
    BottomSheetDialog dialog;
    BottomSheetBehavior<View> bottomSheetBehavior;

    TakePictureRecyclerView adapter;


    ActivityResultLauncher<PickVisualMediaRequest> pickMultipleMedia;
    ActivityResultLauncher<PickVisualMediaRequest> pickMedia;
    int position;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pickMultipleMedia = registerForActivityResult(new ActivityResultContracts.PickMultipleVisualMedia(5), uris -> {
            if (!uris.isEmpty()) {
                viewModel.addPictureToAdapter(uris);
                Log.d("PhotoPicker", "Number of items selected: " + uris.size());
            } else {
                Log.d("PhotoPicker", "No media selected");
            }
        });

        pickMedia = registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), uri -> {
            if (uri != null) {
                Log.d("PhotoPicker", "Selected URI: " + uri);
                viewModel.changeAdapterPicture(uri,position);
            } else {
                Log.d("PhotoPicker", "No media selected");
            }
        });

        viewModel = new ViewModelProvider(this).get(EditPicturesFragmentDialogViewModel.class);

    }

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentEditPicturesDialogBinding.inflate(inflater, container, false);
        viewModel.createModel();
        viewModel.downloadData();
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        binding.nextButton.setOnClickListener(v -> {
            if (adapter.getData().size()>=1) {
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
        viewModel.data.observe(getViewLifecycleOwner(), uris -> {

            binding.addPicture.setEnabled(uris.size() < 5);
            binding.nextButton.setEnabled(uris.size() >= 1);

            if (binding.recyclerView.getAdapter()==null) {
                adapter = new TakePictureRecyclerView(uris, (data, position) -> {

                    new MaterialAlertDialogBuilder(requireContext())
                            .setTitle("Изменение фото")
                            .setMessage("Выберите, что хотите сделать с выбранной фотографией")
                            .setPositiveButton("Изменить фотографию", ((dialog, which) -> {
                                pickPicture(position);
                            }))
                            .setNegativeButton("Удалить фотографию", (dialog, which) -> {
                                viewModel.deleteAdapterPicture(position);
                            })
                            .show();

                });
                binding.recyclerView.setAdapter(adapter);
            }else{
                adapter.setData(uris);
                adapter.notifyDataSetChanged();
            }
        });

        binding.addPicture.setOnClickListener(v -> {
            pickPictures();
        });

        binding.toolBar.setNavigationOnClickListener(v -> dismiss());
        viewModel.taskResult.observe(getViewLifecycleOwner(), aBoolean -> dismiss());
        return binding.getRoot();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        bottomSheetBehavior = BottomSheetBehavior.from((View) view.getParent());
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        CoordinatorLayout layout = binding.getRoot();
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

    private void pickPicture(int position) {
        this.position=position;
        pickMedia.launch(new PickVisualMediaRequest.Builder()
                .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                .build());
    }

    private void pickPictures(){
        pickMultipleMedia.launch(new PickVisualMediaRequest.Builder()
                .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                .build());
    }
}