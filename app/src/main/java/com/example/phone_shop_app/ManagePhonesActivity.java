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

// Implement the listener interface from the new adapter
public class ManagePhonesActivity extends AppCompatActivity implements ManagePhoneAdapter.OnPhoneListener {

    private RecyclerView recyclerView;
    private List<PhoneModel> phoneList = new ArrayList<>();
    private FirebaseFirestore db;
    private ManagePhoneAdapter adapter; // Use the new ManagePhoneAdapter

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_phones);

        recyclerView = findViewById(R.id.recyclerViewPhones);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        db = FirebaseFirestore.getInstance();

        // Initialize the new adapter, passing 'this' as the listener
        adapter = new ManagePhoneAdapter(this, phoneList, this);
        recyclerView.setAdapter(adapter);

        loadPhones();
    }

    private void loadPhones() {
        db.collection("phones")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        phoneList.clear();
                        for (QueryDocumentSnapshot doc : task.getResult()) {
                            try {
                                PhoneModel phone = doc.toObject(PhoneModel.class);
                                phone.setId(doc.getId());
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

    @Override
    public void onEditClicked(PhoneModel phone) {
        showEditDialog(phone);
    }

    @Override
    public void onDeleteClicked(PhoneModel phone) {
        showDeleteConfirmationDialog(phone);
    }

    private void showDeleteConfirmationDialog(PhoneModel phone) {
        new AlertDialog.Builder(this)
                .setTitle("Delete Phone")
                .setMessage("Are you sure you want to delete '" + phone.getName() + "'?")
                .setPositiveButton("Delete", (dialog, which) -> deletePhone(phone))
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void deletePhone(PhoneModel phone) {
        db.collection("phones").document(phone.getId())
                .delete()
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Phone deleted successfully", Toast.LENGTH_SHORT).show();
                    loadPhones(); // Refresh the list
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Error deleting phone: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    public void showEditDialog(PhoneModel phone) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Edit Phone");

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        int padding = (int) (16 * getResources().getDisplayMetrics().density);
        layout.setPadding(padding, padding, padding, padding);

        final EditText inputName = new EditText(this);
        inputName.setHint("Name");
        inputName.setText(phone.getName());
        layout.addView(inputName);

        final EditText inputDesc = new EditText(this);
        inputDesc.setHint("Description");
        inputDesc.setText(phone.getDescription());
        layout.addView(inputDesc);

        final EditText inputPrice = new EditText(this);
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

            DocumentReference docRef = db.collection("phones").document(phone.getId());
            docRef.update("name", newName, "description", newDesc, "price", newPrice)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(this, "Phone updated", Toast.LENGTH_SHORT).show();
                        loadPhones();
                    })
                    .addOnFailureListener(e -> Toast.makeText(this, "Update failed: " + e.getMessage(), Toast.LENGTH_SHORT).show());
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());
        builder.show();
    }
}
