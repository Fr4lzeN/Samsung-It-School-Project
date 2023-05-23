package com.example.bubble;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.bubble.JSON.UserInfoJSON;
import com.example.bubble.databinding.FragmentInformationTabBinding;
import com.example.bubble.mainMenu.ProfileFragmentViewModel;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Calendar;

public class InformationTabFragment extends Fragment {



    FragmentInformationTabBinding binding;
    ProfileFragmentViewModel viewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(requireParentFragment()).get(ProfileFragmentViewModel.class);
        viewModel.userInfo.observe(getViewLifecycleOwner(), userInfoJSON -> displayInfo(userInfoJSON));
        binding = FragmentInformationTabBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void displayInfo(UserInfoJSON user){
        Calendar today = Calendar.getInstance();
        int year = today.get(Calendar.YEAR)- user.dateOfBirth.year;
        if ((user.dateOfBirth.month>(today.get(Calendar.MONTH)+1)) ||(user.dateOfBirth.month==(today.get(Calendar.MONTH)+1) && user.dateOfBirth.day>today.get(Calendar.DAY_OF_MONTH))){
            year--;
        }
        binding.info.setText(user.info);
        binding.gender.setText(user.gender);

        if (year%10 ==1){
            binding.age.setText(year+" год");
            return;
        }

        if (year%10 <5 && year!=0){
            binding.age.setText(year+" года");
            return;
        }

        binding.age.setText(year+" лет");

    }

}