package com.example.bubble.UI.adapter;

import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bubble.data.JSONModels.MessageJSON;
import com.example.bubble.databinding.MessageItemListBinding;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class FirebaseMessageAdapter extends FirebaseRecyclerAdapter<MessageJSON, FirebaseMessageAdapter.MessageViewHolder> {

    MutableLiveData<Integer> itemCount;
    MutableLiveData<Integer> allItemCount;

    public FirebaseMessageAdapter(@NonNull FirebaseRecyclerOptions<MessageJSON> options, MutableLiveData<Integer> itemCount, MutableLiveData<Integer> allItemCount) {
        super(options);
        this.itemCount=itemCount;
        this.allItemCount=allItemCount;
    }

    @Override
    public int getItemCount() {
        return super.getItemCount();
    }

    @Override
    public void onDataChanged() {
        super.onDataChanged();
        allItemCount.setValue(getItemCount());
    }

    @Override
    protected void onBindViewHolder(@NonNull MessageViewHolder holder, int position, @NonNull MessageJSON model) {
        TypedValue typedValue = new TypedValue();;
        MaterialCardView.LayoutParams params = (MaterialCardView.LayoutParams) holder.message.getLayoutParams();
        if (model.uid.equals(FirebaseAuth.getInstance().getUid())){
            params.gravity= Gravity.END;
            holder.text.setGravity(Gravity.END);
            holder.time.setGravity(Gravity.END);
            holder.itemView.getContext().getTheme().resolveAttribute(com.google.android.material.R.attr.colorPrimaryContainer, typedValue, true);
        }
        else{
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
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        MessageItemListBinding binding = MessageItemListBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new MessageViewHolder(binding);
    }

    public class MessageViewHolder extends RecyclerView.ViewHolder{

        MaterialCardView message;
        TextView text;
        TextView time;

        public MessageViewHolder(@NonNull MessageItemListBinding binding) {
            super(binding.getRoot());
            message=binding.message;
            text=binding.text;
            time=binding.time;
        }
    }

}
