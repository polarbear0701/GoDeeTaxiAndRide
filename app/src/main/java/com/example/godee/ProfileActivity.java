package com.example.godee;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class ProfileActivity extends AppCompatActivity {

    private Button logoutButton;
    private TextView userEdit, settingEdit;

    FirebaseAuth auth;

    FirebaseUser user;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        ProgressBar progressBar = findViewById(R.id.progressBar);

        setContentView(R.layout.activity_profile);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        userEdit = findViewById(R.id.profile_user_edit);
        settingEdit = findViewById(R.id.profile_setting_edit);

//        db.collection("users").document(auth.getUid()).get().addOnCompleteListener(task -> {
//            if (task.isSuccessful()) {
//                emailDisplay.setText(task.getResult().get("email").toString());
//                nameDisplay.setText(task.getResult().get("name").toString());
//            }
////            else{
////                progressBar.setVisibility(View.GONE);
////            }
//        });

        getSupportActionBar().hide();

        logoutButton = findViewById(R.id.logoutButton);

        BottomNavigationView pageMenu = findViewById(R.id.page_navigation);
        pageMenu.setSelectedItemId(R.id.activity_profile);
        pageNavigation(pageMenu);

        userEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), UserInfoActivity.class);
                startActivity(intent);
                finish();
            }
        });

        settingEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), UserSettingActivity.class);
                startActivity(intent);
                finish();
            }
        });

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userSignOut();
            }
        });
    }
    // Function for page navigation (bottom navigation bar)
    public void pageNavigation(BottomNavigationView pageMenu) {
        pageMenu.setOnItemSelectedListener(item ->
        {   int itemId = item.getItemId();
            if (itemId == R.id.activity_home){
                Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
                startActivity(intent);
                finish();
            }
            if (itemId == R.id.activity_profile){
                return true;
            }
            if (itemId == R.id.actiivty_history){
                Intent intent = new Intent(getApplicationContext(), HistoryPageActivity.class);
                startActivity(intent);
                finish();
            }
            // This part is expandable base on the number of tab need for the application
            /* Structure
               if (itemId == R.id.[name of the item]){
               Intent intent = new Intent(getApplicationContext(), [insert the activity associated with the icon].class);
               startActivity(intent);
               finish();}
             */
            return true;
        });
    }

    public void userSignOut() {
        auth.signOut();
        Intent backtoLogin = new Intent(getApplicationContext(), LoginPageActivity.class);
        startActivity(backtoLogin);
        finish();
    }
}