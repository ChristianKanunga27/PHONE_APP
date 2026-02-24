package com.example.phone_shop_app;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class ManagePhonesActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private List<PhoneModel> phoneList = new ArrayList<>();
    private FirebaseFirestore db;
    private PhoneAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_phones);

        recyclerView = findViewById(R.id.recyclerViewPhones);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        db = FirebaseFirestore.getInstance();

        // NOTE: You may need a different adapter that can handle edit/delete clicks
        // for the manage screen.
        adapter = new PhoneAdapter(this, phoneList);
        recyclerView.setAdapter(adapter);

        loadPhones();
    }

    private void loadPhones() {
        // FINAL FIX: Replaced brittle toObject() with robust, manual data parsing.
        db.collection("phones")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        phoneList.clear();
                        for (QueryDocumentSnapshot doc : task.getResult()) {
                            try {
                                PhoneModel phone = new PhoneModel();
                                phone.setId(doc.getId()); // Use the setter, as you correctly identified

                                if (doc.contains("name")) {
                                    phone.setName(doc.getString("name"));
                                }
                                if (doc.contains("description")) {
                                    phone.setDescription(doc.getString("description"));
                                }
                                if (doc.contains("imageUrl")) {
                                    phone.setImageUrl(doc.getString("imageUrl"));
                                }

                                // Safely get the price, handling different number types
                                if (doc.contains("price")) {
                                    Object priceObject = doc.get("price");
                                    if (priceObject instanceof Number) {
                                        phone.setPrice(((Number) priceObject).doubleValue());
                                    }
                                }
                                phoneList.add(phone);
                            } catch (Exception e) {
                                Log.e("FirestoreError", "Error parsing document " + doc.getId(), e);
                            }
                        }
                        adapter.notifyDataSetChanged();
                    } else {
                        Log.e("FirestoreError", "Error getting documents: ", task.getException());
                        Toast.makeText(this, "Failed to load phones", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void showEditDialog(PhoneModel phone) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Edit Phone");

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        int padding = (int) (16 * getResources().getDisplayMetrics().density);
        layout.setPadding(padding, padding, padding, padding);

        EditText inputName = new EditText(this);
        inputName.setHint("Name");
        inputName.setText(phone.getName());
        layout.addView(inputName);

        EditText inputDesc = new EditText(this);
        inputDesc.setHint("Description");
        inputDesc.setText(phone.getDescription());
        layout.addView(inputDesc);

        EditText inputPrice = new EditText(this);
        inputPrice.setHint("Price");
        inputPrice.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        inputPrice.setText(String.valueOf(phone.getPrice()));
        layout.addView(inputPrice);

        builder.setView(layout);

        builder.setPositiveButton("Save", (dialog, which) -> {
            String newName = inputName.getText().toString().trim();
            String newDesc = inputDesc.getText().toString().trim();
            double newPrice = 0;
            try {
                newPrice = Double.parseDouble(inputPrice.getText().toString().trim());
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Invalid price", Toast.LENGTH_SHORT).show();
                return;
            }

            // Correctly use the getter for the private 'id' field
            DocumentReference docRef = db.collection("phones").document(phone.getId());
            docRef.update(
                    "name", newName,
                    "description", newDesc,
                    "price", newPrice
            ).addOnSuccessListener(aVoid -> {
                Toast.makeText(this, "Phone updated", Toast.LENGTH_SHORT).show();
                loadPhones();
            }).addOnFailureListener(e ->
                    Toast.makeText(this, "Update failed: " + e.getMessage(), Toast.LENGTH_SHORT).show()
            );
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

        builder.show();
    }
}
