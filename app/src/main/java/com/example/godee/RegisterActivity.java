package com.example.godee;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        getSupportActionBar().hide();

        TextView textView;
        EditText inputEmail, inputPassword, inputConfirmPassword;

        inputEmail = findViewById(R.id.email);
        inputPassword = findViewById(R.id.password);
        inputConfirmPassword = findViewById(R.id.passwordConfirmation);

        textView = findViewById(R.id.loginNow);
        Button registerButton = findViewById(R.id.btn_register);


        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), LoginPageActivity.class);
                startActivity(intent);
                finish();
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email, password, confirm;
                email = String.valueOf(inputEmail.getText());
                password = String.valueOf(inputPassword.getText());
                confirm = String.valueOf(inputConfirmPassword.getText());

                if (!(email.isEmpty()) && !(password.isEmpty()) && !(confirm.isEmpty()) && password.equals(confirm)) {
                    signUpFunction(email, password);
                }
                else{
                    Toast.makeText(RegisterActivity.this, "Fail to register!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    private void signUpFunction(String userEmail, String userPassword){
        FirebaseAuth userAccount = FirebaseAuth.getInstance();
        userAccount.createUserWithEmailAndPassword(userEmail, userPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    Toast.makeText(RegisterActivity.this, "Sign up account successfully", Toast.LENGTH_SHORT).show();
                    Intent backToLogin = new Intent(RegisterActivity.this, LoginPageActivity.class);
                    startActivity(backToLogin);
                    finish();
                }
            }
        });
    }
}