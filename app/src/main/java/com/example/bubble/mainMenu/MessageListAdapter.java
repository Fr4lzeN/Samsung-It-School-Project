package com.example.bubble.mainMenu;


import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.bubble.JSON.MessageListItem;
import com.example.bubble.JSON.UserInfoJSON;
import com.example.bubble.databinding.MessageListItemListBinding;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

public class MessageListAdapter extends RecyclerView.Adapter<MessageListAdapter.MessageListViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(UserInfoJSON userInfo, String messageId, String uid);
    }

    MessageListItemListBinding binding;
    List<MessageListItem> data;
    OnItemClickListener listener;

    public MessageListAdapter() {
    }

    public MessageListAdapter(List<MessageListItem> data, OnItemClickListener listener) {
        this.data = data;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MessageListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = MessageListItemListBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new MessageListViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageListViewHolder holder, int position) {
        Glide.with(holder.itemView.getContext()).load(data.get(position).picture).into(holder.image);
        holder.name.setText(data.get(position).userInfo.name);
        String author;
        try {
            if (data.get(position).message.uid.equals(FirebaseAuth.getInstance().getUid())) {
                author = "Вы: ";
            } else {
                author = data.get(position).userInfo.name + ": ";
            }
            holder.message.setText(author + data.get(position).message.message);
            holder.itemView.setOnClickListener(v -> listener.onItemClick(data.get(position).userInfo, data.get(position).messageId, data.get(position).uid));
        }catch (java.lang.NullPointerException t){
            Log.d("Error", data.get(position).toString());
            Log.d("Error", t.toString());
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public  class MessageListViewHolder extends RecyclerView.ViewHolder{

        ShapeableImageView image;
        TextView name;
        TextView message;

        public MessageListViewHolder(MessageListItemListBinding binding) {
            super(binding.getRoot());
            image = binding.image;
            name = binding.name;
            message = binding.message;
        }
    }
}
