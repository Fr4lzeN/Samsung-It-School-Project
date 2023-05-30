package com.example.bubble.UI.fragments;

import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.example.bubble.UI.adapter.GroupMembersAdapter;
import com.example.bubble.data.JSONModels.FriendInfo;
import com.example.bubble.R;
import com.example.bubble.data.firebase.FirebaseActions;
import com.example.bubble.databinding.DialogFragmentGroupChatBinding;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class GroupChatDialogFragment extends Fragment {

    DialogFragmentGroupChatBinding binding;
    ArrayList<FriendInfo> users;
    Uri picture;
    String chatName;
    String chatId;

    public GroupChatDialogFragment(ArrayList<FriendInfo> users, Uri picture, String chatName, String chatId) {
        this.users = users;
        this.picture = picture;
        this.chatName = chatName;
        this.chatId = chatId;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DialogFragmentGroupChatBinding.inflate(inflater,container, false);
        binding.chatName.setText(chatName);
        binding.membersCount.setText(users.size()+" участников");
        Glide.with(requireContext()).load(picture).into(binding.chatImage);
        GroupMembersAdapter adapter = new GroupMembersAdapter(users, uid -> {
            if (uid.equals(FirebaseAuth.getInstance().getUid())){
                replaceFragment(new MyProfileFragment());
            }
            else{
                replaceFragment(new UserProfileFragment(uid));
            }
        });
        binding.membersRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false));
        binding.membersRecyclerView.setAdapter(adapter);

        binding.leaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseActions.leaveFromGroupChat(chatId, FirebaseAuth.getInstance().getUid());
                fragmentBack();
            }
        });

        return binding.getRoot();
    }

    private void fragmentBack() {
        FragmentManager fm = getParentFragmentManager();
        for (int i=0; i<2; i++){
            fm.popBackStack();
        }
    }


    public void replaceFragment(Fragment fragment){
        FragmentManager fm = getParentFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.nav_host_fragment, fragment).addToBackStack(null);
        ft.commit();
    }

}