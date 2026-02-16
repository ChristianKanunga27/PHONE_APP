package com.example.phone_shop_app;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class Settings extends AppCompatActivity {

    private TextInputEditText etName, etEmail, etPhone, etAddress;
    private MaterialButton btnSave;
    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Toolbar toolbar = findViewById(R.id.toolbar_settings);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        prefs = getSharedPreferences("user_prefs", MODE_PRIVATE);

        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        etPhone = findViewById(R.id.etPhone);
        etAddress = findViewById(R.id.etAddress);
        btnSave = findViewById(R.id.btnSaveSettings);

        // load saved data
        etName.setText(prefs.getString("name", ""));
        etEmail.setText(prefs.getString("email", ""));
        etPhone.setText(prefs.getString("phone", ""));
        etAddress.setText(prefs.getString("address", ""));

        btnSave.setOnClickListener(v -> {
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("name", etName.getText() != null ? etName.getText().toString() : "");
            editor.putString("email", etEmail.getText() != null ? etEmail.getText().toString() : "");
            editor.putString("phone", etPhone.getText() != null ? etPhone.getText().toString() : "");
            editor.putString("address", etAddress.getText() != null ? etAddress.getText().toString() : "");
            editor.apply();
            Toast.makeText(Settings.this, "Saved", Toast.LENGTH_SHORT).show();
            finish();
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
