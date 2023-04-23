package com.example.bubble.registration;

import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.bubble.R;
import com.example.bubble.databinding.FragmentLoadPictureBinding;

import java.util.List;

public class LoadPictureFragment extends Fragment{

    FragmentLoadPictureBinding binding;
    boolean nextFragment = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding=FragmentLoadPictureBinding.inflate(inflater,container,false);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        FillDataActivity.getViewModel().pictureAdapter.observe(getViewLifecycleOwner(), takePictureRecyclerView -> {
            binding.recyclerView.setAdapter(takePictureRecyclerView);
        });
        FillDataActivity.getViewModel().data.observe(getViewLifecycleOwner(), this::buttonColor);

        binding.backButton.setOnClickListener(view -> {
                Navigation.findNavController(binding.getRoot()).popBackStack();
        });
        binding.nextButton.setOnClickListener(view -> {
                if (nextFragment){
                    FillDataActivity.getViewModel().sendUserInfo();
                    FillDataActivity.getViewModel().sendPictures();
                    FillDataActivity.getViewModel().setHobbies();
                    Navigation.findNavController(binding.getRoot()).navigate(R.id.action_loadPictureFragment_to_loadingFragment2);
                }
                else{
                    Toast.makeText(getContext(), "Необходимо загрузить хотя бы одну фотографию", Toast.LENGTH_SHORT).show();
                }
        });

        return binding.getRoot();
    }
    void buttonColor(List<Uri> uris){
        if (uris.size()>1){
            binding.nextButton.setBackgroundColor(getResources().getColor(R.color.green));
            nextFragment=true;
        }
        else{
            binding.nextButton.setBackgroundColor(getResources().getColor(R.color.purple_500));
            nextFragment=false;
        }
    }


}