package com.example.godee.Driver.Driver;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.godee.Driver.Driver.ModelClass.DriverModel;
import com.example.godee.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.Objects;

public class DriverInfoActivity extends AppCompatActivity {
    private String currentUserID;
    private FirebaseFirestore db; // Firestore instance

    // TextViews for displaying user information
    private TextView nameView, emailView, phoneNumberView, ageView, nationalityView, accountTypeView, ratingView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_info);

        getSupportActionBar().hide();

        currentUserID = getIntent().getStringExtra("CURRENT_USER_ID");
        Log.d("DriverInfoCurrentId", currentUserID);
        db = FirebaseFirestore.getInstance(); // Initialize Firestore

        // Initialize TextViews
        nameView = findViewById(R.id.driver_name);
        emailView = findViewById(R.id.driver_email);
        phoneNumberView = findViewById(R.id.driver_phone_number);
        ageView = findViewById(R.id.driver_age);
        nationalityView = findViewById(R.id.driver_nationality);
        accountTypeView = findViewById(R.id.driver_account_type);
        ratingView = findViewById(R.id.driver_rating);

        Button backBtn = findViewById(R.id.driver_info_backBtn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), DriverProfileActivity.class);
                startActivity(intent);
            }
        });

        fetchUserData();
    }

    private void fetchUserData() {
        db.collection("drivers").document(currentUserID)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            DriverModel driverModel = documentSnapshot.toObject(DriverModel.class);

                            // Display user data using driverModel's getters
                            assert driverModel != null;
                            nameView.setText(driverModel.getName());
                            emailView.setText(driverModel.getEmail());
                            phoneNumberView.setText(driverModel.getPhoneNumber());
                            ageView.setText(String.valueOf(driverModel.getAge()));
                            nationalityView.setText(driverModel.getNationality());
                            //accountTypeView.setText(String.valueOf(userModel.getAccountType()));
                            ratingView.setText(String.valueOf(driverModel.getRating()));

                        } else {
                            // Handle case where user does not exist
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Handle any errors
                    }
                });
    }

}
