package com.example.bubble.mainMenu;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.bubble.JSON.MessageListItem;
import com.example.bubble.R;
import com.example.bubble.databinding.FragmentMessagesBinding;

import java.util.ArrayList;

public class MessagesFragment extends Fragment {

    FragmentMessagesBinding binding;
    MainActivityViewModel viewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentMessagesBinding.inflate(inflater,container, false);
        viewModel = new ViewModelProvider(requireActivity()).get(MainActivityViewModel.class);
        MainActivity.getViewModel().allMessagesData.observe(getViewLifecycleOwner(), messageListItems -> {
            MessageListAdapter adapter = new MessageListAdapter(messageListItems, (userInfo, messageId, uid) ->
                    replaceFragment(new MessageFragment(uid, userInfo, messageId)));
            binding.recyclerView.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false));
            binding.recyclerView.setAdapter(adapter);
        });

        binding.toolBar.setNavigationOnClickListener(v -> replaceFragment(new CreateGroupChatFragment(viewModel.friends.getValue())));

        return binding.getRoot();
    }

    private void replaceFragment(Fragment next) {
        FragmentManager fragmentManager = getParentFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.nav_host_fragment, next).addToBackStack(null);
        fragmentTransaction.commit();
    }
}