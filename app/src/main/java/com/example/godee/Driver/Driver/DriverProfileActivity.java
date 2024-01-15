package com.example.godee.Driver.Driver;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.godee.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class DriverProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_profile);

        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser driver = auth.getCurrentUser();


        BottomNavigationView driverPageMenu = findViewById(R.id.driver_page_navigation);
        driverPageMenu.setSelectedItemId(R.id.driver_activity_profile);
        driverPageNavigation(driverPageMenu);

        Button btnLogout = findViewById(R.id.btn_logout_driver);

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auth.signOut();
                Intent backtoLogin = new Intent(getApplicationContext(), DriverLoginActivity.class);
                startActivity(backtoLogin);
                finish();
            }
        });
    }


    // Function for page navigation (bottom navigation bar)
    public void driverPageNavigation(BottomNavigationView driverPageMenu) {
        driverPageMenu.setOnItemSelectedListener(item ->
        {   int itemId = item.getItemId();
            if (itemId == R.id.driver_activity_home){
                Intent intent = new Intent(getApplicationContext(), DriverMapsActivity.class);
                startActivity(intent);
                finish();
            }
            if (itemId == R.id.driver_activity_profile){
                return true;
            }
            if (itemId == R.id.driver_activity_history){
                Intent intent = new Intent(getApplicationContext(), DriverHistoryActivity.class);
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
}