package com.example.godee;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class DriverLoginActivity extends AppCompatActivity {

    FirebaseAuth auth;

    FirebaseUser driver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_login);

        auth = FirebaseAuth.getInstance();
        driver = auth.getCurrentUser();

        if (driver != null) {
            driver.getTenantId();
            Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
            startActivity(intent);
            finish();
        }

        getSupportActionBar().hide();
        TextView textView;
        textView = findViewById(R.id.registerNow);
        TextView tvBackToUserLogin = findViewById(R.id.tvBackToUserLogin);

        tvBackToUserLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DriverLoginActivity.this, LoginPageActivity.class);
                startActivity(intent);
            }
        });

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent returnUser = new Intent(getApplicationContext(), DriverRegisterActivity.class);
                startActivity(returnUser);
                finish();
            }
        });
    }
}