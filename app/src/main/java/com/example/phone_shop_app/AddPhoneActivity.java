package com.example.phone_shop_app;

import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.cloudinary.android.MediaManager;
import com.cloudinary.android.callback.ErrorInfo;
import com.cloudinary.android.callback.UploadCallback;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class AddPhoneActivity extends AppCompatActivity {

    private EditText edtName, edtDescription, edtPrice;
    private ImageView imgPhone;
    private Button btnSelectImage, btnAddPhone;
    private Uri selectedImageUri;

    private ActivityResultLauncher<String> pickImageLauncher;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_phone);

        edtName = findViewById(R.id.etPhoneName);
        edtDescription = findViewById(R.id.etDescription);
        edtPrice = findViewById(R.id.etPrice);
        imgPhone = findViewById(R.id.imgPhone);
        btnSelectImage = findViewById(R.id.btnSelectImage);
        btnAddPhone = findViewById(R.id.btnSavePhone);

        db = FirebaseFirestore.getInstance();

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
        btnAddPhone.setOnClickListener(v -> uploadPhone());
    }

    private void uploadPhone() {
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

        // Upload image to Cloudinary using an UNSIGNED preset
        MediaManager.get().upload(selectedImageUri)
                .unsigned("phones_preset") // <-- your unsigned upload preset name in Cloudinary
                .option("folder", "phones")
                .callback(new UploadCallback() {
                    @Override
                    public void onStart(String requestId) {
                        Toast.makeText(AddPhoneActivity.this, "Uploading image...", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onProgress(String requestId, long bytes, long totalBytes) { }

                    @Override
                    public void onSuccess(String requestId, Map resultData) {
                        String imageUrl = (String) resultData.get("secure_url");

                        // Save phone data to Firestore
                        Map<String, Object> phone = new HashMap<>();
                        phone.put("name", name);
                        phone.put("description", description);
                        phone.put("price", price);
                        phone.put("imageUrl", imageUrl);

                        db.collection("phones")
                                .add(phone)
                                .addOnSuccessListener(docRef ->
                                        Toast.makeText(AddPhoneActivity.this, "Phone added successfully!", Toast.LENGTH_LONG).show())
                                .addOnFailureListener(e ->
                                        Toast.makeText(AddPhoneActivity.this, "Failed to save phone: " + e.getMessage(), Toast.LENGTH_LONG).show());
                    }

                    @Override
                    public void onError(String requestId, ErrorInfo error) {
                        Toast.makeText(AddPhoneActivity.this, "Upload failed: " + error.toString(), Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onReschedule(String requestId, ErrorInfo error) { }
                })
                .dispatch();
    }
}
