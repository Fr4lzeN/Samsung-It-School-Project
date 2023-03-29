package com.example.bubble.auth;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.bubble.R;
import com.example.bubble.databinding.FragmentLoginBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.concurrent.TimeUnit;

public class LoginFragment extends Fragment {
    FragmentLoginBinding binding;
    static String phoneNumber;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding=FragmentLoginBinding.inflate(inflater, container, false);

        binding.finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phoneNumber = binding.phoneNumber.getText().toString();
                if (TextUtils.isEmpty(phoneNumber) || phoneNumber.length() != 12) {
                    Toast.makeText(getContext(), "Неправильный номер", Toast.LENGTH_SHORT).show();
                    binding.phoneNumber.requestFocus();
                    return;
                }
                Navigation.findNavController(binding.getRoot()).navigate(R.id.action_loginFragment_to_phoneVerificationFragment);
            }
        });

        return binding.getRoot();
    }

    static String getPhoneNumber(){
        return phoneNumber;
    }


}