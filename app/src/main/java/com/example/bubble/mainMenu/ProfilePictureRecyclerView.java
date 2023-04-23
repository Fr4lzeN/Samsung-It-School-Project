package com.example.bubble.mainMenu;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.bubble.databinding.ProfilePictureItemListBinding;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.storage.StorageReference;

import java.util.List;

public class ProfilePictureRecyclerView extends RecyclerView.Adapter<ProfilePictureRecyclerView.PictureHolder> {

    List<StorageReference> data;


    public ProfilePictureRecyclerView(List<StorageReference> data) {
        this.data = data;
    }

    @NonNull
    @Override
    public PictureHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ProfilePictureItemListBinding binding = ProfilePictureItemListBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new PictureHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull PictureHolder holder, int position) {
        Glide.with(holder.itemView.getContext()).load(data.get(position)).into(holder.image);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class PictureHolder extends RecyclerView.ViewHolder{

        ShapeableImageView image;

        public PictureHolder(ProfilePictureItemListBinding binding) {
            super(binding.getRoot());
            image = binding.image;
        }

    }
}
