package com.example.bubble;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.Navigation;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.bubble.databinding.ActivityMainBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.prefs.Preferences;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    DatabaseReference data;
    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        mAuth=FirebaseAuth.getInstance();
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
                   replaceFragment(new ProfileFragment(this.getPreferences(MODE_PRIVATE).getString("login", null)));
                   break;
           }
           return true;
       });
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
        replaceFragment(new SearchFragment());
        binding.bottomNavigationView.setVisibility(View.VISIBLE);
    }

    void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.nav_host_fragment, fragment);
        fragmentTransaction.commit();
    }

}