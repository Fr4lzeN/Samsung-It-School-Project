package com.example.bubble.mainMenu;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.bubble.JSON.UserInfoJSON;
import com.example.bubble.R;
import com.example.bubble.databinding.FragmentMyProfileBinding;
import com.example.bubble.databinding.FragmentUserProfileBinding;

import java.util.Calendar;

public class UserProfileFragment extends Fragment {

    FragmentUserProfileBinding binding;
    String uid;
    UserInfoJSON userInfo;
    ProfilePictureRecyclerView adapter;
    ProfileFragmentViewModel viewModel;

    public UserProfileFragment(String uid) {
        this.uid = uid;
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
        binding = FragmentUserProfileBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(this).get(ProfileFragmentViewModel.class);
        viewModel.setUid(uid);

        viewModel.downloadPictures();
        viewModel.downloadUserData();
        viewModel.getHobbies();
        viewModel.checkFriendStatus();

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
        viewModel.friendStatus.observe(getViewLifecycleOwner(), friendStatus -> {
            if (friendStatus==null || friendStatus == FriendStatus.NO_STATUS){
                binding.addFriendButton.setText("Добавить в друзья");
            }else {
                switch (friendStatus) {
                    case FRIENDS:
                        binding.addFriendButton.setText("Удалить из друзей");
                        break;
                    case INCOMING_REQUEST:
                        binding.addFriendButton.setText("Принять запрос в друзья");
                        break;
                    case OUTGOING_REQUEST:
                        binding.addFriendButton.setText("Отменить заявку");
                        break;
                }
            }
        });

        binding.addFriendButton.setOnClickListener(v -> {
            viewModel.addFriendButton(requireContext());
        });

        binding.messageButton.setOnClickListener(v -> {
            replaceFragment();
        });

        return binding.getRoot();
    }



    public void displayInfo(UserInfoJSON user){
        userInfo = user;
        Calendar today = Calendar.getInstance();
        int year = today.get(Calendar.YEAR)- user.dateOfBirth.year;
        if ((user.dateOfBirth.month>(today.get(Calendar.MONTH)+1)) ||((user.dateOfBirth.month==today.get(Calendar.MONTH) && user.dateOfBirth.day>today.get(Calendar.DAY_OF_MONTH))) ){
            year--;
        }
        binding.name.setText(user.name+", "+String.valueOf(year));
        binding.info.setText(user.info);
    }

    private void replaceFragment() {
            FragmentManager fragmentManager = getParentFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.nav_host_fragment, new MessageFragment(uid,userInfo)).addToBackStack(null);
            fragmentTransaction.commit();
    }

}
