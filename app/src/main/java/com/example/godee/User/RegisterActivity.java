package com.example.godee.User;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.godee.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class RegisterActivity extends AppCompatActivity {
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        getSupportActionBar().hide();

        TextView textView;
        EditText inputEmail, inputPassword, inputConfirmPassword;
        EditText inputName, inputPhoneNumber, inputAge, inputNationality;
        EditText inputAddressNumber, inputStreet, inputWard, inputDistrict, inputCity, inputCountry;

        inputEmail = findViewById(R.id.email);
        inputPassword = findViewById(R.id.password);
        inputConfirmPassword = findViewById(R.id.passwordConfirmation);
        inputName = findViewById(R.id.name);
        inputPhoneNumber = findViewById(R.id.phoneNumber);
        inputAge = findViewById(R.id.age);
        inputNationality = findViewById(R.id.nationality);
        inputAddressNumber = findViewById(R.id.addressNumber);
        inputStreet = findViewById(R.id.addressStreet);
        inputWard = findViewById(R.id.addressWard);
        inputDistrict = findViewById(R.id.addressDistrict);
        inputCity = findViewById(R.id.addressCity);
        inputCountry = findViewById(R.id.addressCountry);


        textView = findViewById(R.id.loginNow);
        Button registerButton = findViewById(R.id.btn_register);
        progressBar = findViewById(R.id.progressBar);


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
                //credential registration
                String email, password, confirm;

                //User information registration
                String name, phone, nationality;
                int age;

                //User address registration
                String addressNumber, street, ward, district, city, country;

                email = String.valueOf(inputEmail.getText());
                password = String.valueOf(inputPassword.getText());
                confirm = String.valueOf(inputConfirmPassword.getText());

                name = String.valueOf(inputName.getText());
                phone = String.valueOf(inputPhoneNumber.getText());
                try {
                    age = Integer.parseInt(String.valueOf(inputAge.getText()));
                } catch (NumberFormatException e) {
                    age = 0;
                }

                nationality = String.valueOf(inputNationality.getText());

                addressNumber = String.valueOf(inputAddressNumber.getText());
                street = String.valueOf(inputStreet.getText());
                ward = String.valueOf(inputWard.getText());
                district = String.valueOf(inputDistrict.getText());
                city = String.valueOf(inputCity.getText());
                country = String.valueOf(inputCountry.getText());

                userAddress userAddress = new userAddress(addressNumber, street, ward, district, city, country);
                UserModel userModel = new UserModel(name, email, phone, age, nationality, userAddress, 100);




                if (!(email.isEmpty()) && !(password.isEmpty()) && !(confirm.isEmpty()) && password.equals(confirm)) {
                    progressBar.setVisibility(View.VISIBLE);
                    signUpFunction(email, password, userModel);
                }
                else{
                    Toast.makeText(RegisterActivity.this, "Fail to register!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    private void signUpFunction(String userEmail, String userPassword, UserModel userModel){
        FirebaseAuth userAccount = FirebaseAuth.getInstance();
        FirebaseFirestore userDataRegister = FirebaseFirestore.getInstance();
        userAccount.createUserWithEmailAndPassword(userEmail, userPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressBar.setVisibility(View.GONE);
                if (task.isSuccessful()){
                    userDataRegister.collection("users").document(userAccount.getCurrentUser().getUid()).set(userModel);
                    Toast.makeText(RegisterActivity.this, "Sign up account successfully", Toast.LENGTH_SHORT).show();
                    Intent backToLogin = new Intent(RegisterActivity.this, LoginPageActivity.class);
                    startActivity(backToLogin);
                    finish();
                }
            }
        });
    }
}