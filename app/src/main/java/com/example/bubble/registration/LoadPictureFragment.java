package com.example.bubble.registration;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.bubble.R;
import com.example.bubble.databinding.FragmentLoadPictureBinding;

import java.util.List;

public class LoadPictureFragment extends Fragment{

    FragmentLoadPictureBinding binding;
    FillDataViewModel viewModel;
    int position;
    int amount;

    TakePictureRecyclerView adapter;

    // Registers a photo picker activity launcher in multi-select mode.
// In this example, the app lets the user select up to 5 media files.
    ActivityResultLauncher<PickVisualMediaRequest> pickMultipleMedia;
    ActivityResultLauncher<PickVisualMediaRequest> pickMedia;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pickMultipleMedia = registerForActivityResult(new ActivityResultContracts.PickMultipleVisualMedia(5-amount), uris -> {
            // Callback is invoked after the user selects media items or closes the
            // photo picker.
            if (!uris.isEmpty()) {
                viewModel.addPictureToAdapter(uris);
                Log.d("PhotoPicker", "Number of items selected: " + uris.size());
            } else {
                Log.d("PhotoPicker", "No media selected");
            }
        });

       pickMedia = registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), uri -> {
            // Callback is invoked after the user selects a media item or closes the
            // photo picker.
            if (uri != null) {
                Log.d("PhotoPicker", "Selected URI: " + uri);
                viewModel.changeAdapterPicture(uri,position);
            } else {
                Log.d("PhotoPicker", "No media selected");
            }
        });

        viewModel = new ViewModelProvider(requireActivity()).get(FillDataViewModel.class);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding=FragmentLoadPictureBinding.inflate(inflater,container,false);

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        viewModel.data.observe(getViewLifecycleOwner(), uris -> {

            binding.addPicture.setEnabled(uris.size() < 5);
            binding.nextButton.setEnabled(uris.size() >= 1);

            if (binding.recyclerView.getAdapter()==null) {
                adapter = new TakePictureRecyclerView(uris, (data, position) -> {
                    new RecyclerPictureBottomDialogFragment(action -> {
                        if (action == TakePictureEnum.DELETE) {
                            viewModel.deleteAdapterPicture(position);
                        } else {
                            pickPicture(position);
                        }
                    }).show(getParentFragmentManager(), null);
                });
                binding.recyclerView.setAdapter(adapter);
            }else{
                adapter.setData(uris);
                adapter.notifyDataSetChanged();
            }
        });
        binding.addPicture.setOnClickListener(v -> {
            //pickPictures(binding.recyclerView.getAdapter().getItemCount());
            pickPictures(0);
        });
        binding.backButton.setOnClickListener(view -> {
                Navigation.findNavController(binding.getRoot()).popBackStack();
        });
        binding.nextButton.setOnClickListener(view -> {
            viewModel.sendData();
            Navigation.findNavController(binding.getRoot()).navigate(R.id.action_loadPictureFragment_to_loadingFragment2);
        });

        return binding.getRoot();
    }

    private void pickPicture(int position) {
        this.position=position;
        pickMedia.launch(new PickVisualMediaRequest.Builder()
                .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                .build());
    }

    private void pickPictures(int amount){
        this.amount=amount;
// For this example, launch the photo picker and let the user choose images
// and videos. If you want the user to select a specific type of media file,
// use the overloaded versions of launch(), as shown in the section about how
// to select a single media item.
        pickMultipleMedia.launch(new PickVisualMediaRequest.Builder()
                .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                .build());
    }
}