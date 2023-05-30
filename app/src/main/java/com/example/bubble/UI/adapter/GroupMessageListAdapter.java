package com.example.bubble.UI.adapter;

import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.example.bubble.data.JSONModels.GroupMessageListItem;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

public class GroupMessageListAdapter extends  MessageListAdapter{

    public  interface OnItemClickListener{
        void onItemClick(String chatId,String chatName, Uri picture);
    }

    OnItemClickListener listener;
    List<GroupMessageListItem> data;

    public GroupMessageListAdapter(List<GroupMessageListItem> data, OnItemClickListener listener) {
        this.data=data;
        this.listener=listener;
    }

    @Override
    public void onBindViewHolder(@NonNull MessageListViewHolder holder, int position) {
        Glide.with(holder.itemView.getContext()).load(data.get(position).picture).into(holder.image);
        holder.name.setText(data.get(position).chatName);
        String author;
        try {
            if (data.get(position).message.uid.equals(FirebaseAuth.getInstance().getUid())) {
                author = "Вы: ";
            } else {
                author = data.get(position).userName + ": ";
            }
            holder.message.setText(author + data.get(position).message.message);
            holder.itemView.setOnClickListener(v -> listener.onItemClick(data.get(position).groupChatId,data.get(position).chatName, data.get(position).picture));
        }catch (java.lang.NullPointerException t){
            Log.d("Error", t.toString());
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
}
