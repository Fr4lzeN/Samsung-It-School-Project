package com.example.bubble.auth;

import android.app.Activity;
import android.content.Context;
import android.net.wifi.hotspot2.pps.Credential;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class AuthorizationActivityModel {


    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    String verificationId;
    PhoneAuthProvider.ForceResendingToken resendingToken;
    FirebaseAuth mAuth;
    Context context;
    MutableLiveData<PhoneAuthEnum> codeState;

    public AuthorizationActivityModel(Context context, MutableLiveData<PhoneAuthEnum> codeState) {
        this.context = context;
        this.codeState=codeState;
        mAuth = FirebaseAuth.getInstance();
    }

    public static boolean checkNumber(String number) {
        if (!TextUtils.isEmpty(number)) {
            return true;
        }
        else{
            return false;
        }
    }

    public  PhoneAuthProvider.OnVerificationStateChangedCallbacks createCallback() {
         mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential credential) {
                // This callback will be invoked in two situations:
                // 1 - Instant verification. In some cases the phone number can be instantly
                //     verified without needing to send or enter a verification code.
                // 2 - Auto-retrieval. On some devices Google Play services can automatically
                //     detect the incoming verification SMS and perform verification without
                //     user action.
                Log.d("TAG", "onVerificationCompleted:" + credential);
                signIn(credential);
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
                codeState.setValue(PhoneAuthEnum.WRONG_NUMBER);
            }

            @Override
            public void onCodeSent(@NonNull String VerificationId,
                                   @NonNull PhoneAuthProvider.ForceResendingToken token) {
                // The SMS verification code has been sent to the provided phone number, we
                // now need to ask the user to enter the code and then construct a credential
                // by combining the code with a verification ID.
                Log.d("TAG", "onCodeSent:" + VerificationId);
                verificationId = VerificationId;
                resendingToken = token;
            }
        };
        return mCallbacks;
    }

    private void signIn(PhoneAuthCredential credential) {
        codeState.setValue(PhoneAuthEnum.WAITING);
        FirebaseAuth.getInstance().signInWithCredential(credential).addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                codeState.setValue(PhoneAuthEnum.SUCCESS);
            }
            else{
                codeState.setValue(PhoneAuthEnum.FAILTURE);
            }
        });
    }

    public void signIn(String code){
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        signIn(credential);
    }

    public void sendCode(String number) {
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(number)       // Phone number to verify
                        .setTimeout(120L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity((Activity) context)                 // Activity (for callback binding)
                        .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    public void resendCode(String number) {
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(number)       // Phone number to verify
                        .setTimeout(120L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity((Activity) context)                 // Activity (for callback binding)
                        .setCallbacks(mCallbacks)      // OnVerificationStateChangedCallbacks
                        .setForceResendingToken(resendingToken)
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }
}
