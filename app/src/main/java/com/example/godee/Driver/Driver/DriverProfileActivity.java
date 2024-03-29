package com.example.godee.Driver.Driver;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.godee.ChatActivity;
import com.example.godee.R;
import com.example.godee.Driver.Driver.DriverInfoActivity;
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

        Button driver_info = findViewById(R.id.profile_driver_edit);
        Button driver_setting = findViewById(R.id.profile_driver_setting_edit);
        Button driver_logout = findViewById(R.id.profile_driver_logout);
        Button driver_chat = findViewById(R.id.profile_driver_chat);

        driver_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ChatActivity.class);
                startActivity(intent);
            }
        });

        driver_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auth.signOut();
                Intent intent = new Intent(getApplicationContext(), DriverLoginActivity.class);
                startActivity(intent);
                finish();
            }
        });


        driver_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), DriverInfoActivity.class);
                intent.putExtra("CURRENT_USER_ID", driver.getUid());
                startActivity(intent);
            }
        });

        driver_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), DriverSettingActivity.class);
                startActivity(intent);
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