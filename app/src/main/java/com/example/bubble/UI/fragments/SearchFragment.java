package com.example.bubble.UI.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.bubble.R;
import com.example.bubble.UI.adapter.SearchHobbiesAdapter;
import com.example.bubble.UI.viewModels.MainActivityViewModel;
import com.example.bubble.databinding.FragmentSearchBinding;
import com.example.bubble.tools.textWatchers.MyTextWatcher;

public class SearchFragment extends Fragment {

    FragmentSearchBinding binding;
    SearchHobbiesAdapter adapter;
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
        viewModel.hobbyList.observe(getViewLifecycleOwner(), hobby -> {
            adapter = new SearchHobbiesAdapter(hobby,this::replaceFragment);
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