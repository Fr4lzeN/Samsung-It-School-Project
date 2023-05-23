package com.example.bubble.mainMenu;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.bubble.LoadingFragment;
import com.example.bubble.R;
import com.example.bubble.databinding.FragmentSearchBinding;
import com.example.bubble.registration.MyTextWatcher;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class SearchFragment extends Fragment {

    FragmentSearchBinding binding;
    SearchHobbiesRecyclerView adapter;
    MainActivityViewModel viewModel;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSearchBinding.inflate(inflater,container,false);
        viewModel = new ViewModelProvider(requireActivity()).get(MainActivityViewModel.class);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        MainActivity.getViewModel().hobbyList.observe(getViewLifecycleOwner(), hobby -> {
            adapter = new SearchHobbiesRecyclerView(hobby,this::replaceFragment);
            binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
            binding.recyclerView.setAdapter(adapter);
        });
        viewModel.searchText.observe(getViewLifecycleOwner(), s -> {
            if (adapter!=null){
                adapter.getFilter().filter(s);
            }
        });

        binding.search.addTextChangedListener(new MyTextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                    viewModel.setSearchText(s.toString());
            }
        });

    }

    private void replaceFragment(String hobby) {
        FragmentManager fragmentManager = getParentFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.nav_host_fragment, new PeopleByHobbyFragment(hobby)).addToBackStack(null);
        fragmentTransaction.commit();
    }
}