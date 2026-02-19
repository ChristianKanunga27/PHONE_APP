package com.example.phone_shop_app;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class AddPhoneActivity extends AppCompatActivity {

    private EditText edtName, edtDescription, edtPrice;
    private ImageView imgPhone;
    private Button btnSelectImage, btnAddPhone;

    private Uri selectedImageUri;
    private ActivityResultLauncher<String> pickImageLauncher;

    private FirebaseFirestore db;
    private StorageReference storageRef;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_phone);

        // ⚡ Fix: Use XML IDs exactly
        edtName = findViewById(R.id.etPhoneName);        // was edtPhoneName
        edtDescription = findViewById(R.id.etDescription);
        edtPrice = findViewById(R.id.etPrice);
        imgPhone = findViewById(R.id.imgPhone);
        btnSelectImage = findViewById(R.id.btnSelectImage);
        btnAddPhone = findViewById(R.id.btnSavePhone);  // Save button in XML

        // Firebase
        db = FirebaseFirestore.getInstance();
        storageRef = FirebaseStorage.getInstance().getReference();

        // Image picker
        pickImageLauncher = registerForActivityResult(
                new ActivityResultContracts.GetContent(),
                uri -> {
                    if (uri != null) {
                        selectedImageUri = uri;
                        imgPhone.setImageURI(uri);
                    }
                }
        );

        btnSelectImage.setOnClickListener(v -> pickImageLauncher.launch("image/*"));
        btnAddPhone.setOnClickListener(v -> addPhone());
    }

    private void addPhone() {
        String name = edtName.getText().toString().trim();
        String description = edtDescription.getText().toString().trim();
        String priceStr = edtPrice.getText().toString().trim();

        if (name.isEmpty() || description.isEmpty() || priceStr.isEmpty() || selectedImageUri == null) {
            Toast.makeText(this, "Fill all fields and select an image", Toast.LENGTH_SHORT).show();
            return;
        }

        double price;
        try {
            price = Double.parseDouble(priceStr);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Enter a valid price", Toast.LENGTH_SHORT).show();
            return;
        }

        String filename = "phones/" + System.currentTimeMillis() + ".jpg";
        StorageReference phoneRef = storageRef.child(filename);

        phoneRef.putFile(selectedImageUri)
                .addOnSuccessListener(taskSnapshot ->
                        phoneRef.getDownloadUrl().addOnSuccessListener(uri -> {
                            PhoneModel phone = new PhoneModel(name, description, price, uri.toString());
                            db.collection("phones")
                                    .add(phone)
                                    .addOnSuccessListener(docRef ->
                                            Toast.makeText(this, "Phone added successfully", Toast.LENGTH_LONG).show())
                                    .addOnFailureListener(e ->
                                            Toast.makeText(this, "Failed: " + e.getMessage(), Toast.LENGTH_LONG).show());
                        }))
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Upload failed: " + e.getMessage(), Toast.LENGTH_LONG).show());
    }
}
