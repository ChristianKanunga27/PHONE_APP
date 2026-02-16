package com.example.phone_shop_app;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.button.MaterialButton;

public class Account extends AppCompatActivity {

    private TextView tvName, tvEmail, tvPhone, tvAddress;
    private MaterialButton btnEdit;
    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        Toolbar toolbar = findViewById(R.id.toolbar_account);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        prefs = getSharedPreferences("user_prefs", MODE_PRIVATE);

        tvName = findViewById(R.id.tvName);
        tvEmail = findViewById(R.id.tvEmail);
        tvPhone = findViewById(R.id.tvPhone);
        tvAddress = findViewById(R.id.tvAddress);
        btnEdit = findViewById(R.id.btnEditProfile);

        tvName.setText(prefs.getString("name", "Guest User"));
        tvEmail.setText(prefs.getString("email", ""));
        tvPhone.setText(prefs.getString("phone", ""));
        tvAddress.setText(prefs.getString("address", ""));

        btnEdit.setOnClickListener(v -> {
            startActivity(new Intent(Account.this, Settings.class));
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
