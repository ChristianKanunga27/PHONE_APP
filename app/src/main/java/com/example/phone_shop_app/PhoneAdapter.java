package com.example.phone_shop_app;

import android.app.AlertDialog;
import android.content.Context;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class PhoneAdapter extends RecyclerView.Adapter<PhoneAdapter.PhoneViewHolder> {

    private final List<PhoneModel> phoneList;
    private final Context context;
    private final FirebaseFirestore db;

    public PhoneAdapter(Context context, List<PhoneModel> phoneList) {
        this.context = context;
        this.phoneList = phoneList;
        this.db = FirebaseFirestore.getInstance();
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
        holder.tvPrice.setText("TZS " + String.format("%,.2f", phone.getPrice()));

        Glide.with(context)
                .load(phone.getImageUrl())
                .placeholder(R.drawable.ic_launcher_background) 
                .error(R.drawable.ic_launcher_background) 
                .into(holder.imgPhone);

        holder.btnBuy.setOnClickListener(v -> showPhoneNumberDialog(phone));
    }

    private void showPhoneNumberDialog(PhoneModel phone) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Confirm Purchase");

        final EditText input = new EditText(context);
        input.setHint("Enter your phone number");
        input.setInputType(InputType.TYPE_CLASS_PHONE);
        builder.setView(input);

        builder.setPositiveButton("Confirm", (dialog, which) -> {
            String phoneNumber = input.getText().toString().trim();
            if (phoneNumber.isEmpty()) {
                Toast.makeText(context, "Phone number is required", Toast.LENGTH_SHORT).show();
                return;
            }
            createOrder(phone, phoneNumber);
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

        builder.show();
    }

    private void createOrder(PhoneModel phone, String phoneNumber) {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) {
            Toast.makeText(context, "You must be logged in to buy", Toast.LENGTH_SHORT).show();
            return;
        }

        String userId = currentUser.getUid();
        String userEmail = currentUser.getEmail();

        OrderModel order = new OrderModel(
                phone.getId(),
                phone.getName(),
                phone.getPrice(),
                userId,
                userEmail,
                phoneNumber
        );

        db.collection("orders")
                .add(order)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(context, "Order placed successfully!", Toast.LENGTH_SHORT).show();
                    Log.d("OrderSuccess", "Order created with ID: " + documentReference.getId());
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(context, "Failed to place order: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e("OrderFailure", "Error placing order", e);
                });
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
            imgPhone = itemView.findViewById(R.id.imgPhoneItem);
            btnBuy = itemView.findViewById(R.id.btnBuy);
        }
    }
}
