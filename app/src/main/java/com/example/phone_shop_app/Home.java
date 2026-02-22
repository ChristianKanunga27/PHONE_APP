package com.example.phone_shop_app;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class Home extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private FirebaseFirestore db;

    private List<PhoneModel> phoneList;
    private PhoneAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Toolbar and Drawer
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.nav_open, R.string.nav_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(menuItem -> {
            int id = menuItem.getItemId();
            if (id == R.id.nav_new_phone) {
                startActivity(new Intent(Home.this, NewPhone.class));
            } else if (id == R.id.nav_my_orders) { // Added this block
                startActivity(new Intent(Home.this, MyOrdersActivity.class));
            } else if (id == R.id.nav_account) {
                startActivity(new Intent(Home.this, Account.class));
            } else if (id == R.id.nav_settings) {
                startActivity(new Intent(Home.this, Settings.class));
            } else if (id == R.id.nav_logout) {
                Intent intent = new Intent(Home.this, Login.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        });

        // RecyclerView
        recyclerView = findViewById(R.id.recyclerViewPhones);
        progressBar = findViewById(R.id.progressBar);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        phoneList = new ArrayList<>();
        adapter = new PhoneAdapter(this, phoneList);
        recyclerView.setAdapter(adapter);

        // Firestore
        db = FirebaseFirestore.getInstance();
        loadPhones();
    }

    private void loadPhones() {
        progressBar.setVisibility(View.VISIBLE);
        db.collection("phones")
                .get()
                .addOnCompleteListener(task -> {
                    progressBar.setVisibility(View.GONE);
                    if (task.isSuccessful()) {
                        phoneList.clear();
                        for (QueryDocumentSnapshot doc : task.getResult()) {
                            try {
                                // --- Manual and Safe Data Parsing ---
                                PhoneModel phone = new PhoneModel();
                                phone.setId(doc.getId());

                                if (doc.contains("name")) {
                                    phone.setName(doc.getString("name"));
                                }
                                if (doc.contains("description")) {
                                    phone.setDescription(doc.getString("description"));
                                }
                                if (doc.contains("imageUrl")) {
                                    phone.setImageUrl(doc.getString("imageUrl"));
                                }

                                // Safely get the price, handling different number types
                                if (doc.contains("price")) {
                                    Object priceObject = doc.get("price");
                                    if (priceObject instanceof Number) {
                                        phone.setPrice(((Number) priceObject).doubleValue());
                                    }
                                }
                                phoneList.add(phone);
                            } catch (Exception e) {
                                Log.e("FirestoreError", "Error parsing document " + doc.getId(), e);
                            }
                        }
                        adapter.notifyDataSetChanged();
                    } else {
                        Log.e("FirestoreError", "Error getting documents: ", task.getException());
                        Toast.makeText(Home.this, "Failed to load phones.", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
