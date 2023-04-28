package com.example.bubble.mainMenu;

import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.bubble.JSON.UserInfoJSON;
import com.example.bubble.R;
import com.example.bubble.databinding.FragmentMessageBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;

public class MessageFragment extends Fragment {

    FragmentMessageBinding binding;
    String uid;
    UserInfoJSON userData;
    String messageId;
    Boolean checkIfChatExists = false;
    Boolean firstOpen = true;
    MessageFragmentViewModel viewModel;
    Integer itemCount = 0;

    View.OnClickListener openProfile = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            openProfileFragment(uid);
        }
    };

    public MessageFragment(String uid, UserInfoJSON userData, String messageId) {
        this.uid = uid;
        this.userData = userData;
        this.messageId = messageId;
    }

    public MessageFragment(String uid, UserInfoJSON userData){
        this.uid = uid;
        this.userData=userData;
        checkIfChatExists=true;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentMessageBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(this).get(MessageFragmentViewModel.class);
        viewModel.setUserData(userData);
        viewModel.downloadPicture(uid);
        if (checkIfChatExists){
            viewModel.getMessageId(uid);
        }else {
            viewModel.setMessageId(messageId);
        }
        viewModel.messageId.observe(getViewLifecycleOwner(), s -> {
            if (s!=null){
                viewModel.createAdapter(this);
                messageId=s;
            }
        });
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
        viewModel.userData.observe(getViewLifecycleOwner(), userInfoJSON -> {
            binding.name.setText(userInfoJSON.name);
        });
        viewModel.picture.observe(getViewLifecycleOwner(), uri -> {
            Glide.with(requireContext()).load(uri).into(binding.image);
        });
        binding.sendMessageImage.setOnClickListener(v -> {
            if (!TextUtils.isEmpty(binding.messageEditText.getText().toString())) {
                if (messageId != null) {
                    viewModel.sendMessage(binding.messageEditText.getText().toString());
                } else {
                    viewModel.createNewMessage(uid, binding.messageEditText.getText().toString());
                }
                binding.messageEditText.setText("");
            }
        });

        binding.name.setOnClickListener(openProfile);
        binding.image.setOnClickListener(openProfile);

        return binding.getRoot();
    }

    private void openProfileFragment(String uid) {
        firstOpen=true;
        FragmentManager fm = getParentFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.nav_host_fragment, new UserProfileFragment(uid)).addToBackStack(null);
        ft.commit();
    }

}