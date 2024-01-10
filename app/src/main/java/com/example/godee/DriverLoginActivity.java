package com.example.godee;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.app.ProgressDialog;

import com.example.godee.ModelClass.DriverModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

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

        Objects.requireNonNull(getSupportActionBar()).hide();
        TextView textView;
        textView = findViewById(R.id.registerNow);
        TextView tvBackToUserLogin = findViewById(R.id.tvBackToUserLogin);
        Button driverLoginButton = findViewById(R.id.btn_login_driver);

        EditText emailSignIn = findViewById(R.id.emailSignIn);
        EditText passwordSignIn = findViewById(R.id.passwordSignIn);

        driverLoginButton.setOnClickListener(v -> {
            ProgressDialog progressDialog = new ProgressDialog(DriverLoginActivity.this);
            progressDialog.setMessage("Logging in...");
            progressDialog.setCancelable(false);
            progressDialog.show();

            String emailText = String.valueOf(emailSignIn.getText());
            String passwordText = String.valueOf(passwordSignIn.getText());
            hideKeyboard(v);

            if ((emailText.isEmpty()) || (passwordText.isEmpty())) {
                Toast.makeText(DriverLoginActivity.this, "Please fill in all the fields!", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss(); // Dismiss the dialog if fields are empty
            } else {
                auth.signInWithEmailAndPassword(emailText, passwordText).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentReference docRef = FirebaseFirestore.getInstance().collection("drivers").document(Objects.requireNonNull(auth.getCurrentUser()).getUid());
                        docRef.get().addOnSuccessListener(documentSnapshot -> {
                            DriverModel driverModelTemp = documentSnapshot.toObject(DriverModel.class);
                            assert driverModelTemp != null;
                            if (driverModelTemp.getAccountType() != 200) {
                                Toast.makeText(DriverLoginActivity.this, "You are a user, please login in user page!", Toast.LENGTH_SHORT).show();
                                auth.signOut();
                            } else {
                                Toast.makeText(DriverLoginActivity.this, "Welcome Driver!!", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(DriverLoginActivity.this, DriverMapsActivity.class);
                                startActivity(intent);
                                finish();
                            }
                            progressDialog.dismiss(); // Dismiss the dialog after processing
                        });
                    } else {
                        Toast.makeText(DriverLoginActivity.this, "Login Failed!", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss(); // Dismiss the dialog on failure
                    }
                });
            }
        });

        tvBackToUserLogin.setOnClickListener(view -> {
            Intent intent = new Intent(DriverLoginActivity.this, LoginPageActivity.class);
            startActivity(intent);
            finish();
        });

        textView.setOnClickListener(v -> {
            Intent returnUser = new Intent(getApplicationContext(), DriverRegisterActivity.class);
            startActivity(returnUser);
            finish();
        });


    }

    private void hideKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

}