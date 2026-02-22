package com.example.phone_shop_app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.button.MaterialButton;

import java.util.List;

public class PhoneAdapter extends RecyclerView.Adapter<PhoneAdapter.PhoneViewHolder> {

    private final List<PhoneModel> phoneList;
    private final Context context;

    public PhoneAdapter(Context context, List<PhoneModel> phoneList) {
        this.context = context;
        this.phoneList = phoneList;
    }

    @NonNull
    @Override
    public PhoneViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_phone, parent, false);
        return new PhoneViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PhoneViewHolder holder, int position) {
        PhoneModel phone = phoneList.get(position);

        holder.tvName.setText(phone.getName());
        holder.tvDescription.setText(phone.getDescription());
        holder.tvPrice.setText("TZS " + phone.getPrice());

        Glide.with(context)
                .load(phone.getImageUrl())
                .placeholder(R.drawable.ic_launcher_background)
                .into(holder.imgPhone);

        holder.btnBuy.setOnClickListener(v ->
                Toast.makeText(context, "Buy " + phone.getName(), Toast.LENGTH_SHORT).show());
    }

    @Override
    public int getItemCount() {
        return phoneList.size();
    }

    public static class PhoneViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvDescription, tvPrice;
        ImageView imgPhone;
        MaterialButton btnBuy;

        public PhoneViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvPhoneName);
            tvDescription = itemView.findViewById(R.id.tvPhoneDescription);
            tvPrice = itemView.findViewById(R.id.tvPhonePrice);
            imgPhone = itemView.findViewById(R.id.imgPhoneItem); // Corrected ID
            btnBuy = itemView.findViewById(R.id.btnBuy);
        }
    }
}
