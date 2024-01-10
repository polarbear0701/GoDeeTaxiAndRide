package com.example.godee.Driver.Driver;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import com.example.godee.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class DriverMapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    LatLng driverCurrentLocationInstance;
    FirebaseAuth auth;
    FirebaseUser user;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FusedLocationProviderClient client;
    Handler handler;
    long refresh = 5000;
    Runnable runnable;
    private Button joinSessionBtn;
    private Boolean checkSessionJoin = false;

    @Override
    protected void onStop() {
        super.onStop();
        auth.signOut();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestPermission();
        client = LocationServices.getFusedLocationProviderClient(DriverMapsActivity.this);

        super.onCreate(savedInstanceState);
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        com.example.godee.databinding.ActivityDriverMapsBinding binding = com.example.godee.databinding.ActivityDriverMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Initiate content for page navigation bar
        BottomNavigationView driverPageMenu = findViewById(R.id.driver_page_navigation);
        driverPageMenu.setSelectedItemId(R.id.driver_activity_home);
        driverPageNavigation(driverPageMenu);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);
        handler = new Handler();
        handler.postDelayed(runnable = () -> {
            mMap.clear();
            getDriverCurrentLocation();
            handler.postDelayed(runnable, refresh);

        }, refresh);

        joinSessionBtn = findViewById(R.id.btn);
        joinSessionBtn.setOnClickListener(v -> {
            checkSessionJoin = true;
        });

    }

    // Function for page navigation (bottom navigation bar)
    public void driverPageNavigation(BottomNavigationView driverPageMenu) {
        driverPageMenu.setOnItemSelectedListener(item ->
        {   int itemId = item.getItemId();
            if (itemId == R.id.driver_activity_home){
                // Start new activity base on the item selected on the navigation bar
                // If the current tab is equal to the selected item only need to set this if-statement to return true
                return true;
            }
            if (itemId == R.id.driver_activity_profile){
                Intent intent = new Intent(getApplicationContext(), DriverProfileActivity.class);
                startActivity(intent);
                finish();
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

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
//        mMap.getUiSettings().setAllGesturesEnabled(true);
        //set zoom control
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.setPadding(0, 0,0, 400);
    }
    private void requestPermission(){
        ActivityCompat.requestPermissions(DriverMapsActivity.this,new String[]{
                android.Manifest.permission.ACCESS_COARSE_LOCATION,
                android.Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.INTERNET
        }, 99);
    }

    @SuppressLint("MissingPermission")
    public void getDriverCurrentLocation(){
        client.getCurrentLocation(LocationRequest.PRIORITY_HIGH_ACCURACY, null).addOnSuccessListener(location -> {
            LatLng driverCurrentLocation = new LatLng(location.getLatitude(), location.getLongitude());
            driverCurrentLocationInstance = driverCurrentLocation;
            if (checkSessionJoin)
                joinSession();
            mMap.addMarker(new MarkerOptions().position(driverCurrentLocation).title("Your Location"));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(driverCurrentLocation));
            mMap.animateCamera(CameraUpdateFactory.zoomTo(14));
        });
    }

    public void joinSession(){
        DocumentReference docRef = db.collection("drivers").document(Objects.requireNonNull(auth.getCurrentUser()).getUid());
        docRef.addSnapshotListener((value, error) -> {
            if (error != null){
                Log.e("DriverMapsActivity", "Listen failed", error);
            }

            if (value != null && value.exists()){
                docRef.update("latitude", driverCurrentLocationInstance.latitude);
                docRef.update("longitude", driverCurrentLocationInstance.longitude);
                Toast.makeText(DriverMapsActivity.this, "Driver is online" + driverCurrentLocationInstance, Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(DriverMapsActivity.this, "Driver is offline", Toast.LENGTH_SHORT).show();
            }
        });
    }
}