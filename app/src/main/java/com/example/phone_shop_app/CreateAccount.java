package com.example.phone_shop_app;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class CreateAccount extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_create_account);

        // Initialize Firebase Auth & Firestore
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Bind views with correct IDs
        Button registerBtn = findViewById(R.id.btnSignUp);
        EditText edtName = findViewById(R.id.etName);
        EditText edtEmail = findViewById(R.id.etEmail);
        EditText edtPassword = findViewById(R.id.etPassword);
        TextView loginText = findViewById(R.id.tvSignIn);

        // Click listener to go to Login screen
        loginText.setOnClickListener(v ->
                startActivity(new Intent(CreateAccount.this, Login.class)));

        // Registration logic
        registerBtn.setOnClickListener(v -> {

            String name = edtName.getText().toString().trim();
            String email = edtEmail.getText().toString().trim();
            String password = edtPassword.getText().toString().trim();

            // ✅ Input validation
            if (name.isEmpty()) {
                edtName.setError("Name is required");
                edtName.requestFocus();
                return;
            }

            if (email.isEmpty()) {
                edtEmail.setError("Email is required");
                edtEmail.requestFocus();
                return;
            }

            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                edtEmail.setError("Enter a valid email");
                edtEmail.requestFocus();
                return;
            }

            if (password.isEmpty()) {
                edtPassword.setError("Password is required");
                edtPassword.requestFocus();
                return;
            }

            if (password.length() < 6) {
                edtPassword.setError("Password must be at least 6 characters");
                edtPassword.requestFocus();
                return;
            }

            // Prevent admin email registration
            if ("kanungachristian@gmail.com".equalsIgnoreCase(email)) {
                Toast.makeText(this, "This email is reserved for Admin", Toast.LENGTH_LONG).show();
                return;
            }

            // 🔥 Create user with Firebase Auth
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {

                            // Get user ID
                            String userId = mAuth.getCurrentUser().getUid();

                            // Prepare user data
                            Map<String, Object> user = new HashMap<>();
                            user.put("name", name);
                            user.put("email", email);
                            user.put("role", "user"); // default role

                            // Save to Firestore
                            db.collection("users")
                                    .document(userId)
                                    .set(user)
                                    .addOnSuccessListener(aVoid -> {
                                        Toast.makeText(CreateAccount.this,
                                                "Registered Successfully!", Toast.LENGTH_LONG).show();

                                        // Go to Login screen
                                        Intent intent = new Intent(CreateAccount.this, Login.class);
                                        startActivity(intent);
                                        finish(); // close registration screen
                                    })
                                    .addOnFailureListener(e -> {
                                        Toast.makeText(CreateAccount.this,
                                                "Failed to save user data: " + e.getMessage(),
                                                Toast.LENGTH_LONG).show();
                                    });

                        } else {
                            Toast.makeText(CreateAccount.this,
                                    "Registration Failed: " +
                                            task.getException().getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                    });
        });
    }
}
