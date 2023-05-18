package com.example.bubble.auth;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

public abstract class ExtendedTextWatcher implements TextWatcher{

    View view;

    public  void beforeTextChanged(CharSequence s, int start, int count, int after){};

    public  void onTextChanged(CharSequence s, int start, int before, int count){};

    public ExtendedTextWatcher() {
    }

    public View getView() {
        return view;
    }

    @Override
    public abstract void afterTextChanged(Editable s);

}
