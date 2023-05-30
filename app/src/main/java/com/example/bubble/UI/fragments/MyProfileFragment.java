package com.example.bubble.UI.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.bubble.UI.adapter.ProfilePictureAdapter;
import com.example.bubble.R;
import com.example.bubble.UI.viewModels.ProfileFragmentViewModel;
import com.example.bubble.databinding.FragmentMyProfileBinding;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;

public class MyProfileFragment extends Fragment {
    String uid;

    FragmentMyProfileBinding binding;
    ProfilePictureAdapter adapter;
    ProfileFragmentViewModel viewModel;

    public MyProfileFragment() {
       this.uid = FirebaseAuth.getInstance().getUid();
    }


    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT) {
        @Override
        public float getSwipeEscapeVelocity(float defaultValue) {
            return super.getSwipeEscapeVelocity(defaultValue);
        }

        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public int getSwipeDirs(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
            int position = viewHolder.getAdapterPosition();
            if (position==0){
                return ItemTouchHelper.LEFT;
            }
            if (position==adapter.getItemCount()-1){
                return ItemTouchHelper.RIGHT;
            }
            return super.getSwipeDirs(recyclerView, viewHolder);
        };

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            int position = viewHolder.getAdapterPosition();
            Log.d("Swipe", "swipe");
            switch (direction){
                case ItemTouchHelper.RIGHT:
                    binding.recyclerView.scrollToPosition(position-1);
                    binding.selectedPicture.getChildAt(position).setEnabled(false);
                    binding.selectedPicture.getChildAt(position-1).setEnabled(true);
                    binding.selectedPicture.getChildAt(position-1).performClick();
                    break;
                case ItemTouchHelper.LEFT:
                    binding.recyclerView.scrollToPosition(position+1);
                    binding.selectedPicture.getChildAt(position).setEnabled(false);
                    binding.selectedPicture.getChildAt(position+1).setEnabled(true);
                    binding.selectedPicture.getChildAt(position+1).performClick();
                    break;
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentMyProfileBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(this).get(ProfileFragmentViewModel.class);
        viewModel.setUid(uid);

        viewModel.downloadPictures();
        viewModel.downloadUserData();
        viewModel.getHobbies();

        viewModel.data.observe(getViewLifecycleOwner(), storageReferences -> {
            adapter = new ProfilePictureAdapter(storageReferences);
            binding.selectedPicture.getChildAt(0).performClick();
            for (int i=1; i<storageReferences.size(); i++){
                binding.selectedPicture.getChildAt(i).setVisibility(View.VISIBLE);
            }
            binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false){
                @Override
                public boolean canScrollHorizontally() {
                    return false;
                }
            });
            binding.recyclerView.setAdapter(adapter);
            if (storageReferences.size()>1){
                ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
                itemTouchHelper.attachToRecyclerView(binding.recyclerView);
            }
        });

        viewModel.userInfo.observe(getViewLifecycleOwner(), user -> {
            binding.name.setText(user.name);
            replaceFragment(0);
        });

        binding.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                replaceFragment(tab.parent.getSelectedTabPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        return binding.getRoot();
    }

    private void replaceFragment(int selectedTabPosition) {
        FragmentManager fm = getChildFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        switch (selectedTabPosition){
            case 0:
                ft.replace(R.id.nav_host_fragment, new InformationTabFragment());
                break;
            case 1:
                ft.replace(R.id.nav_host_fragment, new HobbyTabFragment());
                break;
        }
        ft.commit();

    }


}