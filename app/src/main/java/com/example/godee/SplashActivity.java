package com.example.godee;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.example.godee.Driver.Driver.DriverMapsActivity;
import com.example.godee.User.LoginPageActivity;
import com.example.godee.User.MapsActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

import java.util.Objects;

public class SplashActivity extends AppCompatActivity {
    private FirebaseAuth auth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);


        ImageView imageView = findViewById(R.id.imageView);
        Animation fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        imageView.startAnimation(fadeIn);
        Objects.requireNonNull(getSupportActionBar()).hide();

        int SPLASH_DISPLAY_LENGTH = 2000;
        new Handler().postDelayed(() -> {
//                Intent mainIntent = new Intent(SplashActivity.this, LoginPageActivity.class);
//                SplashActivity.this.startActivity(mainIntent);
//                SplashActivity.this.finish();
            auth = FirebaseAuth.getInstance();
            db = FirebaseFirestore.getInstance();

            if (auth.getCurrentUser() != null) {
                DocumentReference docRef = db.collection("users").document(auth.getCurrentUser().getUid());
                docRef.get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        if (Objects.requireNonNull(task.getResult()).exists()) {
                            Intent intent = new Intent(SplashActivity.this, MapsActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Intent intent = new Intent(SplashActivity.this, DriverMapsActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                });
            } else {
                Intent intent = new Intent(SplashActivity.this, LoginPageActivity.class);
                startActivity(intent);
                finish();
            }
        }, SPLASH_DISPLAY_LENGTH);
    }
}