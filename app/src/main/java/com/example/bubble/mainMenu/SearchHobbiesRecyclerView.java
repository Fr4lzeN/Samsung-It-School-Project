package com.example.bubble.mainMenu;

import android.annotation.SuppressLint;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bubble.databinding.SearchHobbyItemListBinding;
import com.google.firebase.database.GenericTypeIndicator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class SearchHobbiesRecyclerView extends RecyclerView.Adapter<SearchHobbiesRecyclerView.SearchHobbiesViewHolder> implements Filterable {


    public  interface OnItemClick {
        void onClick(String hobby);
    }

    SearchHobbyItemListBinding binding;
    ArrayList<String> data;
    ArrayList<String> searchData;
    OnItemClick listener;

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

    public SearchHobbiesRecyclerView(ArrayList<String> data, OnItemClick listener) {
        this.data = data;
        this.listener = listener;
        this.searchData = new ArrayList<>(data);
    }

    @NonNull
    @Override
    public SearchHobbiesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = SearchHobbyItemListBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new SearchHobbiesViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchHobbiesViewHolder holder, int position) {
        holder.textView.setText(searchData.get(position));
        holder.onBind(searchData.get(position));
    }

    @Override
    public int getItemCount() {
        return searchData.size();
    }

    @Override
    public Filter getFilter() {
        return filter;
    }



    public class SearchHobbiesViewHolder extends RecyclerView.ViewHolder{

        TextView textView;

        public SearchHobbiesViewHolder(SearchHobbyItemListBinding binding) {
            super(binding.getRoot());
            textView = binding.textView;
        }

        public void onBind(String hobby) {
            textView.setOnClickListener(v -> listener.onClick(hobby));
        }
    }
}
