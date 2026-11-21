package com.uol.lostfound;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class PostItemActivity extends AppCompatActivity {

    private EditText etName, etLocation, etContact;
    private RadioGroup rgType;
    private Button btnPost;
    private ImageButton btnBack;
    private DatabaseReference dbRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_item);

        dbRef = FirebaseDatabase.getInstance().getReference("LostFound");

        etName = findViewById(R.id.etName);
        etLocation = findViewById(R.id.etLocation);
        etContact = findViewById(R.id.etContact);
        rgType = findViewById(R.id.rgType);
        btnPost = findViewById(R.id.btnPost);
        btnBack = findViewById(R.id.btnBack);

        // Make the back button work
        btnBack.setOnClickListener(v -> {
            finish(); // Closes this activity and returns to MainActivity
        });

        btnPost.setOnClickListener(v -> {
            String name = etName.getText().toString().trim();
            String location = etLocation.getText().toString().trim();
            String contact = etContact.getText().toString().trim();
            
            if (TextUtils.isEmpty(name)) {
                etName.setError("Name is required");
                return;
            }
            if (TextUtils.isEmpty(location)) {
                etLocation.setError("Location is required");
                return;
            }
            if (TextUtils.isEmpty(contact)) {
                etContact.setError("Contact is required");
                return;
            }
            if (rgType.getCheckedRadioButtonId() == -1) {
                Toast.makeText(PostItemActivity.this, "Select Lost or Found", Toast.LENGTH_SHORT).show();
                return;
            }

            String type = (rgType.getCheckedRadioButtonId() == R.id.rbLost) ? "Lost" : "Found";

            LostItem newItem = new LostItem("", name, type, location, contact, "Open");

            String key = dbRef.push().getKey();
            if (key != null) {
                newItem.setId(key);
                
                dbRef.child(key).setValue(newItem).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(PostItemActivity.this, "Item Posted!", Toast.LENGTH_SHORT).show();
                        finish(); // Go back to the board
                    } else {
                        Toast.makeText(PostItemActivity.this, "Failed: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }
}
