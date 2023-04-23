package com.example.bubble.auth;

import android.app.Activity;
import android.os.CountDownTimer;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class AuthorizationActivityViewModel extends ViewModel {

    MutableLiveData<String> phoneNumber = new MutableLiveData<>();
    MutableLiveData<String> verificationId = new MutableLiveData<>();
    MutableLiveData<PhoneAuthProvider.ForceResendingToken> token = new MutableLiveData<>();

    MutableLiveData<Boolean> result = new MutableLiveData<>();
    MutableLiveData<PhoneAuthEnum> codeState = new MutableLiveData<>(PhoneAuthEnum.NONE);

    final MutableLiveData<FirebaseAuth> mAuth = new MutableLiveData<>(FirebaseAuth.getInstance());

    MutableLiveData<String> time = new MutableLiveData<>();

    boolean reset = false;


    CountDownTimer timer = new CountDownTimer(120000, 1000) {
        @Override
        public void onTick(long millisUntilFinished) {
            String timeLeft = String.format("%02d:%02d",
                    TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished),
                    TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished)
                            - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((millisUntilFinished))));
            time.setValue(timeLeft);
        }

        @Override
        public void onFinish() {
            codeState.setValue(PhoneAuthEnum.RESEND);
        }
    };

    public void setTimer(){
        timer.start();
        codeState.setValue(PhoneAuthEnum.WAITING);
    }


    OnCompleteListener<AuthResult> listener = task -> {
        result.setValue(task.isSuccessful());
    };


    public boolean setPhoneNumber(String number){
        if(AuthorizationActivityModel.checkNumber(number)){
            if (!Objects.equals(phoneNumber.getValue(), number)) {
                reset = true;
                phoneNumber.setValue(number);
            }
                return true;
        }
        return false;
    }

    public void onCodeSent(String verificationId, PhoneAuthProvider.ForceResendingToken token) {
        this.verificationId.setValue(verificationId);
        this.token.setValue(token);
    }

    public void signIn(String code) {
        AuthorizationActivityModel.signIn(mAuth.getValue(), verificationId.getValue(), code).addOnCompleteListener(listener);
    }

    public void signIn(PhoneAuthCredential credential){
        AuthorizationActivityModel.signIn(mAuth.getValue(), credential).addOnCompleteListener(listener);
    }

    public void resendCode(Activity activity, PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks) {
        AuthorizationActivityModel.resendCode(mAuth.getValue(), phoneNumber.getValue(), activity, mCallbacks, token.getValue());
        setTimer();
    }

    public void sendCode(Activity activity, PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks) {
        AuthorizationActivityModel.sendCode(mAuth.getValue(), phoneNumber.getValue(), activity, mCallbacks);
        setTimer();
    }

    public void clearCodeState(){
        if (reset) {
            codeState.setValue(PhoneAuthEnum.NONE);
            reset = false;
        }
    }
}
