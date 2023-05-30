package com.example.bubble.UI;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.bubble.UI.fragments.SettingsFragment;
import com.example.bubble.R;
import com.example.bubble.UI.fragments.FriendsFragment;
import com.example.bubble.UI.viewModels.MainActivityViewModel;
import com.example.bubble.UI.fragments.MessagesFragment;
import com.example.bubble.UI.fragments.SearchFragment;
import com.example.bubble.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    MainActivityViewModel viewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        viewModel = new ViewModelProvider(this).get(MainActivityViewModel.class);
        viewModel.auth();

        viewModel.incomingRequests.observe(this, friendInfos -> {
            if (friendInfos!=null && friendInfos.size()!=0) {
                binding.bottomNavigationView.getOrCreateBadge(R.id.friends).setNumber(friendInfos.size());
            }else{
                binding.bottomNavigationView.removeBadge(R.id.friends);
            }
        });

        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()){
                case R.id.search:
                    replaceFragment(new SearchFragment());
                    break;
                case R.id.messages:
                    replaceFragment(new MessagesFragment());
                    break;
                case R.id.friends:
                    replaceFragment(new FriendsFragment());
                    break;
                case R.id.profile:
                    replaceFragment(new SettingsFragment(viewModel.user.getValue()));
                    break;
            }
            return true;
        });
    }

    public void onStart(){
        super.onStart();
        viewModel.user.observe(this, firebaseUser -> {
            if (firebaseUser==null){
                Intent intent = new Intent(this, AuthorizationActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                return;
            }
            if (firebaseUser.getPhotoUrl()==null){
                Intent intent = new Intent(this, FillDataActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                return;
            }
            if (binding.bottomNavigationView.getVisibility()==View.GONE) {
                binding.bottomNavigationView.setSelectedItemId(R.id.search);
                binding.bottomNavigationView.setVisibility(View.VISIBLE);
                viewModel.getMessageData();
                viewModel.getGroupMessageData();
                viewModel.getFriendList();
                viewModel.getHobbies();
            }

        });

    }

    void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        for (int i=0; i< fragmentManager.getBackStackEntryCount(); i++){
            fragmentManager.popBackStack();
        }
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.nav_host_fragment, fragment);
        fragmentTransaction.commit();
    }


}