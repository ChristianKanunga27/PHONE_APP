package com.example.phone_shop_app;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class OrdersActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private OrderAdapter adapter;
    private List<OrderModel> orderList;
    private FirebaseFirestore db;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);

        setSupportActionBar(findViewById(R.id.toolbar));
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        recyclerView = findViewById(R.id.recyclerViewOrders);
        progressBar = findViewById(R.id.progressBarOrders);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        orderList = new ArrayList<>();
        adapter = new OrderAdapter(this, orderList);
        recyclerView.setAdapter(adapter);

        db = FirebaseFirestore.getInstance();
        loadOrders();
    }

    private void loadOrders() {
        progressBar.setVisibility(View.VISIBLE);
        db.collection("orders")
                .get()
                .addOnCompleteListener(task -> {
                    progressBar.setVisibility(View.GONE);
                    if (task.isSuccessful()) {
                        orderList.clear();
                        for (QueryDocumentSnapshot doc : task.getResult()) {
                            try {
                                OrderModel order = doc.toObject(OrderModel.class);
                                order.setOrderId(doc.getId());
                                orderList.add(order);
                            } catch (Exception e) {
                                Log.e("FirestoreError", "Error parsing document " + doc.getId(), e);
                            }
                        }
                        
                        // Sort the list by date in descending order (newest first)
                        Collections.sort(orderList, (o1, o2) -> {
                            if (o1.getOrderDate() == null || o2.getOrderDate() == null) {
                                return 0;
                            }
                            return o2.getOrderDate().compareTo(o1.getOrderDate());
                        });
                        
                        adapter.notifyDataSetChanged();

                        if (orderList.isEmpty()) {
                            Toast.makeText(this, "No orders found.", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Log.e("FirestoreError", "Error getting orders: ", task.getException());
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
