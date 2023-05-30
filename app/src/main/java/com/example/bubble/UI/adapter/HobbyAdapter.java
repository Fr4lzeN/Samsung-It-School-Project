package com.example.bubble.UI.adapter;

import android.annotation.SuppressLint;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.recyclerview.widget.RecyclerView;

import com.example.bubble.databinding.HobbyRecyclerViewItemListBinding;
import com.google.android.material.chip.Chip;

import java.util.ArrayList;
import java.util.List;

public class HobbyAdapter extends RecyclerView.Adapter<HobbyAdapter.HobbyViewHolder> implements Filterable {

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

        public HobbyAdapter(ArrayList<String> data, ArrayList<Boolean> checked) {
        this.data = data;
        this.checked = checked;
        searchData = new ArrayList<>(data);
    }

    public HobbyAdapter(ArrayList<String> data, ArrayList<Boolean> checked, OnItemClick listener) {
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
        holder.chip.setChecked(checked.get(data.indexOf(searchData.get(position))));
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
                notifyDataSetChanged();
                if (listener!=null){
                    listener.onItemClick(checked.contains(true));
                }
            });
        }
    }
}
