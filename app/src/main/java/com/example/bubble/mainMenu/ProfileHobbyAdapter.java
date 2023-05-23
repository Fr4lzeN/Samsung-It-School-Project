package com.example.bubble.mainMenu;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bubble.databinding.ProfileHobbyListItemBinding;

import java.util.ArrayList;
import java.util.List;

public class ProfileHobbyAdapter extends RecyclerView.Adapter<ProfileHobbyAdapter.ProfileHobbyViewHolder> {

    List<String> data;

    ProfileHobbyListItemBinding binding;

    public ProfileHobbyAdapter(List<String> data) {
        this.data = data;
    }

    @NonNull
    @Override
    public ProfileHobbyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = ProfileHobbyListItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ProfileHobbyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ProfileHobbyViewHolder holder, int position) {
        holder.textView.setText(data.get(position));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ProfileHobbyViewHolder extends RecyclerView.ViewHolder{

        TextView textView;

        public ProfileHobbyViewHolder(ProfileHobbyListItemBinding binding) {
            super(binding.getRoot());
            textView = binding.textView;
        }
    }
}
