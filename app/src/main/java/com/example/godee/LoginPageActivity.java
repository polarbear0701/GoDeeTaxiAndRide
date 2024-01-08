package com.example.godee;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class LoginPageActivity extends AppCompatActivity {

    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

        user = mAuth.getCurrentUser();

        if (user != null) {
            user.getTenantId();
            Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
            startActivity(intent);
            finish();
        }

        TextView textView;
        Button loginButton;
        EditText emailSignIn, passwordSignIn;

        getSupportActionBar().hide();

        loginButton = findViewById(R.id.btn_login);
        textView = findViewById(R.id.registerNow);
        emailSignIn = findViewById(R.id.emailSignIn);
        passwordSignIn = findViewById(R.id.passwordSignIn);

        TextView tvDriverLogin = findViewById(R.id.tvDriverLogin);

        tvDriverLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginPageActivity.this, DriverLoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent returnUser = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(returnUser);
                finish();
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email, password;

                email = String.valueOf(emailSignIn.getText());
                password = String.valueOf(passwordSignIn.getText());

                FirebaseFirestore db = FirebaseFirestore.getInstance();
                DocumentReference documentReference = db.collection("users").document("EcWs461VgHPFZ6IKieTWp7rOkh23");
                documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        UserModel modelTemp = documentSnapshot.toObject(UserModel.class);
                        if (modelTemp.getAccountType() != 100){
                            Toast.makeText(LoginPageActivity.this, "You are a driver, please login in driver page!", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                                if (task.isSuccessful()){
                                    Toast.makeText(LoginPageActivity.this, "Login successfully!", Toast.LENGTH_SHORT).show();
                                    Intent toHomePage = new Intent(LoginPageActivity.this, MapsActivity.class);
                                    startActivity(toHomePage);
                                    finish();
                                }
                                else{
                                    Toast.makeText(LoginPageActivity.this, "Login failed!", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                });
            }
        });
    }
}