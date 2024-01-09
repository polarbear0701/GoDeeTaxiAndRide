package com.example.godee;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import com.example.godee.databinding.ActivityDriverMapsBinding;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class DriverMapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityDriverMapsBinding binding;
    private Button driverLogOutBtn;
    LatLng driverCurrentLocationInstance;
    FirebaseAuth auth;
    FirebaseUser user;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FusedLocationProviderClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestPermission();
        client = LocationServices.getFusedLocationProviderClient(DriverMapsActivity.this);

        super.onCreate(savedInstanceState);
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        binding = ActivityDriverMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        driverLogOutBtn = findViewById(R.id.btn_logout_driver);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);

        driverLogOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auth.signOut();
                Intent backToLogin = new Intent(getApplicationContext(), DriverLoginActivity.class);
                startActivity(backToLogin);
                finish();
            }
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
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setAllGesturesEnabled(true);
        mMap.setPadding(0, 0,0, 400);
        while (true){
            getDriverCurrentLocation();
        }

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
            DocumentReference docRef = db.collection("drivers").document(auth.getCurrentUser().getUid());
            docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                    if (error != null){
                        Log.e("DriverMapsActivity", "Listen failed", error);
                    }

                    if (value != null && value.exists()){
                        Toast.makeText(DriverMapsActivity.this, "Driver is online" + driverCurrentLocationInstance, Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(DriverMapsActivity.this, "Driver is offline", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            mMap.addMarker(new MarkerOptions().position(driverCurrentLocation).title("Your Location"));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(driverCurrentLocation));
            mMap.animateCamera(CameraUpdateFactory.zoomTo(14));
        });
    }
}