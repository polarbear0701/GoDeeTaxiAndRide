package com.example.godee.User;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.example.godee.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.Objects;

public class UserInfoActivity extends AppCompatActivity {
    private String currentUserID;
    private FirebaseFirestore db; // Firestore instance

    // TextViews for displaying user information
    private TextView nameView, emailView, phoneNumberView, ageView, nationalityView, accountTypeView, ratingView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);

        getSupportActionBar().hide();

        currentUserID = getIntent().getStringExtra("CURRENT_USER_ID");
        Log.d("UserInfoCurrentId", currentUserID);
        db = FirebaseFirestore.getInstance(); // Initialize Firestore

        // Initialize TextViews
        nameView = findViewById(R.id.name);
        emailView = findViewById(R.id.email);
        phoneNumberView = findViewById(R.id.phone_number);
        ageView = findViewById(R.id.age);
        nationalityView = findViewById(R.id.nationality);
        accountTypeView = findViewById(R.id.account_type);
        ratingView = findViewById(R.id.rating);

        Button backBtn = findViewById(R.id.info_backBtn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
                startActivity(intent);
            }
        });

        fetchUserData();
    }

    private void fetchUserData() {
        db.collection("users").document(currentUserID)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            UserModel userModel = documentSnapshot.toObject(UserModel.class);

                            // Display user data using UserModel's getters
                            assert userModel != null;
                            nameView.setText(userModel.getName());
                            emailView.setText(userModel.getEmail());
                            phoneNumberView.setText(userModel.getPhoneNumber());
                            ageView.setText(String.valueOf(userModel.getAge()));
                            nationalityView.setText(userModel.getNationality());
                            //accountTypeView.setText(String.valueOf(userModel.getAccountType()));
                            ratingView.setText(String.valueOf(userModel.getRating()));

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
