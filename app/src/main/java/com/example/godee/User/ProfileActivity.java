package com.example.godee.User;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.example.godee.ChatActivity;
import com.example.godee.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class ProfileActivity extends AppCompatActivity {

    private Button logoutButton, chatButton;
    private TextView userEdit, settingEdit;

    FirebaseAuth auth;

    FirebaseUser user;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    SharedPreferences sharedPreferences;
    boolean nightMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        ProgressBar progressBar = findViewById(R.id.progressBar);

        setContentView(R.layout.activity_profile);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        userEdit = findViewById(R.id.profile_user_edit);
        settingEdit = findViewById(R.id.profile_setting_edit);

        sharedPreferences = getSharedPreferences("MODE", Context.MODE_PRIVATE);

        nightMode = sharedPreferences.getBoolean("nightMode", false);

        if (nightMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }

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

        chatButton = findViewById(R.id.chatButton);
        chatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.collection("users").document(Objects.requireNonNull(auth.getUid())).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        UserModel temp = task.getResult().toObject(UserModel.class);
                        Intent intent = new Intent(ProfileActivity.this, ChatActivity.class);
                        intent.putExtra("CURRENT_USER_ID", user.getUid());
                        assert temp != null;
                        intent.putExtra("OTHER_USER_ID", temp.getCurrentDriverID());
                        startActivity(intent);
                    }
                });

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
            if (itemId == R.id.activity_history){
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