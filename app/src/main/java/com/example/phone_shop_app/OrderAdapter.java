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

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {

    private final List<OrderModel> orderList;
    private final Context context;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());

    public OrderAdapter(Context context, List<OrderModel> orderList) {
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
        holder.tvUserEmail.setText(order.getUserEmail());
        holder.tvUserPhone.setText(order.getUserPhoneNumber()); // Display phone number
        holder.tvPrice.setText(String.format(Locale.getDefault(), "TZS %,.2f", order.getPrice()));
        
        if (order.getOrderDate() != null) {
            holder.tvOrderDate.setText(dateFormat.format(order.getOrderDate()));
        }
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public static class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView tvPhoneName, tvUserEmail, tvPrice, tvOrderDate, tvUserPhone;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            tvPhoneName = itemView.findViewById(R.id.tvOrderPhoneName);
            tvUserEmail = itemView.findViewById(R.id.tvOrderUserEmail);
            tvUserPhone = itemView.findViewById(R.id.tvOrderUserPhone); // Find the new TextView
            tvPrice = itemView.findViewById(R.id.tvOrderPrice);
            tvOrderDate = itemView.findViewById(R.id.tvOrderDate);
        }
    }
}
