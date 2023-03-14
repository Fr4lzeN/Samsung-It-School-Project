package com.example.bubble;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.bubble.databinding.FragmentRegistrationBinding;

public class RegistrationFragment extends Fragment {
    FragmentRegistrationBinding binding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding=FragmentRegistrationBinding.inflate(inflater, container, false);

        binding.backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(binding.getRoot()).popBackStack();
            }
        });

        binding.finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (binding.PasswordEditText.getText().toString().equals(binding.PasswordConfirmEditText.getText().toString())) {
                    progressBarOn();
                    Server
                            .registration(binding.loginEditText.getText().toString(),
                                    binding.PasswordEditText.getText().toString(),
                                    binding.EmailEditText.getText().toString())
                            .subscribe(this::onSuccess, this::onError);
                }
                else{
                    Toast.makeText(getContext(), "Пароли не совпадают", Toast.LENGTH_SHORT).show();
                }
            }

            void onSuccess(LoginDataJSON data){
                if (data.result){
                    Intent intent = new Intent();
                    intent.putExtra("login", data.login);
                    intent.putExtra("password", data.password);
                    Toast.makeText(getContext(), "Y", Toast.LENGTH_SHORT).show();
                    getActivity().setResult(Activity.RESULT_OK, intent);
                    getActivity().finish();
                }
                else{
                    progressBarOff();
                    switch (data.error){
                        case "email":
                            Toast.makeText(getContext(), "Аккаунт с такой почтой уже существует", Toast.LENGTH_SHORT).show();
                            break;
                        case "login":
                            Toast.makeText(getContext(), "Аккаунт с таким логином уже существует", Toast.LENGTH_SHORT).show();
                            break;
                    }
                }
            }

            void onError(Throwable t){
                progressBarOff();
                Toast.makeText(getContext(), "Проверьте свое подключение к интернету и попробуйте позже", Toast.LENGTH_LONG).show();
                Log.wtf("Login", t.toString());
            }

        });

        return binding.getRoot();
    }

    void progressBarOn(){
        binding.viewLayout.setVisibility(View.GONE);
        binding.progressBar.setVisibility(View.VISIBLE);
    }

    void progressBarOff(){
        binding.progressBar.setVisibility(View.GONE);
        binding.viewLayout.setVisibility(View.VISIBLE);
    }


}
