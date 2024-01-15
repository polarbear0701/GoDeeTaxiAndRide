package com.example.godee.User;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import com.example.godee.Driver.Driver.ModelClass.DriveSession;
import com.example.godee.Driver.Driver.ModelClass.DriverModel;
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
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.maps.DirectionsApiRequest;
import com.google.maps.DistanceMatrixApi;
import com.google.maps.GeoApiContext;
import com.google.maps.android.PolyUtil;
import com.google.maps.model.DirectionsLeg;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.DirectionsStep;
import com.google.maps.model.DistanceMatrix;
import com.google.maps.model.TravelMode;
import com.google.maps.model.Unit;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback{
//    private static final int LOCATION_PERMISSION = 99;

    private GoogleMap mMap;
    private LatLng userCurrentLocationInstance;
    private LatLng userDestination;
    private FusedLocationProviderClient client;
    private String destinationAddress;
    int price = 0;
//    TextView priceView = findViewById(R.id.pricing);

    private FirebaseFirestore db = FirebaseFirestore.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestPermission();
        client = LocationServices.getFusedLocationProviderClient(MapsActivity.this);

        ProgressBar progressBar = findViewById(R.id.progressBar);
        super.onCreate(savedInstanceState);
        com.example.godee.databinding.ActivityMapsBinding binding = com.example.godee.databinding.ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Button confirmBooking = findViewById(R.id.btnConfirmBooking);

        confirmBooking.setOnClickListener(v -> {
            Toast.makeText(this, "Booking confirmed", Toast.LENGTH_SHORT).show();
            CollectionReference onlineDriver = db.collection("drivers");
            onlineDriver.whereEqualTo("inSession", true).get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    double minDistance = 500;
                    String driverId = "";
                    FirebaseAuth mAuth = FirebaseAuth.getInstance();

                    for (int i = 0; i < task.getResult().size(); i++) {
                        DriverModel temp = task.getResult().getDocuments().get(i).toObject(DriverModel.class);
                        assert temp != null;
                        LatLng driverLocation = new LatLng(temp.getLatitude(), temp.getLongitude());
                        double distance = matchingAlgorithm(userCurrentLocationInstance, driverLocation);
                        if (distance < minDistance && distance > 1) {
                            minDistance = distance;
                            driverId = task.getResult().getDocuments().get(i).getId();
                        }
//                        Log.d("Driver" + i, task.getResult().getDocuments().get(i).getId() + " " +distance);
                    }
//                    Log.d("Min distance", String.valueOf(minDistance) + " " + driverId);
                    DriveSession newSession = new DriveSession(driverId, mAuth.getCurrentUser().getUid(), userCurrentLocationInstance.latitude, userCurrentLocationInstance.longitude, userDestination.latitude, userDestination.longitude, destinationAddress);
//                    db.collection("sessions").document(newSession.getSessionID()).set(newSession);
                    db.collection("drivers").document(driverId).update("driverAllSession", FieldValue.arrayUnion(newSession));
                    db.collection("drivers").document(driverId).update(("inSession"), false);
                    DocumentReference driver = db.collection("drivers").document(driverId);
                    driver.update("currentGuest", mAuth.getCurrentUser().getUid());
                }
            });
        });

        BottomNavigationView pageMenu = findViewById(R.id.page_navigation);
        pageMenu.setSelectedItemId(R.id.activity_home);
        pageNavigation(pageMenu);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.main_map_home);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);
        SearchView searchView = findViewById(R.id.locationSearch);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                String location = searchView.getQuery().toString();
                destinationAddress = location;
                List<Address> addressList = null;
                Geocoder geocoder = new Geocoder(MapsActivity.this);
                progressBar.setVisibility(View.VISIBLE);
                try {
                    addressList = geocoder.getFromLocationName(location, 1);
                    progressBar.setVisibility(View.GONE);
                } catch (IOException e) {
                    e.printStackTrace();
                    progressBar.setVisibility(View.GONE);
                }
                assert addressList != null;
                if (addressList.size() == 0) {
                    Toast.makeText(MapsActivity.this, "Location not found", Toast.LENGTH_SHORT).show();
                }
                else {
//                    mMap.clear();
                    Address address = addressList.get(0);
                    LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
                    userDestination = latLng;
                    drawRoute(userCurrentLocationInstance, latLng);
                    mMap.addMarker(new MarkerOptions().position(latLng).title(location));
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10));
                    Toast.makeText(MapsActivity.this, userCurrentLocationInstance.latitude + " " + userCurrentLocationInstance.longitude, Toast.LENGTH_SHORT).show();
                    Toast.makeText(MapsActivity.this, query, Toast.LENGTH_SHORT).show();
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
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
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        getUserCurrentPosition();
        //set padding for zoom control
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.setPadding(0, 0,0, 400);


        // Add a marker in Sydney and move the camera
//        LatLng sydney = new LatLng(-31,151);
//        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

    //get permission for location
    private void requestPermission(){
        ActivityCompat.requestPermissions(MapsActivity.this,new String[]{
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.INTERNET
        }, 99);
    }


    @SuppressLint("MissingPermission")
    public void getUserCurrentPosition(){
        client.getCurrentLocation(LocationRequest.PRIORITY_HIGH_ACCURACY, null).addOnSuccessListener(location -> {
            LatLng userCurrentLocation = new LatLng(location.getLatitude(), location.getLongitude());
            userCurrentLocationInstance = userCurrentLocation;
            mMap.addMarker(new MarkerOptions().position(userCurrentLocation).title("Your location"));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(userCurrentLocation));
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(userCurrentLocation, 13));
        });
    }

    // Function for page navigation (bottom navigation bar)
    public void pageNavigation(BottomNavigationView pageMenu) {
        pageMenu.setOnItemSelectedListener(item ->
        {   int itemId = item.getItemId();
            if (itemId == R.id.activity_home){
                // Start new activity base on the item selected on the navigation bar
                // If the current tab is equal to the selected item only need to set this if-statement to return true
                return true;
            }
            if (itemId == R.id.activity_profile){
                Intent intent = new Intent(getApplicationContext(),ProfileActivity.class);
                startActivity(intent);
                finish();
            }
            if (itemId == R.id.activity_history){
                Intent intent = new Intent(getApplicationContext(),HistoryPageActivity.class);
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

    private void drawRoute(LatLng origin, LatLng destination) {
        // Use a directions API client to get route information
        // Primary key here: AIzaSyAHUvfBiUgU0zaKO2TxG4oYKLl5geXMiwc
        // Replace with this key when exceed daily limit:  AIzaSyA-qGgUm9sSW2Kg8QX47yPofhaiVp14tAs
        // Extra key AIzaSyC_RqsNZ4qaLg2r3iY5_zMipflMarUrZus
//        AIzaSyB8ycsYUrwFVKgsW3aQ8OYx51NPm8TktMc
        LinearLayout bookingUI = findViewById(R.id.bookingView);
        TextView priceView = findViewById(R.id.pricing);
        bookingUI.setVisibility(View.VISIBLE);

        String apiKey = "AIzaSyB8ycsYUrwFVKgsW3aQ8OYx51NPm8TktMc";
        String second = "AIzaSyDOr1sNIfAdOHQ-BuktUDmIL4ySNjLdxL4";
        GeoApiContext context = new GeoApiContext.Builder()
                .apiKey(apiKey)
                .build();
        com.google.maps.model.LatLng convertedOrigin = new com.google.maps.model.LatLng(origin.latitude, origin.longitude); // San Francisco, CA
        com.google.maps.model.LatLng convertedDestination = new com.google.maps.model.LatLng(destination.latitude, destination.longitude); // Los Angeles, CA
        DirectionsApiRequest request = new DirectionsApiRequest(context);
        try {
            DirectionsResult result = request.origin(convertedOrigin)
                    .destination(convertedDestination)
                    .await();

            // Draw the route on the map
            List<LatLng> path = new ArrayList<>();
            for (DirectionsLeg leg : result.routes[0].legs) {
                for (DirectionsStep step : leg.steps) {
                    path.addAll(PolyUtil.decode(step.polyline.getEncodedPath()));
                }
            }
            mMap.addPolyline(new PolylineOptions().addAll(path).color(Color.BLUE));
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("Error getting route", e.getMessage());
        }

        try{
            DistanceMatrix distanceMatrix = DistanceMatrixApi.newRequest(context)
                    .origins(convertedOrigin)
                    .destinations(convertedDestination)
                    .mode(TravelMode.DRIVING)
                    .units(Unit.METRIC)
                    .await();

            // Access distance value in kilometers
            long distanceInMeters = distanceMatrix.rows[0].elements[0].distance.inMeters;
            double distanceInKilometers = distanceInMeters / 1000.0;
            price = (int) (distanceInKilometers * 10000);
            priceView.setText(String.valueOf(price));
            Toast.makeText(this, "Distance: " + distanceInKilometers + " km", Toast.LENGTH_SHORT).show();
            Log.d("Distance", "Distance: " + distanceInKilometers + " km");
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("Error getting distance", e.getMessage());
            Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show();
        }
    }

    private double matchingAlgorithm(LatLng origin, LatLng destination){
        String apiKey = "AIzaSyB8ycsYUrwFVKgsW3aQ8OYx51NPm8TktMc";
        GeoApiContext context = new GeoApiContext.Builder()
                .apiKey(apiKey)
                .build();
        com.google.maps.model.LatLng convertedOrigin = new com.google.maps.model.LatLng(origin.latitude, origin.longitude);
        com.google.maps.model.LatLng convertedDestination = new com.google.maps.model.LatLng(destination.latitude, destination.longitude);
        try{
            DistanceMatrix distanceMatrix = DistanceMatrixApi.newRequest(context)
                    .origins(convertedOrigin)
                    .destinations(convertedDestination)
                    .mode(TravelMode.DRIVING)
                    .units(Unit.METRIC)
                    .await();

            // Access distance value in kilometers
            long distanceInMeters = distanceMatrix.rows[0].elements[0].distance.inMeters;
            double distanceInKilometers = distanceInMeters / 1000.0;
            Toast.makeText(this, "Distance: " + distanceInKilometers + " km", Toast.LENGTH_SHORT).show();
            Log.d("Distance", "Distance: " + distanceInKilometers + " km");
            return distanceInKilometers;
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("Error getting distance", e.getMessage());
            Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show();
        }
        return 0;
    }
}