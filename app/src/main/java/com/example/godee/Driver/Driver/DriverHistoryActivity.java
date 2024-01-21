package com.example.godee.Driver.Driver;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.godee.HistoryAdapter;
import com.example.godee.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;
import com.example.godee.Driver.Driver.ModelClass.DriveSession;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

public class DriverHistoryActivity extends AppCompatActivity {
    private FirebaseFirestore db;
    private RecyclerView recyclerView;
    private HistoryAdapter adapter;
    private List<DriveSession> driveSessionList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_history);

        getSupportActionBar().hide();

        // Initialize RecyclerView
        recyclerView = findViewById(R.id.historyRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        driveSessionList = new ArrayList<>();
        driveSessionList.add(new DriveSession("driverID1", "userID1", 12.345, 67.890, 23.456, 78.901, "Address 1", 300000));
        driveSessionList.add(new DriveSession("driverID2", "userID2", 34.567, 89.012, 45.678, 90.123, "Address 2", 300000));
        // Initialize Adapter
        adapter = new HistoryAdapter(driveSessionList);
        recyclerView.setAdapter(adapter);

        BottomNavigationView pageMenu = findViewById(R.id.page_navigation);
        pageMenu.setSelectedItemId(R.id.activity_history);
        pageNavigation(pageMenu);

        db = FirebaseFirestore.getInstance();
        fetchSessionsFromFirebase();
    }
    private void fetchSessionsFromFirebase() {
        db.collection("sessions").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                driveSessionList = new ArrayList<>();
                for (QueryDocumentSnapshot document : task.getResult()) {
                    DriveSession session = document.toObject(DriveSession.class);
                    driveSessionList.add(session);
                }
                updateRecyclerView();
            } else {
                // Handle errors
            }
        });
    }
    private void updateRecyclerView() {
        adapter = new HistoryAdapter(driveSessionList);
        recyclerView.setAdapter(adapter);
    }
    public void pageNavigation(BottomNavigationView pageMenu) {
        pageMenu.setOnItemSelectedListener(item ->
        {   int itemId = item.getItemId();
            if (itemId == R.id.activity_home){
                Intent intent = new Intent(getApplicationContext(), DriverMapsActivity.class);
                startActivity(intent);
                finish();
            }
            if (itemId == R.id.activity_history){
                return true;
            }
            if (itemId == R.id.activity_profile){
                Intent intent = new Intent(getApplicationContext(), DriverProfileActivity.class);
                startActivity(intent);
                finish();
            }
            // This part is expandable base on the number of tab need for the application
            /* Structure
               if (itemId == R.id.[name of the item]){
               Intent intent = new Intent(getApplicationContext(), [insert the activity associated with the icon].class);
               startActivity(intent);
               finish();}
             */
            return true;
        });
    }
}