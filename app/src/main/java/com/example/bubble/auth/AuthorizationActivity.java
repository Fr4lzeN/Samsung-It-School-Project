package com.example.bubble.auth;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.bubble.databinding.ActivityAuthorizationBinding;

public class AuthorizationActivity extends AppCompatActivity {

    static AuthorizationActivityViewModel viewModel;

    public static AuthorizationActivityViewModel getViewModel() {
        return viewModel;
    }

    ActivityAuthorizationBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityAuthorizationBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        viewModel = new ViewModelProvider(this).get(AuthorizationActivityViewModel.class);
    }
}