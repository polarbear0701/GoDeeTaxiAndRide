package com.example.godee.Driver.Driver;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.godee.R;
import com.example.godee.User.LoginPageActivity;
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


        Objects.requireNonNull(getSupportActionBar()).hide();
        TextView textView;
        textView = findViewById(R.id.registerNow);
        TextView tvBackToUserLogin = findViewById(R.id.tvBackToUserLogin);
        Button driverLoginButton = findViewById(R.id.btn_login_driver);
        ProgressBar progressBar = findViewById(R.id.progressBar);

        EditText emailSignIn = findViewById(R.id.emailSignIn);
        EditText passwordSignIn = findViewById(R.id.passwordSignIn);

        driverLoginButton.setOnClickListener(v -> {
            String emailText = emailSignIn.getText().toString();
            String passwordText = passwordSignIn.getText().toString();
            hideKeyboard(v);

            if (emailText.isEmpty() || passwordText.isEmpty()) {
                Toast.makeText(DriverLoginActivity.this, "Please fill in all the fields!", Toast.LENGTH_SHORT).show();
                return;
            }

            // ProgressBar
            progressBar.setVisibility(View.VISIBLE);

            auth.signInWithEmailAndPassword(emailText, passwordText).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    DocumentReference docRef = FirebaseFirestore.getInstance().collection("drivers").document(Objects.requireNonNull(auth.getCurrentUser()).getUid());
                    docRef.get().addOnCompleteListener(task1 -> {
                        if (task1.isSuccessful()) {
                            if (Objects.requireNonNull(task1.getResult()).exists()) {
                                Toast.makeText(DriverLoginActivity.this, "Welcome driver! Have a happy day.", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(DriverLoginActivity.this, DriverMapsActivity.class);
                                startActivity(intent);
                                finish();
                            } else {
                                Toast.makeText(DriverLoginActivity.this, "Not yet register as our driver!", Toast.LENGTH_SHORT).show();
                                auth.signOut();
                                progressBar.setVisibility(View.GONE);
                            }
                        }
                    });
                } else {
                    Toast.makeText(DriverLoginActivity.this, "Login Failed!", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                }
            });
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