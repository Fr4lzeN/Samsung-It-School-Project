package com.example.bubble.UI.adapter;

import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.bubble.data.JSONModels.FriendInfo;
import com.example.bubble.data.JSONModels.MessageJSON;
import com.example.bubble.R;
import com.example.bubble.databinding.GroupMessageItemListBinding;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Map;

public class FirebaseGroupMessageAdapter extends FirebaseRecyclerAdapter<MessageJSON, FirebaseGroupMessageAdapter.GroupMessageViewHolder> {

    Map<String, FriendInfo> users;
    MutableLiveData<Integer> itemCount;
    MutableLiveData<Integer> allItemCount;

    public FirebaseGroupMessageAdapter(@NonNull FirebaseRecyclerOptions<MessageJSON> options, Map<String, FriendInfo> users, MutableLiveData<Integer> itemCount, MutableLiveData<Integer> allItemCount) {
        super(options);
        this.users = users;
        this.itemCount = itemCount;
        this.allItemCount = allItemCount;
    }

    @Override
    public void onDataChanged() {
        super.onDataChanged();
        allItemCount.setValue(getItemCount());
    }

    @Override
    protected void onBindViewHolder(@NonNull GroupMessageViewHolder holder, int position, @NonNull MessageJSON model) {
        TypedValue typedValue = new TypedValue();;
        MaterialCardView.LayoutParams params = (MaterialCardView.LayoutParams) holder.message.getLayoutParams();
        if (model.uid.equals(FirebaseAuth.getInstance().getUid())){
            holder.name.setVisibility(View.GONE);
            holder.name.setText("");
            holder.userImage.setVisibility(View.GONE);
            params.gravity= Gravity.END;
            holder.text.setGravity(Gravity.END);
            holder.time.setGravity(Gravity.END);
            holder.itemView.getContext().getTheme().resolveAttribute(com.google.android.material.R.attr.colorPrimaryContainer, typedValue, true);
        }
        else{
            holder.name.setVisibility(View.VISIBLE);
            try {
                holder.name.setText(users.get(model.uid).getUserData().name);
                Glide.with(holder.itemView.getContext()).load(users.get(model.uid).getPicture()).into(holder.userImage);
            }catch (NullPointerException e){
                holder.name.setText("Вышел");
                Glide.with(holder.itemView.getContext()).load(R.drawable.ic_launcher_foreground).into(holder.userImage);
            }
            params.gravity= Gravity.START;
            holder.text.setGravity(Gravity.START);
            holder.time.setGravity(Gravity.START);
            holder.itemView.getContext().getTheme().resolveAttribute(com.google.android.material.R.attr.colorSurfaceContainer, typedValue, true);
        }
        holder.message.setLayoutParams(params);
        int color = typedValue.data;
        holder.message.setCardBackgroundColor(color);
        holder.text.setText(model.message);
        Calendar date = new GregorianCalendar();
        date.setTimeInMillis(model.date);
        String minute = String.valueOf(date.get(Calendar.MINUTE));
        if (minute.length()==1){
            minute="0"+minute;
        }
        holder.time.setText(date.get(Calendar.DAY_OF_MONTH)+"/"+(date.get(Calendar.MONTH)+1)+"/"+date.get(Calendar.YEAR)+" "+date.get(Calendar.HOUR_OF_DAY)+":"+minute);
        itemCount.setValue(holder.getAbsoluteAdapterPosition());
    }

    @NonNull
    @Override
    public GroupMessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        GroupMessageItemListBinding binding = GroupMessageItemListBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false );
        return new GroupMessageViewHolder(binding);
    }

    public class GroupMessageViewHolder extends RecyclerView.ViewHolder{

        MaterialCardView message;
        ShapeableImageView userImage;
        TextView name;
        TextView text;
        TextView time;

        public GroupMessageViewHolder(@NonNull GroupMessageItemListBinding binding) {
            super(binding.getRoot());
            message=binding.message;
            userImage=binding.userPicture;
            name = binding.name;
            text = binding.text;
            time = binding.time;
        }
    }

}
