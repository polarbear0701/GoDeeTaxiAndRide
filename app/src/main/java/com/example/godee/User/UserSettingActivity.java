package com.example.godee.User;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SwitchCompat;

import com.example.godee.R;

public class UserSettingActivity extends AppCompatActivity {
    SwitchCompat darkModeToggle;
    Button customerSupport, setting_backBtn;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    boolean nightMode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_setting);

         darkModeToggle = findViewById(R.id.user_dark_mode);
         customerSupport = findViewById(R.id.user_customer_support);
         setting_backBtn = findViewById(R.id.setting_backBtn);

         setting_backBtn.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 Intent backtoProfile = new Intent(getApplicationContext(), ProfileActivity.class);
                 startActivity(backtoProfile);
             }
         });

         sharedPreferences = getSharedPreferences("MODE", Context.MODE_PRIVATE);

         nightMode = sharedPreferences.getBoolean("nightMode", false);

         if (nightMode){
             darkModeToggle.setChecked(true);
         }
         // Remember to fully custom color and put dark mode value in res/values/color
            /*for this easily go to res>values>colors and there Right Click> New> values Resource Files > choose a name
            and in Available Qualifiers choose night mode, now you will have a colors.xml file and a colors.xml(night) file which have colors with
            same names but different values specific for that mode(Dark/normal)*/
        darkModeToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (nightMode) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    editor = sharedPreferences.edit();
                    editor.putBoolean("nightMode", false);
                }
                else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    editor = sharedPreferences.edit();
                    editor.putBoolean("nightMode", true);
                }
                editor.apply();
            }
        });


    }
}