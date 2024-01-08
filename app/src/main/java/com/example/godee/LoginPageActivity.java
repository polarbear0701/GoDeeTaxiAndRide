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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
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

        Objects.requireNonNull(getSupportActionBar()).hide();

        loginButton = findViewById(R.id.btn_login);
        textView = findViewById(R.id.registerNow);
        emailSignIn = findViewById(R.id.emailSignIn);
        passwordSignIn = findViewById(R.id.passwordSignIn);

        TextView tvDriverLogin = findViewById(R.id.tvDriverLogin);

        tvDriverLogin.setOnClickListener(v -> {
            Intent intent = new Intent(LoginPageActivity.this, DriverLoginActivity.class);
            startActivity(intent);
            finish();
        });

        textView.setOnClickListener(v -> {
            Intent returnUser = new Intent(getApplicationContext(), RegisterActivity.class);
            startActivity(returnUser);
            finish();
        });

        loginButton.setOnClickListener(v -> {
            String email;
            String password;
            hideKeyboard(v);


            email = String.valueOf(emailSignIn.getText());
            password = String.valueOf(passwordSignIn.getText());

            if (email.isEmpty() || password.isEmpty()){
                //Toast empty
                Toast.makeText(LoginPageActivity.this, "Please fill in all the fields!", Toast.LENGTH_SHORT).show();
                return;
            }


            FirebaseFirestore db = FirebaseFirestore.getInstance();
            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                if (task.isSuccessful()){

                    DocumentReference documentReference = db.collection("users").document(Objects.requireNonNull(mAuth.getUid()));
                    documentReference.get().addOnSuccessListener(documentSnapshot -> {
                        UserModel modelTemp = documentSnapshot.toObject(UserModel.class);
                        assert modelTemp != null;
                        if (modelTemp.getAccountType() != 100){
                            Toast.makeText(LoginPageActivity.this, "You are a driver, please login in driver page!", Toast.LENGTH_SHORT).show();
                            mAuth.signOut();
                        }
                        else{
                            Toast.makeText(LoginPageActivity.this, "Login successfully!", Toast.LENGTH_SHORT).show();
                            Intent toHomePage = new Intent(LoginPageActivity.this, MapsActivity.class);
                            startActivity(toHomePage);
                            finish();
                        }
                    });

                }
                else{
                    Toast.makeText(LoginPageActivity.this, "Login failed!", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

    private void hideKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

}