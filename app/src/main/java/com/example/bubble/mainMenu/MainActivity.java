package com.example.bubble.mainMenu;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.bubble.SettingsFragment;
import com.example.bubble.databinding.ActivityMainBinding;
import com.example.bubble.registration.FillDataActivity;
import com.example.bubble.R;
import com.example.bubble.auth.AuthorizationActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;

import io.reactivex.subjects.PublishSubject;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    public static MainActivityViewModel viewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        //AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        viewModel = new ViewModelProvider(this).get(MainActivityViewModel.class);
        viewModel.auth();
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

    public static MainActivityViewModel getViewModel() {
        return viewModel;
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data!=null && resultCode==RESULT_OK) {
            Log.d("Picture", "1");
            EditPicturesFragmentDialog.getViewModel().onResult(requestCode, data.getData());
        }
    }

}