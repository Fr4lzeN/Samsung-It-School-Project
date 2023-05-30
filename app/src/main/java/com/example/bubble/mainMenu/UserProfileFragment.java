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

import com.example.bubble.HobbyTabFragment;
import com.example.bubble.InformationTabFragment;
import com.example.bubble.JSON.UserInfoJSON;
import com.example.bubble.R;
import com.example.bubble.databinding.FragmentMyProfileBinding;
import com.example.bubble.databinding.FragmentUserProfileBinding;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Calendar;

public class UserProfileFragment extends Fragment {

    String uid;

    FragmentUserProfileBinding binding;
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
        binding = FragmentUserProfileBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(this).get(ProfileFragmentViewModel.class);
        viewModel.setUid(uid);

        viewModel.downloadPictures();
        viewModel.downloadUserData();
        viewModel.getHobbies();
        viewModel.getFriendState();

        viewModel.privacy.observe(getViewLifecycleOwner(), aBoolean -> binding.sendMessage.setEnabled(!aBoolean));

        viewModel.data.observe(getViewLifecycleOwner(), storageReferences -> {
            adapter = new ProfilePictureRecyclerView(storageReferences);
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

        viewModel.friendState.observe(getViewLifecycleOwner(), friendStatusEnum -> {
            if (friendStatusEnum!=null) {
                switch (friendStatusEnum) {
                    case FRIENDS:
                        binding.addFriend.setText("Удалить из друзей");
                        break;
                    case INCOMING_REQUEST:
                        binding.addFriend.setText("Принять заявку");
                        break;
                    case OUTGOING_REQUEST:
                        binding.addFriend.setText("Отменить заявку");
                        break;
                }
            }else{
                binding.addFriend.setText("Добавить в друзья");
            }
        });

        binding.addFriend.setOnClickListener(v -> {
            viewModel.addFriendButton(requireContext());
        });

        binding.sendMessage.setOnClickListener(v -> {
            replaceFragment(new MessageFragment(uid, viewModel.getUserData()));
        });

        return binding.getRoot();
    }

    private void replaceFragment(MessageFragment messageFragment) {
        FragmentManager fragmentManager = getParentFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.nav_host_fragment, messageFragment).addToBackStack(null);
        fragmentTransaction.commit();
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
