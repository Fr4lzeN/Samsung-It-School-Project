package com.example.bubble.UI.fragments;

import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.text.Editable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.example.bubble.UI.adapter.GroupChatAdapter;
import com.example.bubble.UI.viewModels.CreateGroupChatViewModel;
import com.example.bubble.data.JSONModels.FriendInfo;
import com.example.bubble.databinding.FragmentCreateGroupChatBinding;
import com.example.bubble.tools.textWatchers.MyTextWatcher;

import java.util.List;

public class CreateGroupChatFragment extends Fragment {

    FragmentCreateGroupChatBinding binding;
    List<FriendInfo> friendList;
    CreateGroupChatViewModel viewModel;
    ActivityResultLauncher<PickVisualMediaRequest> pickMedia;
    GroupChatAdapter adapter;

    public CreateGroupChatFragment(List<FriendInfo> friendList) {
        this.friendList = friendList;

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pickMedia = registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), uri -> {
            // Callback is invoked after the user selects a media item or closes the
            // photo picker.
            if (uri != null) {
                Log.d("PhotoPicker", "Selected URI: " + uri);
                viewModel.setUri(uri);
            } else {
                Log.d("PhotoPicker", "No media selected");
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentCreateGroupChatBinding.inflate(inflater,container,false);
        viewModel = new ViewModelProvider(this).get(CreateGroupChatViewModel.class);
        viewModel.setFriends(friendList);
        Log.d("Chat", "started");
        viewModel.friendList.observe(getViewLifecycleOwner(), friendInfos -> {
            if (friendInfos!=null) {
                adapter = new GroupChatAdapter(friendInfos, uid -> viewModel.addUid(uid));
                binding.recyclerView.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false));
                binding.recyclerView.setAdapter(adapter);
            }
        });

        binding.toolBar.setNavigationOnClickListener(v -> goBack());

        binding.image.setOnClickListener(v -> {
            Log.d("Chat", "image");
            loadImage();
        });

        viewModel.picture.observe(getViewLifecycleOwner(), uri -> {
            Glide.with(binding.getRoot()).load(uri).into(binding.image);
            if (!TextUtils.isEmpty(viewModel.chatName.getValue()) && viewModel.selectedPeople.getValue() !=null && viewModel.selectedPeople.getValue().size()>0 && viewModel.chatName.getValue()!=null && !TextUtils.isEmpty(viewModel.chatName.getValue())&& uri!=null){
                binding.finish.setEnabled(true);
            }else{
                binding.finish.setEnabled(false);
            }
        });

        viewModel.chatName.observe(getViewLifecycleOwner(), s -> {
            if (viewModel.picture.getValue()!=null && !TextUtils.isEmpty(viewModel.chatName.getValue()) && viewModel.selectedPeople.getValue() !=null && viewModel.selectedPeople.getValue().size()>0 && viewModel.chatName.getValue()!=null && !TextUtils.isEmpty(viewModel.chatName.getValue())){
                binding.finish.setEnabled(true);
            }else{
                binding.finish.setEnabled(false);
            }
        });

        viewModel.selectedPeople.observe(getViewLifecycleOwner(), strings -> {
            if (viewModel.picture.getValue()!=null && !TextUtils.isEmpty(viewModel.chatName.getValue()) && viewModel.selectedPeople.getValue() !=null && viewModel.selectedPeople.getValue().size()>0 && viewModel.chatName.getValue()!=null && !TextUtils.isEmpty(viewModel.chatName.getValue())){
                binding.finish.setEnabled(true);
            }else{
                binding.finish.setEnabled(false);
            }
        });

        binding.messageEditText.addTextChangedListener(new MyTextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                viewModel.setName(s.toString());
            }
        });

        binding.finish.setOnClickListener(v -> {
            Log.d("Chat", "clicked");
            viewModel.createChat(binding.messageEditText.getText().toString());
            goBack();
        });
        return binding.getRoot();
    }

    private void goBack() {
        FragmentManager fm = getParentFragmentManager();
        fm.popBackStack();
    }

    private void loadImage() {
        pickMedia.launch(new PickVisualMediaRequest.Builder()
                .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                .build());
    }
}