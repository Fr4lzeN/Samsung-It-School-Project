package com.example.bubble;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.bubble.databinding.FragmentProfileBinding;

public class ProfileFragment extends Fragment {

    String login;

    FragmentProfileBinding binding;

    public ProfileFragment(String login){
        this.login=login;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding=FragmentProfileBinding.inflate(inflater, container, false);
        Server.getUserInfo(login).subscribe(this::onSuccess, this::onError);
        return binding.getRoot();
    }

    void onSuccess(UserInfoJSON data){

    }

    void onError(Throwable t){
        Log.wtf("userData", t.toString());
    }

}