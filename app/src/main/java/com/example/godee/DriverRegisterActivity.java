package com.example.godee;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.material.textfield.TextInputEditText;

public class DriverRegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_register);
        getSupportActionBar().hide();

        TextInputEditText email = findViewById(R.id.email);
        TextInputEditText password = findViewById(R.id.password);
        TextInputEditText confirm = findViewById(R.id.passwordConfirmation);
        TextInputEditText name = findViewById(R.id.name);
        TextInputEditText phone = findViewById(R.id.phoneNumber);
        TextInputEditText age = findViewById(R.id.age);
        TextInputEditText nationality = findViewById(R.id.nationality);

        Button driverRegisterButton = findViewById(R.id.btn_register_driver);

        driverRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}