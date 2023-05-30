package com.example.bubble.UI.adapter;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.bubble.databinding.PickProfilePictureItemListBinding;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.List;

public class TakePictureAdapter extends  RecyclerView.Adapter<TakePictureAdapter.TakePictureViewHolder> {


    public void setData(List<Uri> uris) {
        data=uris;
    }

    public interface OnItemClickListener{
        void onItemClick(Uri data, int position);
    }
    List<Uri> data;
    OnItemClickListener listener;


    public void addData(Uri uri){
        data.add(uri);
        int count = getItemCount();
        this.notifyItemInserted(count - 1);
        if (count>=6) {
            this.notifyItemChanged(0);
        }
    }

    public void changeData(Uri uri, int i){
        data.set(i, uri);
        this.notifyItemChanged(i);

    }

    public List<Uri> getData() {
        return data;
    }

    public void deleteData(int position){
        data.remove(position);
        this.notifyItemRemoved(position);
        this.notifyItemRangeChanged(position, getItemCount());
        if (getItemCount()<6){
            this.notifyItemChanged(0);
        }
    }


    public TakePictureAdapter(List<Uri> data, OnItemClickListener listener) {
        this.data = data;
        this.listener=listener;
    }

    @NonNull
    @Override
    public TakePictureViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        PickProfilePictureItemListBinding binding = PickProfilePictureItemListBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new TakePictureViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull TakePictureViewHolder holder, int position) {
        holder.bind(data.get(position), listener, position);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class  TakePictureViewHolder extends RecyclerView.ViewHolder{

      ShapeableImageView image;

      public TakePictureViewHolder(PickProfilePictureItemListBinding binding) {
          super(binding.getRoot());
          image = binding.image;
      }
      public void bind (Uri item, OnItemClickListener listener, int position){
          Glide.with(itemView.getContext()).load(item).into(image);
          image.setOnClickListener(v -> listener.onItemClick(item, position));
      }
    }

}
