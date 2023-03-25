package com.example.bubble;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bubble.databinding.PickProfilePictureItemListBinding;
import com.google.android.material.imageview.ShapeableImageView;
import com.squareup.picasso.Picasso;

import java.util.List;

public class TakePictureRecyclerView extends  RecyclerView.Adapter<TakePictureRecyclerView.TakePictureViewHolder> {



    interface OnItemClickListener{
        void onItemClick(Uri data, int position);
    }

    int changePosition;
    List<Uri> data;
    OnItemClickListener listener;


    public void addData(Uri uri){
        data.add(uri);
        int count = getItemCount();
        this.notifyItemInserted(count - 1);
        if (count>=6) {
            this.notifyItemChanged(0);
        }
        FillDataActivity.setData(this.data);
    }

    public void changeData(Uri uri){
        if (changePosition!=-1) {
            data.set(changePosition, uri);
            this.notifyItemChanged(changePosition);
            FillDataActivity.setData(this.data);
        }
    }

    public void deleteData(int position){
        data.remove(position);
        this.notifyItemRemoved(position);
        this.notifyItemRangeChanged(position, getItemCount());
        if (getItemCount()<6){
            this.notifyItemChanged(0);
        }
        FillDataActivity.setData(this.data);
    }

    public void setPictureChangePosition(int position) {
        changePosition=position;
    }

    public TakePictureRecyclerView(List<Uri> data, OnItemClickListener listener) {
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
        if (position==0 && getItemCount()==6){
            holder.image.setVisibility(View.GONE);
        }
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
          Picasso.get().load(item).into(image);
          image.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                  listener.onItemClick(item, position);
              }
          });
      }
    }

}
