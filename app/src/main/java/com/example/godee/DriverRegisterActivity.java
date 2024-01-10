package com.example.godee;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.godee.ModelClass.DriverModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class DriverRegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_register);
        Objects.requireNonNull(getSupportActionBar()).hide();

        TextInputEditText email = findViewById(R.id.email);
        TextInputEditText password = findViewById(R.id.password);
        TextInputEditText confirm = findViewById(R.id.passwordConfirmation);
        TextInputEditText name = findViewById(R.id.name);
        TextInputEditText phone = findViewById(R.id.phoneNumber);
        TextInputEditText age = findViewById(R.id.age);
        TextInputEditText nationality = findViewById(R.id.nationality);

        Button driverRegisterButton = findViewById(R.id.btn_register_driver);

        TextView loginNow = findViewById(R.id.loginNow);
        loginNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DriverRegisterActivity.this, DriverLoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        driverRegisterButton.setOnClickListener(v -> {
            String emailText = String.valueOf(email.getText());
            String passwordText = String.valueOf(password.getText());
            String confirmText = String.valueOf(confirm.getText());
            String nameText = String.valueOf(name.getText());
            String phoneText = String.valueOf(phone.getText());
            int ageText;
            try {
                ageText = Integer.parseInt(String.valueOf(age.getText()));
            } catch (NumberFormatException e) {
                ageText = 0;
            }
            String nationalityText = String.valueOf(nationality.getText());

            DriverModel driverModel = new DriverModel(nameText, emailText, phoneText, ageText, nationalityText, 200);

            if (!(emailText.isEmpty()) && !(passwordText.isEmpty()) && !(confirmText.isEmpty()) && passwordText.equals(confirmText)) {
                signUpFunction(emailText, passwordText, driverModel);
            }
            else{
                Toast.makeText(DriverRegisterActivity.this, "Fail to register!", Toast.LENGTH_SHORT).show();
            }

        });
    }

    private void signUpFunction(String userEmail, String userPassword, DriverModel driverModel){
        FirebaseAuth driverAccount = FirebaseAuth.getInstance();
        FirebaseFirestore driverDataRegister = FirebaseFirestore.getInstance();
        driverAccount.createUserWithEmailAndPassword(userEmail, userPassword).addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                driverDataRegister.collection("drivers").document(Objects.requireNonNull(driverAccount.getCurrentUser()).getUid()).set(driverModel);
                Toast.makeText(DriverRegisterActivity.this, "Sign up as driver successfully", Toast.LENGTH_SHORT).show();
                Intent backToLogin = new Intent(DriverRegisterActivity.this, DriverLoginActivity.class);
                driverAccount.signOut();
                startActivity(backToLogin);
                finish();
            }
        });
    }

}