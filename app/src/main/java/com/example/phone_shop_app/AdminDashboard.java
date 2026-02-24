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

public class AdminDashboard extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);

        // Toolbar
        toolbar = findViewById(R.id.adminToolbar);
        setSupportActionBar(toolbar);

        // DrawerLayout
        drawerLayout = findViewById(R.id.admin_drawer);

        // Toggle (hamburger icon)
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this,
                drawerLayout,
                toolbar,
                R.string.nav_open,
                R.string.nav_close
        );

        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        // NavigationView
        navigationView = findViewById(R.id.admin_nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    // =========================
    // Toolbar Menu
    // =========================
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.admin_toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        // Let drawer toggle handle first
        if (drawerLayout != null &&
                new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                        R.string.nav_open, R.string.nav_close)
                        .onOptionsItemSelected(item)) {
            return true;
        }

        int id = item.getItemId();

        if (id == R.id.toolbar_logout) {
            startActivity(new Intent(this, Login.class));
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // =========================
    // Drawer Menu Clicks
    // =========================
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.nav_admin_dashboard) {
            // Already here

        } else if (id == R.id.nav_add_phone) {
            startActivity(new Intent(this, AddPhoneActivity.class));

        } else if (id == R.id.nav_manage_phones) {
            // startActivity(new Intent(this, ManagePhonesActivity.class));

        } else if (id == R.id.nav_admin_orders) {
            // startActivity(new Intent(this, OrdersActivity.class));

        } else if (id == R.id.nav_admin_logout) {
            startActivity(new Intent(this, Login.class));
            finish();
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    // =========================
    // Back Button Handling
    // =========================
    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}
