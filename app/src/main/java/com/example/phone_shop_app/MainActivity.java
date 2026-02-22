package com.example.phone_shop_app;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // The EdgeToEdge and ViewCompat code has been removed to prevent potential startup crashes.
        // The core functionality of this splash screen remains.

        // 🔹 Move to Login after 2 seconds
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            Intent intent = new Intent(MainActivity.this, Login.class);
            startActivity(intent);
            finish(); // close MainActivity
        }, 2000); // 2000 milliseconds = 2 seconds
    }
}
