package com.example.bubble.UI.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.bubble.UI.adapter.GroupMessageListAdapter;
import com.example.bubble.UI.adapter.MessageListAdapter;
import com.example.bubble.data.JSONModels.GroupMessageListItem;
import com.example.bubble.data.JSONModels.MessageListItem;
import com.example.bubble.R;
import com.example.bubble.UI.viewModels.MainActivityViewModel;
import com.example.bubble.databinding.FragmentMessagesBinding;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

public class MessagesFragment extends Fragment {

    FragmentMessagesBinding binding;
    MainActivityViewModel viewModel;
    Observer<ArrayList<MessageListItem>> messageObserver = this::createMessageAdapter;
    Observer<ArrayList<GroupMessageListItem>> groupMessageObserver = this::createGroupMessageAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentMessagesBinding.inflate(inflater,container, false);
        viewModel = new ViewModelProvider(requireActivity()).get(MainActivityViewModel.class);

        viewModel.groupChatSelected.observe(getViewLifecycleOwner(), aBoolean -> {
            if (aBoolean){
                binding.tabLayout.selectTab(binding.tabLayout.getTabAt(1));
            }else{
                binding.tabLayout.selectTab(binding.tabLayout.getTabAt(0));
            }
        });
        binding.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition()==0){
                    viewModel.allGroupMessagesData.removeObservers(getViewLifecycleOwner());
                    viewModel.allMessagesData.observe(getViewLifecycleOwner(), messageObserver);
                    viewModel.setGroupChatSelected(false);
                }else{
                    viewModel.allMessagesData.removeObservers(getViewLifecycleOwner());
                    viewModel.allGroupMessagesData.observe(getViewLifecycleOwner(), groupMessageObserver);
                    viewModel.setGroupChatSelected(true);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        binding.toolBar.setNavigationOnClickListener(v -> replaceFragment(new CreateGroupChatFragment(viewModel.friends.getValue())));
        viewModel.allMessagesData.observe(getViewLifecycleOwner(), messageObserver);
        return binding.getRoot();
    }

    private void replaceFragment(Fragment next) {
        FragmentManager fragmentManager = getParentFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.nav_host_fragment, next).addToBackStack(null);
        fragmentTransaction.commit();
    }

    private void createMessageAdapter(ArrayList<MessageListItem> messageListItems){
        MessageListAdapter adapter = new MessageListAdapter(messageListItems, (userInfo, messageId, uid) ->
                replaceFragment(new MessageFragment(uid, userInfo, messageId)));
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false));
        binding.recyclerView.setAdapter(adapter);
    }

    private void createGroupMessageAdapter(ArrayList<GroupMessageListItem> groupMessageListItems){
        GroupMessageListAdapter adapter = new GroupMessageListAdapter(groupMessageListItems, (chatId, chatName, picture) -> {
            replaceFragment(new GroupMessageFragment(chatId,chatName,picture));
        });
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false));
        binding.recyclerView.setAdapter(adapter);
    }
}