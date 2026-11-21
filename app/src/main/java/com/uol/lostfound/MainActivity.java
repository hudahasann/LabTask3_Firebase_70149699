package com.uol.lostfound;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private LinearLayout emptyState;
    private Button btnOpenPostForm;
    private LostItemAdapter adapter;
    private List<LostItem> itemList;
    private DatabaseReference dbRef;
    private ValueEventListener valueEventListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        emptyState = findViewById(R.id.emptyState);
        btnOpenPostForm = findViewById(R.id.btnOpenPostForm);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        itemList = new ArrayList<>();

        dbRef = FirebaseDatabase.getInstance().getReference("LostFound");

        adapter = new LostItemAdapter(itemList, item -> {
            if (item.getId() != null && !"Returned".equalsIgnoreCase(item.getStatus())) {
                dbRef.child(item.getId()).child("status").setValue("Returned")
                        .addOnSuccessListener(unused ->
                                Toast.makeText(MainActivity.this, "Marked as Returned", Toast.LENGTH_SHORT).show())
                        .addOnFailureListener(e ->
                                Toast.makeText(MainActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show());
            }
        });

        recyclerView.setAdapter(adapter);

        btnOpenPostForm.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, PostItemActivity.class);
            startActivity(intent);
        });

        valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                itemList.clear();
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    LostItem item = postSnapshot.getValue(LostItem.class);
                    if (item != null) {
                        itemList.add(item);
                    }
                }
                Collections.reverse(itemList);
                adapter.notifyDataSetChanged();

                if (itemList.isEmpty()) {
                    emptyState.setVisibility(View.VISIBLE);
                } else {
                    emptyState.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivity.this, "Database Error: " + error.getMessage(), Toast.LENGTH_LONG).show();
            }
        };

        dbRef.addValueEventListener(valueEventListener);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (dbRef != null && valueEventListener != null) {
            dbRef.removeEventListener(valueEventListener);
        }
    }
}
