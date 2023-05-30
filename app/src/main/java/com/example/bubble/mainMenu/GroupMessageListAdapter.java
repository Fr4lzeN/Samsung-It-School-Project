package com.example.bubble.mainMenu;

import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.bubble.JSON.GroupMessageListItem;
import com.example.bubble.JSON.MessageListItem;
import com.example.bubble.databinding.MessageListItemListBinding;
import com.google.android.material.imageview.ShapeableImageView;
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
