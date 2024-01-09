package com.example.godee;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;
import com.example.godee.databinding.ActivityMapsBinding;
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
import com.google.maps.DirectionsApiRequest;
import com.google.maps.GeoApiContext;
import com.google.maps.android.PolyUtil;
import com.google.maps.model.DirectionsLeg;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.DirectionsStep;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback{
//    private static final int LOCATION_PERMISSION = 99;

    private GoogleMap mMap;
    private LatLng userCurrentLocationInstance;
    private FusedLocationProviderClient client;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestPermission();
        client = LocationServices.getFusedLocationProviderClient(MapsActivity.this);

        super.onCreate(savedInstanceState);

        com.example.godee.databinding.ActivityMapsBinding binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

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
                List<Address> addressList = null;
                Geocoder geocoder = new Geocoder(MapsActivity.this);
                try {
                    addressList = geocoder.getFromLocationName(location, 1);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                assert addressList != null;
                if (addressList.size() == 0) {
                    Toast.makeText(MapsActivity.this, "Location not found", Toast.LENGTH_SHORT).show();
                }
                else {
                    Address address = addressList.get(0);
                    LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
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
            Log.d("current location", "onMapReady: " + userCurrentLocationInstance);
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
            if (itemId == R.id.actiivty_history){
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
        // Replace YOUR_API_KEY with your actual API key
        DirectionsApiRequest request = new DirectionsApiRequest(new GeoApiContext.Builder().apiKey("AIzaSyAHUvfBiUgU0zaKO2TxG4oYKLl5geXMiwc").build());
        try {
            DirectionsResult result = request.origin(new com.google.maps.model.LatLng(origin.latitude, origin.longitude))
                    .destination(new com.google.maps.model.LatLng(destination.latitude, destination.longitude))
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
            // Handle errors, such as network issues or no route found
        }
    }


}