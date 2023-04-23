package com.example.bubble.mainMenu;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
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

import com.example.bubble.R;
import com.example.bubble.databinding.FragmentFriendsBinding;

public class FriendsFragment extends Fragment {

    FragmentFriendsBinding binding;
    FriendsFragmentViewModel viewModel;

    String friend = "Друзья";
    String incoming = "Входящие запросы";
    String outgoing = "Исходящие запросы";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentFriendsBinding.inflate(inflater,container, false);
        Log.d("Functions", "onCreateView");

        viewModel = new ViewModelProvider(this).get(FriendsFragmentViewModel.class);

        binding.friendsTextView.setText(friend+" (0)");
        binding.incomingRequestsTextView.setText(incoming+" (0)");
        binding.outgoingRequestsTextView.setText(outgoing+" (0)");

        viewModel.createAdapters(this);

        viewModel.friendsAdapter.observe(getViewLifecycleOwner(), peopleListRecyclerView -> {
            if (peopleListRecyclerView!=null) {
                binding.friendsRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false));
                binding.friendsRecyclerView.setAdapter(peopleListRecyclerView);
                binding.friendsTextView.setText(friend + " (" + peopleListRecyclerView.getItemCount() + ")");
            }
        });

        viewModel.incomingAdapter.observe(getViewLifecycleOwner(), peopleListRecyclerView -> {
            if (peopleListRecyclerView!=null) {
                binding.incomingRequestsRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false));
                binding.incomingRequestsRecyclerView.setAdapter(peopleListRecyclerView);
                binding.incomingRequestsTextView.setText(incoming + " (" + peopleListRecyclerView.getItemCount() + ")");
            }
        });

        viewModel.outcomingAdapter.observe(getViewLifecycleOwner(), peopleListRecyclerView -> {
            if (peopleListRecyclerView!=null) {
                binding.outgoingRequestsRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false));
                binding.outgoingRequestsRecyclerView.setAdapter(peopleListRecyclerView);
                binding.outgoingRequestsTextView.setText(outgoing + " (" + peopleListRecyclerView.getItemCount() + ")");
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

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("Functions", "onCreate");
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d("Functions", "onViewCreated");
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d("Functions", "onStart");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d("Functions", "onPause");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("Functions", "onResume");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d("Functions", "onDestroyView");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("Functions", "onDestroy");
    }
}