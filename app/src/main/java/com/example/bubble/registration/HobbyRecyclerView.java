package com.example.bubble.registration;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.bubble.R;
import com.example.bubble.databinding.HobbyRecyclerViewItemListBinding;
import com.google.android.material.chip.Chip;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HobbyRecyclerView extends RecyclerView.Adapter<HobbyRecyclerView.HobbyViewHolder> implements Filterable {

    public List<String> getData() {
        return data;
    }

    public ArrayList<Boolean> getChecked() {
        return checked;
    }

    public interface OnItemClick{
        void onItemClick(boolean chipChecked);
    }

    ArrayList<String> data;
    ArrayList<String> searchData;
    ArrayList<Boolean> checked;
    OnItemClick listener = null;


    Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            ArrayList<String> filtered = new ArrayList<>();
            if (TextUtils.isEmpty(constraint)){
                filtered.addAll(data);
            }else{
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (String i : data){
                    if (i.toLowerCase().contains(filterPattern)){
                        filtered.add(i);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values=filtered;
            return  results;
        }

        @SuppressLint("NotifyDataSetChanged")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            searchData.clear();
            searchData.addAll((ArrayList)results.values);
            notifyDataSetChanged();
        }
    };

        public HobbyRecyclerView(ArrayList<String> data,ArrayList<Boolean> checked) {
        this.data = data;
        this.checked = checked;
        searchData = new ArrayList<>(data);
    }

    public HobbyRecyclerView(ArrayList<String> data, ArrayList<Boolean> checked, OnItemClick listener) {
        this.data = data;
        this.checked = checked;
        this.listener = listener;
        searchData = new ArrayList<>(data);
    }

    @Override
    public HobbyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        HobbyRecyclerViewItemListBinding binding = HobbyRecyclerViewItemListBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new HobbyViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(HobbyViewHolder holder, int position) {
        holder.chip.setText(searchData.get(position));
        holder.chip.setSelected(checked.get(data.indexOf(searchData.get(position))));
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return searchData.size();
    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    class HobbyViewHolder extends RecyclerView.ViewHolder{

        Chip chip;

        public HobbyViewHolder(HobbyRecyclerViewItemListBinding binding) {
            super(binding.getRoot());
            this.chip=binding.chip;
        }

        public void bind(int position) {
            chip.setOnClickListener(v -> {
                checked.set(data.indexOf(searchData.get(position)), !checked.get(data.indexOf(searchData.get(position))));
                HobbyRecyclerView.this.notifyItemChanged(position);
                if (listener!=null){
                    listener.onItemClick(checked.contains(true));
                }
            });
        }
    }
}
