package com.example.bubble.registration;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.bubble.R;
import com.example.bubble.databinding.HobbyRecyclerViewItemListBinding;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HobbyRecyclerView extends RecyclerView.Adapter<HobbyRecyclerView.HobbyViewHolder> {

    List<String> data;
    List<Boolean> checked;
    Activity activity;

    public interface OnItemClick{
        void onItemClick(int position, boolean isChecked);
    }

    OnItemClick listener;

    public HobbyRecyclerView(Activity activity, List<String> data,List<Boolean> checked, OnItemClick listener) {
        this.data = data;
        this.checked = checked;
        this.listener = listener;
        this.activity=activity;
    }

    @Override
    public HobbyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        HobbyRecyclerViewItemListBinding binding = HobbyRecyclerViewItemListBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new HobbyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(HobbyViewHolder holder, int position) {
        holder.textView.setText(data.get(position));
        if (checked.get(position)){
            holder.itemView.setBackgroundColor(activity.getResources().getColor(R.color.green));
        }else{
            holder.itemView.setBackgroundColor(activity.getResources().getColor(R.color.white));
        }
        holder.bind(activity,position);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class HobbyViewHolder extends RecyclerView.ViewHolder{

        TextView textView;

        public HobbyViewHolder(HobbyRecyclerViewItemListBinding binding) {
            super(binding.getRoot());
            this.textView=binding.textView;

        }

        public void bind(Activity activity, int position) {
            textView.setOnClickListener(v ->{
               checked.set(position, !checked.get(position));
                listener.onItemClick(position, checked.get(position));
            });
        }
    }
}
