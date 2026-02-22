package com.example.phone_shop_app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.button.MaterialButton;

import java.util.List;

public class ManagePhoneAdapter extends RecyclerView.Adapter<ManagePhoneAdapter.ManagePhoneViewHolder> {

    public interface OnPhoneListener {
        void onEditClicked(PhoneModel phone);
        void onDeleteClicked(PhoneModel phone);
    }

    private final List<PhoneModel> phoneList;
    private final Context context;
    private final OnPhoneListener listener;

    public ManagePhoneAdapter(Context context, List<PhoneModel> phoneList, OnPhoneListener listener) {
        this.context = context;
        this.phoneList = phoneList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ManagePhoneViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_manage_phone, parent, false);
        return new ManagePhoneViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ManagePhoneViewHolder holder, int position) {
        PhoneModel phone = phoneList.get(position);

        holder.tvName.setText(phone.getName());
        holder.tvDescription.setText(phone.getDescription());
        holder.tvPrice.setText("TZS " + String.format("%,.2f", phone.getPrice()));

        Glide.with(context)
                .load(phone.getImageUrl())
                .placeholder(R.drawable.ic_launcher_background)
                .into(holder.imgPhone);

        holder.btnEdit.setOnClickListener(v -> listener.onEditClicked(phone));
        holder.btnDelete.setOnClickListener(v -> listener.onDeleteClicked(phone));
    }

    @Override
    public int getItemCount() {
        return phoneList.size();
    }

    public static class ManagePhoneViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvDescription, tvPrice;
        ImageView imgPhone;
        MaterialButton btnEdit, btnDelete;

        public ManagePhoneViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvPhoneName);
            tvDescription = itemView.findViewById(R.id.tvPhoneDescription);
            tvPrice = itemView.findViewById(R.id.tvPhonePrice);
            imgPhone = itemView.findViewById(R.id.imgPhoneItem);
            btnEdit = itemView.findViewById(R.id.btnEditPhone);
            btnDelete = itemView.findViewById(R.id.btnDeletePhone);
        }
    }
}
