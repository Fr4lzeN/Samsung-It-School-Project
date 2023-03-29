package com.example.bubble.auth;

import android.content.Intent;
import android.net.wifi.hotspot2.pps.Credential;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.text.Editable;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bubble.ExtendedTextWatcher;
import com.example.bubble.mainMenu.MainActivity;
import com.example.bubble.databinding.FragmentPhoneVerificationBinding;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class PhoneVerificationFragment extends Fragment {

    FragmentPhoneVerificationBinding binding;
    FirebaseAuth mAuth;
    String verificationId;
    String code;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentPhoneVerificationBinding.inflate(inflater, container, false);
        mAuth = FirebaseAuth.getInstance();

        PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential credential) {
                // This callback will be invoked in two situations:
                // 1 - Instant verification. In some cases the phone number can be instantly
                //     verified without needing to send or enter a verification code.
                // 2 - Auto-retrieval. On some devices Google Play services can automatically
                //     detect the incoming verification SMS and perform verification without
                //     user action.
                Log.d("TAG", "onVerificationCompleted:" + credential);

                signInWithPhoneAuthCredential(credential);
            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                // This callback is invoked in an invalid request for verification is made,
                // for instance if the the phone number format is not valid.
                Log.w("TAG", "onVerificationFailed", e);

                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    // Invalid request
                } else if (e instanceof FirebaseTooManyRequestsException) {
                    // The SMS quota for the project has been exceeded
                }

                Toast.makeText(getContext(), "Неправильный номер телефона", Toast.LENGTH_SHORT).show();
                Navigation.findNavController(binding.getRoot()).popBackStack();
            }

            @Override
            public void onCodeSent(@NonNull String VerificationId,
                                   @NonNull PhoneAuthProvider.ForceResendingToken token) {
                // The SMS verification code has been sent to the provided phone number, we
                // now need to ask the user to enter the code and then construct a credential
                // by combining the code with a verification ID.
                Log.d("TAG", "onCodeSent:" + verificationId);

                // Save verification ID and resending token so we can use them later
                verificationId = VerificationId;
                PhoneAuthProvider.ForceResendingToken mResendToken = token;
            }
        };

        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(LoginFragment.getPhoneNumber())       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(getActivity())                 // Activity (for callback binding)
                        .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);

        binding.code1.addTextChangedListener(new ExtendedTextWatcher(binding.code1) {

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length()==1){
                    changeCell(this.getView(), true);
                }
            }
        });
        binding.code1.setOnKeyListener((v, keyCode, event) -> {
            if (keyCode==KeyEvent.KEYCODE_DEL){
                if (event.getAction() ==  KeyEvent.ACTION_UP){
                    changeCell(v, false);
                }
            }
            return false;
        });
        binding.code2.addTextChangedListener(new ExtendedTextWatcher(binding.code2) {

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length()==1){
                    changeCell(this.getView(), true);
                }
            }

        });
        binding.code2.setOnKeyListener((v, keyCode, event) -> {
            if (keyCode==KeyEvent.KEYCODE_DEL){
                if (event.getAction() ==  KeyEvent.ACTION_UP){
                    changeCell(v, false);
                }
            }
            return false;
        });
        binding.code3.addTextChangedListener(new ExtendedTextWatcher(binding.code3) {

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length()==1){
                    changeCell(this.getView(), true);
                }
            }

        });
        binding.code3.setOnKeyListener((v, keyCode, event) -> {
            if (keyCode==KeyEvent.KEYCODE_DEL){
                if (event.getAction() ==  KeyEvent.ACTION_UP){
                    changeCell(v, false);
                }
            }
            return false;
        });
        binding.code4.addTextChangedListener(new ExtendedTextWatcher(binding.code4) {

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length()==1){
                    changeCell(this.getView(), true);
                }
            }

        });
        binding.code4.setOnKeyListener((v, keyCode, event) -> {
            if (keyCode==KeyEvent.KEYCODE_DEL){
                if (event.getAction() ==  KeyEvent.ACTION_UP){
                    changeCell(v, false);
                }
            }
            return false;
        });
        binding.code5.addTextChangedListener(new ExtendedTextWatcher(binding.code5) {

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length()==1){
                    changeCell(this.getView(), true);
                }
            }

        });
        binding.code5.setOnKeyListener((v, keyCode, event) -> {
            if (keyCode==KeyEvent.KEYCODE_DEL){
                if (event.getAction() ==  KeyEvent.ACTION_UP){
                    changeCell(v, false);
                }
            }
            return false;
        });
        binding.code6.addTextChangedListener(new ExtendedTextWatcher(binding.code6) {

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length()==1){
                    changeCell(this.getView(), true);
                }
            }
        });
        binding.code6.setOnKeyListener((v, keyCode, event) -> {
            if (keyCode==KeyEvent.KEYCODE_DEL){
                if (event.getAction() ==  KeyEvent.ACTION_UP){
                    changeCell(v, false);
                }
            }
            return false;
        });

        binding.backArrow.setOnClickListener(v -> Navigation.findNavController(binding.getRoot()).popBackStack());

        return binding.getRoot();
    }

    private void changeCell(View view, boolean next){
        if (view.equals(binding.code1)){
            if (next){
                binding.code2.requestFocus();
            }
            return;
        }
        if (view.equals(binding.code2)){
            if (next){
                binding.code3.requestFocus();
            }
            else{
                binding.code1.requestFocus();
            }
            return;
        }
        if (view.equals(binding.code3)){
            if (next){
                binding.code4.requestFocus();
            }
            else{
                binding.code2.requestFocus();
            }
            return;
        }
        if (view.equals(binding.code4)){
            if (next){
                binding.code5.requestFocus();
            }
            else{
                binding.code3.requestFocus();
            }
            return;
        }
        if (view.equals(binding.code5)){
            if (next){
                binding.code6.requestFocus();
            }
            else{
                binding.code4.requestFocus();
            }
            return;
        }
        if (view.equals(binding.code6)){
            if (next){
                code = binding.code1.getText().toString()+binding.code2.getText().toString()+binding.code3.getText().toString()+binding.code4.getText().toString()+binding.code5.getText().toString()+binding.code6.getText().toString();
                PhoneAuthCredential credential =PhoneAuthProvider.getCredential(verificationId,code);
                signInWithPhoneAuthCredential(credential);
            }
            else{
                binding.code5.requestFocus();
            }
            return;
        }
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(getActivity(), task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d("TAG", "signInWithCredential:success");
                        Intent intent = new Intent(getContext(), MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        // Update UI
                    } else {
                        // Sign in failed, display a message and update the UI
                        Log.w("TAG", "signInWithCredential:failure", task.getException());
                        if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                            // The verification code entered was invalid
                        }
                    }
                });
    }

}

