package com.example.phone_shop_app;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
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

        adapter = new PhoneAdapter(this, phoneList);
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
                            PhoneModel phone = doc.toObject(PhoneModel.class);
                            phone.id = doc.getId();
                            phoneList.add(phone);
                        }
                        adapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(this, "Failed to load phones", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void showEditDialog(PhoneModel phone) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Edit Phone");

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(50, 40, 50, 10);

        EditText inputName = new EditText(this);
        inputName.setHint("Name");
        inputName.setText(phone.name);
        layout.addView(inputName);

        EditText inputDesc = new EditText(this);
        inputDesc.setHint("Description");
        inputDesc.setText(phone.description);
        layout.addView(inputDesc);

        EditText inputPrice = new EditText(this);
        inputPrice.setHint("Price");
        inputPrice.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        inputPrice.setText(String.valueOf(phone.price));
        layout.addView(inputPrice);

        builder.setView(layout);

        builder.setPositiveButton("Save", (dialog, which) -> {
            String newName = inputName.getText().toString().trim();
            String newDesc = inputDesc.getText().toString().trim();
            double newPrice = Double.parseDouble(inputPrice.getText().toString().trim());

            DocumentReference docRef = db.collection("phones").document(phone.id);
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

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

        builder.show();
    }
}
