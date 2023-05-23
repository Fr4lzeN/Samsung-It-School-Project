package com.example.bubble;

import android.graphics.Color;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bubble.JSON.MessageJSON;
import com.example.bubble.databinding.MessageItemListBinding;
import com.example.bubble.mainMenu.MessageFragmentModel;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.color.MaterialColors;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class FirebaseMessageAdapter extends FirebaseRecyclerAdapter<MessageJSON, FirebaseMessageAdapter.MessageViewHolder> {

    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     * @param itemCount
     */

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
        TypedValue typedValue;
        if (model.uid.equals(FirebaseAuth.getInstance().getUid())){
            MaterialCardView.LayoutParams params = (MaterialCardView.LayoutParams) holder.message.getLayoutParams();
            params.gravity= Gravity.END;
            holder.message.setLayoutParams(params);
            holder.text.setGravity(Gravity.END);
            holder.time.setGravity(Gravity.END);
            typedValue = new TypedValue();
            holder.itemView.getContext().getTheme().resolveAttribute(com.google.android.material.R.attr.colorPrimaryContainer, typedValue, true);
        }
        else{
            MaterialCardView.LayoutParams params = (MaterialCardView.LayoutParams) holder.message.getLayoutParams();
            params.gravity= Gravity.START;
            holder.message.setLayoutParams(params);
            holder.text.setGravity(Gravity.START);
            holder.time.setGravity(Gravity.START);
            typedValue = new TypedValue();
            holder.itemView.getContext().getTheme().resolveAttribute(com.google.android.material.R.attr.colorSurfaceContainer, typedValue, true);
        }
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
