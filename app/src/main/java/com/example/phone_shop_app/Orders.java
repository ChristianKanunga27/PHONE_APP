package com.example.phone_shop_app;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Orders extends AppCompatActivity {

    private RecyclerView recyclerViewOrders;
    private OrdersAdapter ordersAdapter;
    private List<OrderModel> orderList;
    private FirebaseFirestore db;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Client Orders");
        }

        db = FirebaseFirestore.getInstance();
        // ✅ CORRECTED: Using the correct ID from activity_orders.xml
        progressBar = findViewById(R.id.progressBarOrders);
        recyclerViewOrders = findViewById(R.id.recyclerViewOrders);

        orderList = new ArrayList<>();
        // ✅ CORRECTED: Passing the context to the adapter
        ordersAdapter = new OrdersAdapter(this, orderList);
        recyclerViewOrders.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewOrders.setAdapter(ordersAdapter);

        fetchOrders();
    }

    private void fetchOrders() {
        if (progressBar != null) {
            progressBar.setVisibility(View.VISIBLE);
        }
        recyclerViewOrders.setVisibility(View.GONE);

        // ✅ ROBUST: Fetching all orders and sorting on the client to prevent crashes
        db.collection("orders")
                .get()
                .addOnCompleteListener(task -> {
                    if (progressBar != null) {
                        progressBar.setVisibility(View.GONE);
                    }
                    recyclerViewOrders.setVisibility(View.VISIBLE);

                    if (task.isSuccessful()) {
                        orderList.clear();
                        for (QueryDocumentSnapshot doc : task.getResult()) {
                            try {
                                // Using toObject for safer parsing
                                OrderModel order = doc.toObject(OrderModel.class);
                                order.setOrderId(doc.getId());
                                orderList.add(order);
                            } catch (Exception e) {
                                Log.e("OrdersActivity", "Error parsing order: " + doc.getId(), e);
                            }
                        }
                        
                        // Sorting on the client is more reliable
                        Collections.sort(orderList, (o1, o2) -> {
                            if (o1.getOrderDate() == null || o2.getOrderDate() == null) return 0;
                            return o2.getOrderDate().compareTo(o1.getOrderDate());
                        });
                        
                        ordersAdapter.notifyDataSetChanged();

                        if (orderList.isEmpty()) {
                            Toast.makeText(this, "No orders have been placed yet.", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Log.e("OrdersActivity", "Error fetching orders: ", task.getException());
                        Toast.makeText(this, "Failed to load orders.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
