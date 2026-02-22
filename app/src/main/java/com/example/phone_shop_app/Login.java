package com.example.phone_shop_app;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class Login extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    // --- Admin Credentials ---
    private static final String ADMIN_EMAIL = "christiankanunga@gmail.com";
    private static final String ADMIN_PASSWORD = "kanunga@2727";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        EditText edtEmail = findViewById(R.id.etEmail);
        EditText edtPassword = findViewById(R.id.etPassword);
        Button loginBtn = findViewById(R.id.btnSignIn);
        TextView createAccountLink = findViewById(R.id.tvSignup);

        // Go to Registration screen
        createAccountLink.setOnClickListener(v ->
                startActivity(new Intent(Login.this, CreateAccount.class)));

        loginBtn.setOnClickListener(v -> {

            String email = edtEmail.getText().toString().trim();
            String password = edtPassword.getText().toString().trim();

            // ✅ Input validation
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

            // ✅ --- ADDED: Special check for hardcoded admin credentials ---
            if (ADMIN_EMAIL.equalsIgnoreCase(email) && ADMIN_PASSWORD.equals(password)) {
                Toast.makeText(Login.this, "Admin Login Successful", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Login.this, AdminDashboard.class);
                startActivity(intent);
                finish(); // Close the login activity
                return; // Stop further execution
            }


            // 🔥 Firebase Authentication Login (for all other users)
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {

                            // Get user ID
                            String userId = mAuth.getCurrentUser().getUid();

                            // Get user document from Firestore
                            db.collection("users")
                                    .document(userId)
                                    .get()
                                    .addOnSuccessListener(documentSnapshot -> {
                                        if (documentSnapshot.exists()) {

                                            String role = documentSnapshot.getString("role");

                                            if ("admin".equalsIgnoreCase(role)) {
                                                Toast.makeText(Login.this,
                                                        "Admin Login Successful", Toast.LENGTH_SHORT).show();
                                                startActivity(new Intent(Login.this, AdminDashboard.class));
                                            } else {
                                                Toast.makeText(Login.this,
                                                        "User Login Successful", Toast.LENGTH_SHORT).show();
                                                startActivity(new Intent(Login.this, Home.class));
                                            }

                                            finish();

                                        } else {
                                            // User doc not found, treat as regular user
                                            Toast.makeText(Login.this,
                                                    "Login Successful", Toast.LENGTH_SHORT).show();
                                            startActivity(new Intent(Login.this, Home.class));
                                            finish();
                                        }
                                    })
                                    .addOnFailureListener(e -> Toast.makeText(Login.this,
                                            "Error fetching user role: " + e.getMessage(),
                                            Toast.LENGTH_LONG).show());

                        } else {
                            Toast.makeText(Login.this,
                                    "Login Failed: " + task.getException().getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                    });
        });
    }
}
