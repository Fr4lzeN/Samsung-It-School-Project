package com.example.bubble.auth;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.bubble.mainMenu.MainActivity;
import com.example.bubble.databinding.FragmentPhoneVerificationBinding;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

public class PhoneVerificationFragment extends Fragment {

    FragmentPhoneVerificationBinding binding;
    ProgressDialog progressDialog;
    String timerBaseString = "Новый код через ";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentPhoneVerificationBinding.inflate(inflater, container, false);
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Проверка кода");


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

                AuthorizationActivity.getViewModel().signIn(credential);
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
                Log.d("TAG", "onCodeSent:" + VerificationId);
                AuthorizationActivity.getViewModel().onCodeSent(VerificationId, token);
            }
        };

        AuthorizationActivity.getViewModel().phoneNumber.observe(getViewLifecycleOwner(), s -> {
            AuthorizationActivity.getViewModel().clearCodeState();
        });

        AuthorizationActivity.getViewModel().codeState.observe(getViewLifecycleOwner(), stateEnum -> {
            switch (stateEnum){
                case NONE:
                    AuthorizationActivity.getViewModel().sendCode(requireActivity(), mCallbacks);
                    break;
                case RESEND:
                    binding.textSwitcher.showNext();
                    break;
                case WAITING:
                    break;
            }
        });

        AuthorizationActivity.getViewModel().time.observe(getViewLifecycleOwner(), s -> {
            binding.timerText.setText(timerBaseString+s);
        });

        binding.newCode.setOnClickListener(v -> {
            AuthorizationActivity.getViewModel().resendCode(requireActivity(), mCallbacks);
            binding.textSwitcher.showNext();
        });

        binding.txtPinEntry.setOnPinEnteredListener(str -> {
            AuthorizationActivity.getViewModel().signIn(str.toString());
            progressDialog.show();
        });

        AuthorizationActivity.getViewModel().result.observe(getViewLifecycleOwner(), aBoolean -> {
            progressDialog.dismiss();
            if (aBoolean){
                Toast.makeText(getContext(), "Успешный вход", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getContext(), MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
            else{
                Toast.makeText(getContext(), "Неправильный код", Toast.LENGTH_SHORT).show();
            }
        });

        binding.backArrow.setOnClickListener(v -> Navigation.findNavController(binding.getRoot()).popBackStack());

        return binding.getRoot();
    }

}

