package com.example.phone_shop_app;

import static androidx.core.content.ContextCompat.startActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.phone_shop_app.AdminDashboard;
import com.example.phone_shop_app.CreateAccount;
import com.example.phone_shop_app.Home;
import com.example.phone_shop_app.R;

public class Login extends AppCompatActivity {

    private static final String ADMIN_EMAIL = "kanungachristian@gmail.com";
    private static final String ADMIN_PASSWORD = "kanunga2727";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        EditText edtEmail = findViewById(R.id.etEmail);
        EditText edtPassword = findViewById(R.id.etPassword);
        Button loginBtn = findViewById(R.id.btnSignIn);
        TextView createAccountLink = findViewById(R.id.tvSignup);

        createAccountLink.setOnClickListener(v -> {
            Intent intent = new Intent(Login.this, CreateAccount.class);
            startActivity(intent);
        });

        loginBtn.setOnClickListener(v -> {

            String email = edtEmail.getText().toString().trim();
            String password = edtPassword.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please enter both email and password", Toast.LENGTH_SHORT).show();
                return;
            }

            if (ADMIN_EMAIL.equalsIgnoreCase(email) && ADMIN_PASSWORD.equals(password)) {

                Toast.makeText(this, "Admin Login Successful", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(Login.this, AdminDashboard.class));
                finish();

            } else {

                Toast.makeText(this, "User Login Successful", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(Login.this, Home.class));
                finish();
            }
        });
    }
}
