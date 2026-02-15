package com.example.phone_shop_app;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class Login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Set up Sign Up click listener
        TextView tvSignup = findViewById(R.id.tvSignup);
        tvSignup.setOnClickListener(v -> {
            Intent intent = new Intent(Login.this, CreateAccount.class);
            startActivity(intent);
        });

        // Set up Forgot Password click listener
        TextView tvForgot = findViewById(R.id.tvForgot);
        tvForgot.setOnClickListener(v -> {
            Intent intent = new Intent(Login.this, ForgetPassword.class);
            startActivity(intent);
        });

        // Set up Sign In button click listener
        Button btnSignIn = findViewById(R.id.btnSignIn);
        btnSignIn.setOnClickListener(v -> {
            // Add your login logic here
            Intent intent = new Intent(Login.this, Home.class);
            startActivity(intent);
        });
    }
}
