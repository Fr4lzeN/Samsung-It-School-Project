package com.example.bubble.UI.viewModels;

import android.content.Context;
import android.os.CountDownTimer;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.bubble.data.models.AuthorizationActivityModel;
import com.example.bubble.tools.ENUMs.PhoneAuthEnum;

import java.util.concurrent.TimeUnit;

public class AuthorizationActivityViewModel extends ViewModel {

    MutableLiveData<String> phoneNumber = new MutableLiveData<>();
    AuthorizationActivityModel model;

    public MutableLiveData<PhoneAuthEnum> codeState = new MutableLiveData<>();
    public MutableLiveData<Boolean> pinEntryActive = new MutableLiveData<>();

    public MutableLiveData<String> time = new MutableLiveData<>();

    CountDownTimer timer = new CountDownTimer(90000, 1000) {
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
            time.setValue(null);
        }
    };

    public void setTimer(){
        timer.start();
    }


    public boolean setPhoneNumber(String number){
        if(AuthorizationActivityModel.checkNumber(number)){
            phoneNumber.setValue(number);
            return true;
        }
        return false;
    }

    public void inputCode(String code){
        model.signIn(code);
    }

    public void resendCode() {
        model.resendCode(phoneNumber.getValue());
        setTimer();
    }

    public void sendCode(Context context){
        model = new AuthorizationActivityModel(context, codeState);
        model.createCallback();
        model.sendCode(phoneNumber.getValue());
        setTimer();
    }

    public void clearCodeState() {
        codeState.setValue(null);
    }

    public void setPinEntry(boolean key) {
        pinEntryActive.setValue(key);
    }
}
