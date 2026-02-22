package com.example.phone_shop_app;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class OrdersActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private OrderAdapter adapter;
    private List<OrderModel> orderList;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);

        setSupportActionBar(findViewById(R.id.toolbar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerView = findViewById(R.id.recyclerViewOrders);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        orderList = new ArrayList<>();
        adapter = new OrderAdapter(this, orderList);
        recyclerView.setAdapter(adapter);

        db = FirebaseFirestore.getInstance();
        loadOrders();
    }

    private void loadOrders() {
        db.collection("orders")
                .orderBy("orderDate", Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        orderList.clear();
                        for (QueryDocumentSnapshot doc : task.getResult()) {
                            try {
                                // --- DEFINITIVE FIX: Using robust, manual data parsing ---
                                OrderModel order = new OrderModel();
                                order.setOrderId(doc.getId());

                                if (doc.contains("phoneName")) {
                                    order.setPhoneName(doc.getString("phoneName"));
                                }
                                if (doc.contains("userEmail")) {
                                    order.setUserEmail(doc.getString("userEmail"));
                                }
                                if (doc.contains("userPhoneNumber")) {
                                    order.setUserPhoneNumber(doc.getString("userPhoneNumber"));
                                }
                                if (doc.contains("price")) {
                                    Object priceObject = doc.get("price");
                                    if (priceObject instanceof Number) {
                                        order.setPrice(((Number) priceObject).doubleValue());
                                    }
                                }
                                if (doc.contains("orderDate")) {
                                    order.setOrderDate(doc.getDate("orderDate"));
                                }

                                orderList.add(order);
                            } catch (Exception e) {
                                Log.e("FirestoreError", "Error parsing document " + doc.getId(), e);
                            }
                        }
                        adapter.notifyDataSetChanged();
                    } else {
                        Log.e("FirestoreError", "Error getting orders: ", task.getException());
                        Toast.makeText(this, "Failed to load orders", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
