package com.example.bubble.UI.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.bubble.R;
import com.example.bubble.UI.adapter.PeopleListRecyclerView;
import com.example.bubble.UI.viewModels.MainActivityViewModel;
import com.example.bubble.UI.viewModels.PeopleByHobbyFragmentViewModel;
import com.example.bubble.databinding.FragmentPeopleByHobbyBinding;


public class PeopleByHobbyFragment extends Fragment {

   FragmentPeopleByHobbyBinding binding;
   PeopleByHobbyFragmentViewModel viewModel;
   MainActivityViewModel mViewModel;
   String hobby;

    public PeopleByHobbyFragment(String hobby) {
        this.hobby = hobby;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentPeopleByHobbyBinding.inflate(inflater,container, false);
        viewModel = new ViewModelProvider(this).get(PeopleByHobbyFragmentViewModel.class);
        mViewModel = new ViewModelProvider(requireActivity()).get(MainActivityViewModel.class);
        binding.textView.setText(hobby);
        viewModel.setHobby(hobby);
        if (!viewModel.isDisposalesExist()) {
            viewModel.getUsers();
        }
        viewModel.data.observe(getViewLifecycleOwner(), friendInfoArrayList -> {
            PeopleListRecyclerView adapter = new PeopleListRecyclerView(friendInfoArrayList, uid -> {
                if (uid.equals(mViewModel.getUid()))
                    replaceFragment(new MyProfileFragment());
                else
                    replaceFragment(new UserProfileFragment(uid));
            });
            binding.recyclerView.setLayoutManager(new LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false));
            binding.recyclerView.setAdapter(adapter);
        });
        return binding.getRoot();
    }

    public void replaceFragment(Fragment next) {
        FragmentManager fragmentManager = getParentFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.nav_host_fragment, next).addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        viewModel.disable();
    }
}