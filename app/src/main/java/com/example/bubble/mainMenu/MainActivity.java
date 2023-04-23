package com.example.bubble.mainMenu;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.bubble.SettingsFragment;
import com.example.bubble.databinding.ActivityMainBinding;
import com.example.bubble.registration.FillDataActivity;
import com.example.bubble.R;
import com.example.bubble.auth.AuthorizationActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    FirebaseAuth mAuth;
    SearchFragment searchFragment = new SearchFragment();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        mAuth=FirebaseAuth.getInstance();
    }

    public void onStart(){
        super.onStart();
        FirebaseUser user = mAuth.getCurrentUser();
        if(user==null){
            Intent intent = new Intent(this, AuthorizationActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            return;
        }
        if (user.getPhotoUrl()==null){
            Intent intent = new Intent(this, FillDataActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            return;
        }
        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()){
                case R.id.search:
                    replaceFragment(searchFragment);
                    break;
                case R.id.messages:
                    replaceFragment(new MessagesFragment());
                    break;
                case R.id.friends:
                    replaceFragment(new FriendsFragment());
                    break;
                case R.id.profile:
                    replaceFragment(new SettingsFragment(user));
                    break;
            }
            return true;
        });
        binding.bottomNavigationView.setSelectedItemId(R.id.search);
        binding.bottomNavigationView.setVisibility(View.VISIBLE);
    }

    void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.nav_host_fragment, fragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data!=null && resultCode==RESULT_OK)
            Log.d("Picture", "1");
            EditPicturesFragmentDialog.getViewModel().onResult(requestCode, data.getData());
    }

}