package com.example.phone_shop_app;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;
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

        holder.btnBuy.setOnClickListener(v -> showPhoneNumberBottomSheet(phone));
    }

    private void showPhoneNumberBottomSheet(PhoneModel phone) {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context);
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_complete_order, null);
        bottomSheetDialog.setContentView(view);

        EditText etPhoneNumber = view.findViewById(R.id.etPhoneNumber);
        TextInputLayout tilPhone = view.findViewById(R.id.tilPhone);
        Button btnConfirmOrder = view.findViewById(R.id.btnConfirmOrder);

        btnConfirmOrder.setOnClickListener(v -> {
            String phoneNumber = etPhoneNumber.getText().toString().trim();
            
            // Validation
            if (phoneNumber.isEmpty()) {
                tilPhone.setError("Phone number is required");
                return;
            }
            if (!android.util.Patterns.PHONE.matcher(phoneNumber).matches() || phoneNumber.length() < 10) {
                tilPhone.setError("Enter a valid phone number");
                return;
            }

            tilPhone.setError(null);
            btnConfirmOrder.setEnabled(false);
            btnConfirmOrder.setText("Processing...");

            // Delayed success message (2 seconds) as requested
            new Handler().postDelayed(() -> {
                createOrder(phone, phoneNumber);
                bottomSheetDialog.dismiss();
            }, 2000);
        });

        bottomSheetDialog.show();
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
                    Toast.makeText(context, "Order Success! We will call you at " + phoneNumber, Toast.LENGTH_LONG).show();
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
