package com.example.godee;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ProfileActivity extends AppCompatActivity {

    Button logoutButton;

    FirebaseAuth auth;

    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_profile);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        getSupportActionBar().hide();

        logoutButton = findViewById(R.id.logoutButton);

        BottomNavigationView pageMenu = findViewById(R.id.page_navigation);
        pageMenu.setSelectedItemId(R.id.activity_profile);
        pageNavigation(pageMenu);

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