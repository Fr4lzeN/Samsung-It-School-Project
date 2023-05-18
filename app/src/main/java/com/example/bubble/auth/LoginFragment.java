package com.example.bubble.auth;

import static android.content.Context.INPUT_METHOD_SERVICE;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.alimuzaffar.lib.pin.PinEntryEditText;
import com.example.bubble.R;
import com.example.bubble.databinding.FragmentLoginBinding;
import com.example.bubble.mainMenu.MainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
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
    AuthorizationActivityViewModel viewModel;
    ProgressDialog progressDialog;
    String timerBaseString;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding=FragmentLoginBinding.inflate(inflater, container, false);
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Проверка кода");
        timerBaseString = "Новый код через ";
        viewModel = new ViewModelProvider(requireActivity()).get(AuthorizationActivityViewModel.class);
        binding.finish.setOnClickListener(v -> {
            if (binding.phoneNumberEditText.hasFocus()) {
                hideKeyboard();
                binding.phoneNumberEditText.clearFocus();
            }
            if (viewModel.setPhoneNumber(binding.phoneNumberEditText.getText().toString())) {
                viewModel.setPinEntry(true);
                viewModel.sendCode(requireContext());
            }
            else{
                binding.phoneNumberInputLayout.setError("Неправильный номер");
                binding.phoneNumberEditText.setText("");
            }
        });

        viewModel.pinEntryActive.observe(getViewLifecycleOwner(), aBoolean -> {
            if (aBoolean) {
                binding.txtPinEntry.setText("");
                binding.txtPinEntry.setVisibility(View.VISIBLE);
                binding.textSwitcher.setVisibility(View.VISIBLE);
            }
            else{
                binding.txtPinEntry.setVisibility(View.GONE);
                binding.textSwitcher.setVisibility(View.GONE);
            }
        });

        binding.phoneNumberEditText.addTextChangedListener(new ExtendedTextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                binding.finish.setEnabled(s.toString().length() >= 12);
                if (!TextUtils.isEmpty(s.toString())) {
                    binding.phoneNumberInputLayout.setError(null);
                }
            }
        });

        viewModel.codeState.observe(getViewLifecycleOwner(), phoneAuthEnum -> {
            if (phoneAuthEnum!=null){
                switch (phoneAuthEnum){
                    case SUCCESS:
                        Intent intent = new Intent(getContext(), MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        break;
                    case WAITING:
                        progressDialog.show();
                        hideKeyboard();
                        binding.txtPinEntry.clearFocus();
                        break;
                    case FAILTURE:
                        progressDialog.dismiss();
                        binding.txtPinEntry.setText("");
                        Toast.makeText(requireContext(),"Неправильный код",Toast.LENGTH_SHORT).show();
                        break;
                    case WRONG_NUMBER:
                        viewModel.setPinEntry(false);
                        viewModel.clearCodeState();
                        binding.phoneNumberInputLayout.setError("Неправильный номер");
                        break;
                }
                viewModel.clearCodeState();
            }
        });

        binding.txtPinEntry.setOnPinEnteredListener(str -> {
            viewModel.inputCode(str.toString());
        });

        viewModel.time.observe(getViewLifecycleOwner(), s -> {
            if (s!=null) {
                binding.timerTextView.setText(timerBaseString + s);
            }else{
                binding.textSwitcher.showNext();
            }
        });

        binding.resendCode.setOnClickListener(v -> {
            viewModel.resendCode();
            binding.textSwitcher.showNext();
        });

        return binding.getRoot();
    }

    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) requireContext().getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(requireActivity().getCurrentFocus().getWindowToken(), 0);
    }


}