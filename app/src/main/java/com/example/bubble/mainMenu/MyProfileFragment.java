package com.example.bubble.mainMenu;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.bubble.JSON.UserInfoJSON;
import com.example.bubble.databinding.FragmentMyProfileBinding;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Calendar;

public class MyProfileFragment extends Fragment {
    String uid;

    FragmentMyProfileBinding binding;
    ProfilePictureRecyclerView adapter;
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
                    break;
                case ItemTouchHelper.LEFT:
                    binding.recyclerView.scrollToPosition(position+1);
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
            adapter = new ProfilePictureRecyclerView(storageReferences);
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

        viewModel.userInfo.observe(getViewLifecycleOwner(), this::displayInfo);
        viewModel.hobbies.observe(getViewLifecycleOwner(), strings -> {
            if (strings!=null)
            for (String i : strings){
                binding.hobbiesTextView.setText(binding.hobbiesTextView.getText().toString()+" "+i);
            }
        });
        return binding.getRoot();
    }

    public void displayInfo(UserInfoJSON user){
        Calendar today = Calendar.getInstance();
        int year = today.get(Calendar.YEAR)- user.dateOfBirth.year;
        if ((user.dateOfBirth.month>(today.get(Calendar.MONTH)+1)) ||((user.dateOfBirth.month==today.get(Calendar.MONTH) && user.dateOfBirth.day>today.get(Calendar.DAY_OF_MONTH))) ){
            year--;
        }
        binding.name.setText(user.name+", "+String.valueOf(year));
        binding.info.setText(user.info);
    }

}