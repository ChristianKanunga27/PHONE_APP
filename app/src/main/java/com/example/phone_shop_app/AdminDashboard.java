package com.example.phone_shop_app;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.card.MaterialCardView;
import com.google.android.material.navigation.NavigationView;

public class AdminDashboard extends AppCompatActivity {

    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);

        // Toolbar
        Toolbar toolbar = findViewById(R.id.adminToolbar);
        setSupportActionBar(toolbar);

        // Drawer Layout
        drawerLayout = findViewById(R.id.admin_drawer);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.nav_open, R.string.nav_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        // Navigation View
        NavigationView navigationView = findViewById(R.id.admin_nav_view);
        navigationView.setNavigationItemSelectedListener(this::onNavigationItemSelected);

        // Card Click Listeners
        MaterialCardView cardManagePhones = findViewById(R.id.cardManagePhones);
        cardManagePhones.setOnClickListener(v -> 
                startActivity(new Intent(this, ManagePhonesActivity.class)));

        MaterialCardView cardViewOrders = findViewById(R.id.cardViewOrders);
        cardViewOrders.setOnClickListener(v -> 
                startActivity(new Intent(this, OrdersActivity.class)));
    }

    private boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_add_phone) {
            startActivity(new Intent(this, AddPhoneActivity.class));
        } else if (id == R.id.nav_manage_phones) {
            startActivity(new Intent(this, ManagePhonesActivity.class));
        } else if (id == R.id.nav_admin_orders) {
            startActivity(new Intent(this, OrdersActivity.class));
        } else if (id == R.id.nav_admin_logout) {
            startActivity(new Intent(this, Login.class));
            finish(); // Close the dashboard
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}
