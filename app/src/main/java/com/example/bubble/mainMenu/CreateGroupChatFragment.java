package com.example.bubble.mainMenu;

import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.example.bubble.JSON.FriendInfo;
import com.example.bubble.R;
import com.example.bubble.databinding.FragmentCreateGroupChatBinding;

import java.util.List;

public class CreateGroupChatFragment extends Fragment {

    FragmentCreateGroupChatBinding binding;
    List<FriendInfo> friendList;
    CreateGroupChatViewModel viewModel;
    ActivityResultLauncher<PickVisualMediaRequest> pickMedia;

    public CreateGroupChatFragment(List<FriendInfo> friendList) {
        this.friendList = friendList;
        pickMedia = registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), uri -> {
            // Callback is invoked after the user selects a media item or closes the
            // photo picker.
            if (uri != null) {
                Log.d("PhotoPicker", "Selected URI: " + uri);
                viewModel.setUri(uri);
                Glide.with(binding.getRoot()).load(uri).into(binding.image);
            } else {
                Log.d("PhotoPicker", "No media selected");
            }
        });
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentCreateGroupChatBinding.inflate(inflater,container,false);
        viewModel = new ViewModelProvider(this).get(CreateGroupChatViewModel.class);
        viewModel.setFriends(friendList);

        viewModel.friendList.observe(getViewLifecycleOwner(), new Observer<List<FriendInfo>>() {
            @Override
            public void onChanged(List<FriendInfo> friendInfos) {
                GroupChatAdapter adapter = new GroupChatAdapter(friendInfos, uid -> viewModel.addUid(uid));
                binding.recyclerView.setLayoutManager(new LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false));
                binding.recyclerView.setAdapter(adapter);
            }
        });

        binding.image.setOnClickListener(v -> {
            loadImage();
        });

        binding.finish.setOnClickListener(v -> viewModel.createChat(binding.messageEditText.getText().toString()));

        binding = FragmentCreateGroupChatBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    private void loadImage() {
        pickMedia.launch(new PickVisualMediaRequest.Builder()
                .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                .build());
    }
}