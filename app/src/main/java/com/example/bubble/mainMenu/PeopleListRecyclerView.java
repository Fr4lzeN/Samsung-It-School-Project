package com.example.bubble.mainMenu;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.bubble.JSON.FriendInfo;
import com.example.bubble.JSON.UserInfoJSON;
import com.example.bubble.databinding.PeopleListItemListBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PeopleListRecyclerView extends RecyclerView.Adapter<PeopleListRecyclerView.PeopleListViewHolder> {

    public interface  OnItemClickListener{
        void onClick(String uid);
    }

    List<FriendInfo> data;
    OnItemClickListener listener;
    PeopleListItemListBinding binding;

    public PeopleListRecyclerView(List<FriendInfo> data, OnItemClickListener listener) {
        this.data = data;
        this.listener = listener;
    }

    @NonNull
    @Override
    public PeopleListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = PeopleListItemListBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new PeopleListViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull PeopleListViewHolder holder, int position) {
        holder.name.setText(data.get(position).getUserData().name);
        holder.info.setText(data.get(position).getUserData().info);
        Glide.with(holder.itemView.getContext()).load(data.get(position).getPicture()).into(holder.image);
        holder.itemView.setOnClickListener(v -> listener.onClick(data.get(position).getUid()));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class PeopleListViewHolder extends RecyclerView.ViewHolder{

        ShapeableImageView image;
        TextView name;
        TextView info;
        public PeopleListViewHolder(PeopleListItemListBinding binding) {
            super(binding.getRoot());
            image=binding.image;
            name=binding.nameTextView;
            info = binding.infoTextView;
        }
    }
}
