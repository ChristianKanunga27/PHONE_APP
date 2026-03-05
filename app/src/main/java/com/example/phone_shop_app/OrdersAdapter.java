package com.example.phone_shop_app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class OrdersAdapter extends RecyclerView.Adapter<OrdersAdapter.OrderViewHolder> {

    private final Context context;
    private final List<OrderModel> orderList;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy, hh:mm a", Locale.getDefault());

    public OrdersAdapter(Context context, List<OrderModel> orderList) {
        this.context = context;
        this.orderList = orderList;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_order, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        OrderModel order = orderList.get(position);

        holder.tvPhoneName.setText(order.getPhoneName());
        holder.tvOrderPrice.setText("Price: TZS " + String.format("%,.2f", order.getPrice()));
        holder.tvUserEmail.setText("User: " + order.getUserEmail());
        holder.tvUserPhone.setText("Contact: " + order.getUserPhoneNumber());
        holder.tvOrderId.setText("Order ID: " + order.getOrderId());

        // ✅ CORRECTLY Format and display the date
        if (order.getOrderDate() != null) {
            holder.tvOrderDate.setText("Date: " + dateFormat.format(order.getOrderDate()));
        } else {
            holder.tvOrderDate.setText("Date: Not available");
        }
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public static class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView tvPhoneName, tvOrderPrice, tvUserEmail, tvUserPhone, tvOrderId, tvOrderDate;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            tvPhoneName = itemView.findViewById(R.id.tvOrderPhoneName);
            tvOrderPrice = itemView.findViewById(R.id.tvOrderPrice);
            tvUserEmail = itemView.findViewById(R.id.tvOrderUserEmail);
            tvUserPhone = itemView.findViewById(R.id.tvOrderUserPhone);
            tvOrderId = itemView.findViewById(R.id.tvOrderId);
            // ✅ CORRECTLY find the date TextView
            tvOrderDate = itemView.findViewById(R.id.tvOrderDate);
        }
    }
}
