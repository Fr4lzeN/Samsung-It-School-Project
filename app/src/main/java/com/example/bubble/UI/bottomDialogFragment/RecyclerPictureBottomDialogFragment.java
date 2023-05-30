package com.example.bubble.UI.bottomDialogFragment;

import android.os.Bundle;

import androidx.fragment.app.DialogFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.bubble.databinding.FragmentRecyclerPictureBottomDialogBinding;
import com.example.bubble.tools.ENUMs.TakePictureEnum;

public class RecyclerPictureBottomDialogFragment extends DialogFragment {

    public interface Callback{
        void callback(TakePictureEnum action);
    }

    Callback callback;

    FragmentRecyclerPictureBottomDialogBinding binding;

    public RecyclerPictureBottomDialogFragment(Callback callback) {
        this.callback = callback;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentRecyclerPictureBottomDialogBinding.inflate(inflater,container,false);
        binding.deletePicture.setOnClickListener(v -> {
            callback.callback(TakePictureEnum.DELETE);
            dismiss();
        });
        binding.changePicture.setOnClickListener(v -> {
            callback.callback(TakePictureEnum.CHANGE);
            dismiss();
        });
        return binding.getRoot();
    }
}