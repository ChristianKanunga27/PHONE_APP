package com.example.phone_shop_app;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

public class AdminDashboard extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);

        toolbar = findViewById(R.id.adminToolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.admin_drawer);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.nav_open, R.string.nav_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = findViewById(R.id.admin_nav_view);
        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.nav_admin_dashboard) {
                // Already here
            } else if (id == R.id.nav_add_phone) {
                startActivity(new Intent(AdminDashboard.this, AddPhoneActivity.class));
            } else if (id == R.id.nav_manage_phones) {
                // startActivity(new Intent(AdminDashboard.this, ManagePhonesActivity.class));
            } else if (id == R.id.nav_admin_orders) {
                // startActivity(new Intent(AdminDashboard.this, Orders.class));
            } else if (id == R.id.nav_admin_logout) {
                startActivity(new Intent(AdminDashboard.this, Login.class));
                finish();
            }

            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // This line will now work correctly because the file exists.
        getMenuInflater().inflate(R.menu.admin_toolbar_menu, menu);
        return true;
    }

    // ✅ ADDED THIS METHOD TO HANDLE TOOLBAR CLICKS
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.toolbar_logout) {
            // Handle the toolbar logout click
            startActivity(new Intent(AdminDashboard.this, Login.class));
            finish(); // Close the dashboard
            return true;
        }

        return super.onOptionsItemSelected(item);
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
