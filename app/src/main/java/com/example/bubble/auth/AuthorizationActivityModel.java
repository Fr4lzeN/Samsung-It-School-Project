package com.example.bubble.auth;

import android.app.Activity;
import android.net.wifi.hotspot2.pps.Credential;
import android.os.CountDownTimer;
import android.text.TextUtils;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class AuthorizationActivityModel {

        public static boolean checkNumber(String number) {
        if (!TextUtils.isEmpty(number)) {
            return true;
        }
        else{
            return false;
        }
    }

    public static Task<AuthResult> signIn(FirebaseAuth mAuth, String verificationId, String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId,code);
        return mAuth.signInWithCredential(credential);
    }

    public static Task<AuthResult> signIn(FirebaseAuth mAuth, PhoneAuthCredential credential) {
        return mAuth.signInWithCredential(credential);
    }

    public static void sendCode(FirebaseAuth mAuth, String phoneNumber, Activity activity, PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks) {
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(phoneNumber)       // Phone number to verify
                        .setTimeout(120L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(activity)                 // Activity (for callback binding)
                        .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    public static void resendCode(FirebaseAuth mAuth, String phoneNumber, Activity activity, PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks, PhoneAuthProvider.ForceResendingToken token) {
        PhoneAuthOptions resendOptions =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(phoneNumber)       // Phone number to verify
                        .setTimeout(120L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(activity)                 // Activity (for callback binding)
                        .setCallbacks(mCallbacks)           // OnVerificationStateChangedCallbacks
                        .setForceResendingToken(token)
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(resendOptions);
    }
}
