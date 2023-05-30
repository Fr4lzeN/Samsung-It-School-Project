package com.example.bubble.UI.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.bubble.databinding.FragmentFriendsTabBinding;
import com.example.bubble.UI.viewModels.MainActivityViewModel;
import com.example.bubble.tools.ENUMs.FriendStatusEnum;

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