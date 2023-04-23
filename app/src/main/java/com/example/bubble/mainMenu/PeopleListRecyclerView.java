package com.example.bubble.mainMenu;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.bubble.JSON.UserInfoJSON;
import com.example.bubble.databinding.PeopleListItemListBinding;
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

    Map<String, UserInfoJSON> map;
    List<String> uids;
    OnItemClickListener listener;
    PeopleListItemListBinding binding;

    public PeopleListRecyclerView(List<String> uids, Map<String,UserInfoJSON> map , OnItemClickListener listener) {
        this.listener = listener;
        this.uids=uids;
        this.map=map;
    }

    @NonNull
    @Override
    public PeopleListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = PeopleListItemListBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new PeopleListViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull PeopleListViewHolder holder, int position) {
        holder.name.setText(map.get(uids.get(position)).name);
        holder.info.setText(map.get(uids.get(position)).info);
        Glide.with(holder.itemView.getContext()).load(FirebaseStorage.getInstance().getReference(uids.get(position)).child("1")).into(holder.image);
        holder.itemView.setOnClickListener(v -> listener.onClick(uids.get(position)));
    }

    @Override
    public int getItemCount() {
        return map.size();
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
