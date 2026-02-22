package com.example.phone_shop_app;

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

    private final List<OrderModel> orderList;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());

    public OrdersAdapter(List<OrderModel> orderList) {
        this.orderList = orderList;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        OrderModel order = orderList.get(position);

        holder.tvPhoneName.setText(order.getPhoneName());
        holder.tvUserEmail.setText(order.getUserEmail());
        holder.tvPrice.setText(String.format(Locale.getDefault(), "TZS %,.2f", order.getPrice()));

        if (order.getOrderDate() != null) {
            holder.tvOrderDate.setText(dateFormat.format(order.getOrderDate()));
        } else {
            holder.tvOrderDate.setText("Date not available");
        }
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public static class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView tvPhoneName, tvUserEmail, tvPrice, tvOrderDate;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            tvPhoneName = itemView.findViewById(R.id.tvOrderPhoneName);
            tvUserEmail = itemView.findViewById(R.id.tvOrderUserEmail);
            tvPrice = itemView.findViewById(R.id.tvOrderPrice);
            tvOrderDate = itemView.findViewById(R.id.tvOrderDate);
        }
    }
}
