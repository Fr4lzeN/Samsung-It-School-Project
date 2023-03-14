package com.example.bubble;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.bubble.databinding.ActivityMainBinding;
import com.google.firebase.auth.FirebaseAuth;

import java.util.prefs.Preferences;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    SharedPreferences preferences;
    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        mAuth=FirebaseAuth.getInstance();
        Log.d("FireBase", mAuth.getCurrentUser().toString());
        //autoLogin();
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

    void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.nav_host_fragment, fragment);
        fragmentTransaction.commit();
    }

    void autoLogin(){
        preferences = this.getPreferences(MODE_PRIVATE);
        String password = preferences.getString("password", null);
        String login = preferences.getString("login", null);
        if (password!=null && login !=null){
            Server.logIn(login, password).subscribe(this::onSuccess, this::onError);
        }
        else{
            Intent intent = new Intent(this, AuthorizationActivity.class);
            startActivityForResult(intent, 0);
        }
    }

    void onSuccess (LoginDataJSON data){
        Toast.makeText(this, data.login + " " + data.password, Toast.LENGTH_SHORT).show();
        binding.bottomNavigationView.setVisibility(View.VISIBLE);
        replaceFragment(new SearchFragment());
    }

    void onError (Throwable t){
        Toast.makeText(this, "Проверьте свое подключение к интернету или попробуйте позже", Toast.LENGTH_LONG).show();
        Log.wtf("Login", t.toString());
        autoLogin();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        binding.bottomNavigationView.setVisibility(View.VISIBLE);
        replaceFragment(new SearchFragment());
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("login", data.getStringExtra("login"));
        editor.putString("password", data.getStringExtra("password"));
        editor.apply();
    }
}