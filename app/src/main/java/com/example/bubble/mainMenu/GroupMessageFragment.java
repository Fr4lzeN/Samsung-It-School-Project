package com.example.bubble.mainMenu;

import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.example.bubble.JSON.FriendInfo;
import com.example.bubble.R;
import com.example.bubble.databinding.FragmentGroupMessageBinding;

import java.util.ArrayList;

public class GroupMessageFragment extends Fragment implements View.OnClickListener {

    FragmentGroupMessageBinding binding;
    GroupMessageFragmentViewModel viewModel;
    String chatId;
    String chatName;
    Uri picture;
    Boolean firstOpen = true;
    Integer itemCount;

    public GroupMessageFragment(String chatId, String chatName, Uri picture) {
        this.chatId = chatId;
        this.chatName = chatName;
        this.picture = picture;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentGroupMessageBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(this).get(GroupMessageFragmentViewModel.class);
        viewModel.setOptions(chatName,chatId,picture);
        viewModel.createAdapter(this);
        viewModel.adapter.observe(getViewLifecycleOwner(), adapter -> {
            binding.recyclerView.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false));
            binding.recyclerView.setAdapter(adapter);
            binding.recyclerView.scrollToPosition(adapter.getItemCount()-1);
        });
        viewModel.itemCount.observe(getViewLifecycleOwner(), integer -> {
            itemCount=integer;
        });


        viewModel.allItemCount.observe(getViewLifecycleOwner(), integer -> {
            if (firstOpen || integer - itemCount <=8){
                firstOpen= false;
                binding.recyclerView.scrollToPosition(integer-1);
            }
        });

        binding.name.setText(chatName);
        Glide.with(requireContext()).load(picture).into(binding.image);
        binding.image.setOnClickListener(this);
        binding.name.setOnClickListener(this);
        binding.backArrow.setOnClickListener(v -> getParentFragmentManager().popBackStack());
        binding.messageLayout.setEndIconOnClickListener(v -> {
            if (!TextUtils.isEmpty(binding.messageEditText.getText().toString())){
                FirebaseActions.sendGroupMessage(binding.messageEditText.getText().toString(), chatId);
                binding.messageEditText.setText("");
            }
        });
        return binding.getRoot();
    }

    @Override
    public void onClick(View v) {
        replaceFragment(viewModel.getUsers(), chatName, picture);
    }

    private void replaceFragment(ArrayList<FriendInfo> users, String chatName, Uri picture) {
        FragmentManager fm = getParentFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.nav_host_fragment, new GroupChatDialogFragment(users,picture,chatName, chatId)).addToBackStack(null);
        ft.commit();
    }
}