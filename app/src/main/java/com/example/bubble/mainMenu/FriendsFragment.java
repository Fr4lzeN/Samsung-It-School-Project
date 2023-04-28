package com.example.bubble.mainMenu;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.bubble.JSON.FriendInfo;
import com.example.bubble.R;
import com.example.bubble.databinding.FragmentFriendsBinding;

import java.util.List;

public class FriendsFragment extends Fragment {

    FragmentFriendsBinding binding;


    String friend = "Друзья";
    String incoming = "Входящие запросы";
    String outgoing = "Исходящие запросы";
    Boolean firstOpened;

    public FriendsFragment() {
        firstOpened=true;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentFriendsBinding.inflate(inflater,container, false);
        Log.d("Functions", "onCreateView");


        binding.friendsTextView.setText(friend+" (0)");
        binding.incomingRequestsTextView.setText(incoming+" (0)");
        binding.outgoingRequestsTextView.setText(outgoing+" (0)");


        MainActivity.getViewModel().friends.observe(getViewLifecycleOwner(), friendInfos -> {
            if (friendInfos!=null) {
                PeopleListRecyclerView adapter = new PeopleListRecyclerView(friendInfos, uid ->
                        replaceFragment(new UserProfileFragment(uid)));
                binding.friendsRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false));
                binding.friendsRecyclerView.setAdapter(adapter);
                binding.friendsTextView.setText(friend+" ("+friendInfos.size()+")");
            }
        });

        MainActivity.getViewModel().incomingRequests.observe(getViewLifecycleOwner(), friendInfos -> {
            if (friendInfos!=null) {
                PeopleListRecyclerView adapter = new PeopleListRecyclerView(friendInfos, uid ->
                        replaceFragment(new UserProfileFragment(uid)));
                binding.incomingRequestsRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false));
                binding.incomingRequestsRecyclerView.setAdapter(adapter);
                binding.incomingRequestsTextView.setText(incoming+" ("+friendInfos.size()+")");
            }
        });

        MainActivity.getViewModel().outgoingRequests.observe(getViewLifecycleOwner(), friendInfos -> {
            if (friendInfos!=null) {
                PeopleListRecyclerView adapter = new PeopleListRecyclerView(friendInfos, uid ->
                        replaceFragment(new UserProfileFragment(uid)));
                binding.outgoingRequestsRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false));
                binding.outgoingRequestsRecyclerView.setAdapter(adapter);
                binding.outgoingRequestsTextView.setText(outgoing+" ("+friendInfos.size()+")");
            }
        });

        MainActivity.getViewModel().friendRecyclerIsClicked.observe(getViewLifecycleOwner(), clicked -> {
            if (clicked){
                binding.friendsTextView.callOnClick();
            }
        });
        MainActivity.getViewModel().incomingRecyclerIsClicked.observe(getViewLifecycleOwner(), clicked -> {
            if (clicked){
                binding.incomingRequestsTextView.callOnClick();
            }
        });
        MainActivity.getViewModel().outgoingRecyclerIsClicked.observe(getViewLifecycleOwner(), clicked -> {
            if (clicked){
                binding.outgoingRequestsTextView.callOnClick();
            }
        });

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

    private void replaceFragment(Fragment next) {
        FragmentManager fragmentManager = getParentFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.nav_host_fragment, next).addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Boolean friendState = binding.friendsRecyclerView.getVisibility()==View.VISIBLE;
        Boolean incomingState = binding.incomingRequestsRecyclerView.getVisibility()==View.VISIBLE;
        Boolean outgoingState = binding.outgoingRequestsRecyclerView.getVisibility()==View.VISIBLE;
        MainActivity.viewModel.setRecyclerState(friendState,incomingState,outgoingState);
    }
}