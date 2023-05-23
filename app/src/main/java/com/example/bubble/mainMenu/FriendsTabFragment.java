package com.example.bubble.mainMenu;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.bubble.JSON.FriendInfo;
import com.example.bubble.R;
import com.example.bubble.databinding.FragmentFriendsTabBinding;

import java.util.List;

public class FriendsTabFragment extends Fragment {

    FragmentFriendsTabBinding binding;
    MainActivityViewModel viewModel;
    FriendStatusEnum friends;

    public FriendsTabFragment(FriendStatusEnum friends) {
        this.friends = friends;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentFriendsTabBinding.inflate(inflater,container,false);
        viewModel = new ViewModelProvider(requireActivity()).get(MainActivityViewModel.class);
        switch (friends){
            case FRIENDS:
                viewModel.friends.observe(getViewLifecycleOwner(), friendInfos -> {

                });
                break;
            case INCOMING_REQUEST:
                viewModel.incomingRequests.observe(getViewLifecycleOwner(), friendInfos -> {

                });
                break;
            case OUTGOING_REQUEST:
                viewModel.outgoingRequests.observe(getViewLifecycleOwner(), friendInfos -> {

                });
                break;
        }
        return binding.getRoot();
    }
}