package com.example.bubble.mainMenu;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.bubble.JSON.FriendInfo;
import com.example.bubble.JSON.UserInfoJSON;
import com.example.bubble.databinding.PeopleListItemListBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PeopleListRecyclerView extends RecyclerView.Adapter<PeopleListRecyclerView.PeopleListViewHolder> {

    public interface  OnItemClickListener{
        void onClick(String uid);
    }

    List<FriendInfo> data;
    OnItemClickListener listener;
    PeopleListItemListBinding binding;

    public PeopleListRecyclerView(List<FriendInfo> data, OnItemClickListener listener) {
        this.data = data;
        if (data!=null) {
            Collections.shuffle(this.data);
        }
        this.listener = listener;
    }

    @NonNull
    @Override
    public PeopleListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = PeopleListItemListBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new PeopleListViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull PeopleListViewHolder holder, int position) {
        holder.button.setVisibility(View.GONE);
        Calendar today = Calendar.getInstance();
        int year = today.get(Calendar.YEAR)- data.get(position).getUserData().dateOfBirth.year;
        if ((data.get(position).getUserData().dateOfBirth.month>(today.get(Calendar.MONTH)+1)) ||(data.get(position).getUserData().dateOfBirth.month==(today.get(Calendar.MONTH)+1) && data.get(position).getUserData().dateOfBirth.day>today.get(Calendar.DAY_OF_MONTH))){
            year--;
        }

        if (year%10 ==1){
            holder.age.setText(year+" год");
        }else
        if (year%10 <5 && year!=0){
            holder.age.setText(year+" года");
        }else {
            holder.age.setText(year + " лет");
        }

        holder.info.setMaxLines(4);
        holder.info.setText(data.get(position).getUserData().info);
        holder.name.setText(data.get(position).getUserData().name);
        holder.info.getViewTreeObserver().addOnGlobalLayoutListener(() -> {
            int lines = holder.info.getLayout().getLineCount();
            if (lines>0){
                if (holder.info.getLayout().getEllipsisCount(lines-1)>0){
                    holder.button.setVisibility(View.VISIBLE);
                }
            }
        });
        holder.button.setText("Узнать больше");
        Glide.with(holder.itemView.getContext()).load(data.get(position).getPicture()).into(holder.image);
        holder.itemView.setOnClickListener(v -> listener.onClick(data.get(position).getUid()));
        holder.button.setOnClickListener(v -> {
            if (holder.info.getMaxLines()==4){
                holder.info.setMaxLines(1000);
                holder.button.setText("Закрыть");
            }else{
                holder.info.setMaxLines(4);
                holder.button.setText("Узнать больше");
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class PeopleListViewHolder extends RecyclerView.ViewHolder{

        ShapeableImageView image;
        TextView name;
        TextView age;
        MaterialTextView info;
        MaterialButton button;
        public PeopleListViewHolder(PeopleListItemListBinding binding) {
            super(binding.getRoot());
            image=binding.image;
            name=binding.nameTextView;
            info = binding.infoTextView;
            age = binding.ageTextView;
            button = binding.button;
        }
    }
}
