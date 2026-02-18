package com.example.phone_shop_app;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.json.JSONObject;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class CreateAccount extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_create_account);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Set up Login click listener
        TextView textView2 = findViewById(R.id.textView2);
        textView2.setOnClickListener(v -> {
            Intent intent = new Intent(CreateAccount.this, Login.class);
            startActivity(intent);
        });
        Button registerBtn = findViewById(R.id.button);
        EditText edtEmail = findViewById(R.id.edtEmail);
        EditText edtPassword = findViewById(R.id.edtPassword);

        registerBtn.setOnClickListener(v -> {

            String email = edtEmail.getText().toString().trim();
            String password = edtPassword.getText().toString().trim();

            // 1. Add input validation
            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Email and password cannot be empty", Toast.LENGTH_SHORT).show();
                return;
            }

            // 2. Prevent the admin from creating an account via this form
            if ("kanungachristian@gmail.com".equalsIgnoreCase(email)) {
                Toast.makeText(this, "This email is reserved. Please use a different email.", Toast.LENGTH_LONG).show();
                return;
            }

            new Thread(() -> {
                try {

                    URL url = new URL("http://192.168.1.164:3000/register"); // Ensure this IP is correct for your network
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Content-Type", "application/json");
                    conn.setDoOutput(true);

                    JSONObject jsonParam = new JSONObject();
                    jsonParam.put("email", email);
                    jsonParam.put("password", password);

                    // Use try-with-resources for safer stream handling
                    try (OutputStream os = conn.getOutputStream()) {
                        os.write(jsonParam.toString().getBytes());
                        os.flush();
                    }

                    int responseCode = conn.getResponseCode();

                    runOnUiThread(() -> {
                        if (responseCode == 200) {
                            Toast.makeText(this, "Registered Successfully! Please log in.", Toast.LENGTH_LONG).show();
                            // 3. On success, navigate to the Login screen
                            Intent intent = new Intent(CreateAccount.this, Login.class);
                            startActivity(intent);
                            finish(); // Close this activity so the user can't go back to it
                        } else {
                            Toast.makeText(this, "Registration Failed. Please try again.", Toast.LENGTH_LONG).show();
                        }
                    });

                } catch (Exception e) {
                    e.printStackTrace();
                    runOnUiThread(() -> Toast.makeText(this, "An error occurred during registration.", Toast.LENGTH_SHORT).show());
                }
            }).start();
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
