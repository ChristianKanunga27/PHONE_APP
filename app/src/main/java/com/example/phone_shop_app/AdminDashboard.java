package com.example.phone_shop_app;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

// The class name is now corrected to match the file name 'AdminDashboard.java'
public class AdminDashboard extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);

        Button btnAddPhone = findViewById(R.id.btnAddPhone);
        Button btnViewOrders = findViewById(R.id.btnViewOrders);

        // Set listener for the "Add New Phone" button
        btnAddPhone.setOnClickListener(v -> {
            // TODO: Create a new Activity called 'AddPhoneActivity' to handle this.
            // This new activity will have a form with fields for:
            // - Phone Picture (using an ImageView and an Image Picker)
            // - Phone Price (EditText)
            // - Phone Description (EditText)
            // - A 'Save' button to send the data to your server.
            //
            // Intent intent = new Intent(AdminDashboard.this, AddPhoneActivity.class);
            // startActivity(intent);

            Toast.makeText(this, "TODO: Open 'Add Phone' screen", Toast.LENGTH_SHORT).show();
        });

        // Set listener for the "View Client Orders" button
        btnViewOrders.setOnClickListener(v -> {
            // TODO: Create a new Activity called 'ViewOrdersActivity'.
            // This new activity will fetch and display a list of orders from your backend.
            //
            // Intent intent = new Intent(AdminDashboard.this, ViewOrdersActivity.class);
            // startActivity(intent);

            Toast.makeText(this, "TODO: Open 'View Orders' screen", Toast.LENGTH_SHORT).show();
        });
    }
}
