package com.example.phone_shop_app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

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
                .inflate(R.layout.item_phone_admin, parent, false);
        return new PhoneViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PhoneViewHolder holder, int position) {
        PhoneModel phone = phoneList.get(position);
        holder.tvName.setText(phone.name);
        holder.tvDescription.setText(phone.description);
        holder.tvPrice.setText("TZS " + phone.price);

        Glide.with(context)
                .load(phone.imageUrl)
                .into(holder.ivPhoneImage);

        // Delete phone
        holder.btnDelete.setOnClickListener(v -> {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            StorageReference storageRef = FirebaseStorage.getInstance().getReferenceFromUrl(phone.imageUrl);

            // Delete image from storage first
            storageRef.delete().addOnSuccessListener(aVoid -> {
                db.collection("phones").document(phone.id)
                        .delete()
                        .addOnSuccessListener(aVoid1 -> {
                            phoneList.remove(position);
                            notifyItemRemoved(position);
                            Toast.makeText(context, "Phone deleted successfully", Toast.LENGTH_SHORT).show();
                        })
                        .addOnFailureListener(e ->
                                Toast.makeText(context, "Failed to delete phone: " + e.getMessage(), Toast.LENGTH_SHORT).show());
            }).addOnFailureListener(e ->
                    Toast.makeText(context, "Failed to delete image: " + e.getMessage(), Toast.LENGTH_SHORT).show());
        });

        // Edit phone
        holder.btnEdit.setOnClickListener(v -> {
            if (context instanceof ManagePhonesActivity) {
                ((ManagePhonesActivity) context).showEditDialog(phone);
            }
        });
    }

    @Override
    public int getItemCount() {
        return phoneList.size();
    }

    static class PhoneViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvDescription, tvPrice;
        ImageView ivPhoneImage;
        Button btnEdit, btnDelete;

        public PhoneViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvPhoneName);
            tvDescription = itemView.findViewById(R.id.tvPhoneDescription);
            tvPrice = itemView.findViewById(R.id.tvPhonePrice);
            ivPhoneImage = itemView.findViewById(R.id.ivPhoneImage);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}
