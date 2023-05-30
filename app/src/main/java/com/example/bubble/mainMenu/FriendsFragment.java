package com.example.bubble.mainMenu;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.bubble.JSON.FriendInfo;
import com.example.bubble.R;
import com.example.bubble.databinding.FragmentFriendsBinding;
import com.google.android.material.tabs.TabLayout;

import java.util.List;

public class FriendsFragment extends Fragment {

    FragmentFriendsBinding binding;

    Boolean firstOpened;

    MainActivityViewModel viewModel;
    PeopleListRecyclerView adapter;

    Observer<List<FriendInfo>> observer = this::observeData;


    public FriendsFragment() {
        firstOpened=true;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public View  onCreateView(LayoutInflater inflater, ViewGroup container,
                                                                                Bundle savedInstanceState) {
        binding = FragmentFriendsBinding.inflate(inflater,container, false);
        viewModel = new ViewModelProvider(requireActivity()).get(MainActivityViewModel.class);

         viewModel.incomingRequests.observe(getViewLifecycleOwner(), friendInfos -> {
            if (friendInfos!=null && friendInfos.size()!=0) {
                binding.tabLayout.getTabAt(1).getOrCreateBadge().setNumber(friendInfos.size());
            }else{
                binding.tabLayout.getTabAt(1).removeBadge();
            }
        });

        viewModel.friendStatusShowing.observe(getViewLifecycleOwner(), friendStatusEnum -> {
            switch (friendStatusEnum){
                case FRIENDS:
                    viewModel.friends.observe(getViewLifecycleOwner(), observer);
                    break;
                case OUTGOING_REQUEST:
                    if (binding.tabLayout.getTabAt(0).isSelected()){
                        binding.tabLayout.getTabAt(2).select();
                        break;
                    }
                    viewModel.outgoingRequests.observe(getViewLifecycleOwner(), observer);
                    break;
                case INCOMING_REQUEST:
                    if (binding.tabLayout.getTabAt(0).isSelected()){
                        binding.tabLayout.getTabAt(1).select();
                        break;
                    }
                    viewModel.incomingRequests.observe(getViewLifecycleOwner(), observer);
                    break;
            }
        });

        binding.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()){
                    case 0:
                        Log.d("Tabs", "friends");
                        viewModel.setShowingFriends(FriendStatusEnum.FRIENDS);
                        break;
                    case 1:
                        Log.d("Tabs", "inc");
                        viewModel.setShowingFriends(FriendStatusEnum.INCOMING_REQUEST);
                        break;
                    case 2:
                        Log.d("Tabs", "out");
                        viewModel.setShowingFriends(FriendStatusEnum.OUTGOING_REQUEST);
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                switch (tab.getPosition()){
                    case 0:
                        viewModel.friends.removeObserver(observer);
                        break;
                    case 1:
                        viewModel.incomingRequests.removeObserver(observer);
                        break;
                    case 2:
                        viewModel.outgoingRequests.removeObserver(observer);
                        break;
                }
            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        return binding.getRoot();
    }

    private void drawDog(boolean b) {
        if (b){
            binding.recyclerView.setVisibility(View.GONE);
            binding.emptyLayout.setVisibility(View.VISIBLE);
        }else{
            binding.recyclerView.setVisibility(View.VISIBLE);
            binding.emptyLayout.setVisibility(View.GONE);
        }
    }

    private void createAdapter(List<FriendInfo> friendInfos) {
        adapter = new PeopleListRecyclerView(friendInfos, uid ->
                replaceFragment(new UserProfileFragment(uid)));
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false));
        binding.recyclerView.setAdapter(adapter);
    }

    private void replaceFragment(Fragment next) {
        FragmentManager fragmentManager = getParentFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.nav_host_fragment, next).addToBackStack(null);
        fragmentTransaction.commit();
    }

    private void observeData(List<FriendInfo> friendInfos){
        if (friendInfos!=null && friendInfos.size()!=0) {
            createAdapter(friendInfos);
            drawDog(false);
        }else{
            drawDog(true);
        }
    }
}