package com.example.bubble.mainMenu;

import android.view.View;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.example.bubble.JSON.FriendInfo;

import java.util.ArrayList;

public class GroupMembersAdapter extends MessageListAdapter{

    public  interface  OnItemClickListener{
        void onItemClick(String uid);
    }

    ArrayList<FriendInfo> data;
    OnItemClickListener listener;


    public GroupMembersAdapter(ArrayList<FriendInfo> data, OnItemClickListener listener) {
        this.data = data;
        this.listener=listener;
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public void onBindViewHolder(@NonNull MessageListViewHolder holder, int position) {
        holder.name.setText(data.get(position).getUserData().name);
        Glide.with(holder.itemView.getContext()).load(data.get(position).getPicture()).into(holder.image);
        holder.message.setVisibility(View.GONE);

    }
}
