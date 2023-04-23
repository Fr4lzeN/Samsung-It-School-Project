package com.example.bubble.mainMenu;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.bubble.R;
import com.example.bubble.databinding.FragmentFriendsBinding;

public class FriendsFragment extends Fragment {

    FragmentFriendsBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentFriendsBinding.inflate(inflater,container, false);

        binding.friendsTextView.setOnClickListener(v -> {
            binding.friendsImageSwitcher.showNext();
            if (binding.friendsRecyclerView.getVisibility()==View.GONE){
                binding.friendsRecyclerView.setVisibility(View.VISIBLE);
            }
            else{
                binding.friendsRecyclerView.setVisibility(View.GONE);
            }
        });

        binding.incomingRequestsTextView.setOnClickListener(v -> {
            binding.incomingRequestsImageSwitcher.showNext();
            if (binding.incomingRequestsRecyclerView.getVisibility()==View.GONE){
                binding.incomingRequestsRecyclerView.setVisibility(View.VISIBLE);
            }
            else{
                binding.incomingRequestsRecyclerView.setVisibility(View.GONE);
            }
        });

        binding.outgoingRequestsTextView.setOnClickListener(v -> {
            binding.outgoingRequestsImageSwitcher.showNext();
            if (binding.outgoingRequestsRecyclerView.getVisibility()==View.GONE){
                binding.outgoingRequestsRecyclerView.setVisibility(View.VISIBLE);
            }
            else{
                binding.outgoingRequestsRecyclerView.setVisibility(View.GONE);
            }
        });

        return binding.getRoot();
    }
}