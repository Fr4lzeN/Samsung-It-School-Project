package com.example.bubble.UI.adapter;

import android.util.TypedValue;

import androidx.annotation.NonNull;

import com.example.bubble.data.JSONModels.FriendInfo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GroupChatAdapter extends PeopleListRecyclerView{

    List<Boolean> checked;


    public GroupChatAdapter(List<FriendInfo> data, OnItemClickListener listener) {
        super(data, listener);
        checked = new ArrayList<>(Collections.nCopies(data.size(),false));
    }

    @Override
    public void onBindViewHolder(@NonNull PeopleListViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        TypedValue typedValue = new TypedValue();
        if (checked.get(position)) {
            holder.itemView.getContext().getTheme().resolveAttribute(com.google.android.material.R.attr.colorPrimaryContainer, typedValue, true);
        }else{
            holder.itemView.getContext().getTheme().resolveAttribute(com.google.android.material.R.attr.colorSurfaceContainer, typedValue, true);
        }
        int color = typedValue.data;
        holder.itemView.setBackgroundColor(color);
        holder.itemView.setOnClickListener(v -> {
            checked.set(position, !checked.get(position));
            this.notifyItemChanged(position);
            listener.onClick(data.get(position).getUid());
        });
    }
}
