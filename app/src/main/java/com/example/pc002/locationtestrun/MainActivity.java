package com.example.pc002.locationtestrun;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import org.w3c.dom.Text;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private LocationCallback mLocationCallback;
    private FusedLocationProviderClient mFusedLocationClient;
    private LocationRequest mLocationRequest;
    private Location lastKnowLocation;
    private Location currentLocation;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        updateValuesFromBundle(savedInstanceState);

        textView = findViewById(R.id.text);

        mLocationRequest = LocationRequest.create();
        mLocationRequest.setInterval(2000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.INTERNET, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    10);

            return;
        }

        mFusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            // Logic to handle location object

                            lastKnowLocation = location;
                        }
                    }
                });

        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {

                List<Location> locations = locationResult.getLocations();

                if(locations.size() == 1) {
                    currentLocation = locations.get(0);
                }

                if (locationResult == null) {
                    return;
                }

                for (Location location : locationResult.getLocations()) {
                    // Update UI with location data
                    // ...

                }
            }

            @Override
            public void onLocationAvailability(LocationAvailability locationAvailability) {
                if(currentLocation != null){
                    textView.setText(currentLocation.getLatitude()+"\n"+currentLocation.getLongitude());
                } else {
                    textView.setText(lastKnowLocation.getLatitude()+"\n"+lastKnowLocation.getLongitude());
                }
            }
        };
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 10:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startLocationUpdates();
                }
                break;
        }
    }
    //    private void updateValuesFromBundle(Bundle savedInstanceState) {
//        if (savedInstanceState == null) {
//            return;
//        }
//
//        // Update the value of mRequestingLocationUpdates from the Bundle.
//        if (savedInstanceState.keySet().contains(REQUESTING_LOCATION_UPDATES_KEY)) {
//            mRequestingLocationUpdates = savedInstanceState.getBoolean(
//                    REQUESTING_LOCATION_UPDATES_KEY);
//        }
//
//        // ...
//
//        // Update UI to match restored state
//        updateUI();
//    }

    @Override
    protected void onResume() {
        super.onResume();
//        if(mRequestingLocationUpdates){
        startLocationUpdates();
//        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopLocationUpdates();
    }

    private void stopLocationUpdates() {
        mFusedLocationClient.removeLocationUpdates(mLocationCallback);
    }

//    @Override
//    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
//        outState.putBoolean(REQUESTING_LOCATION_UPDATES_KEY,
//                mRequestingLocationUpdates);
//        super.onSaveInstanceState(outState, outPersistentState);
//    }

    @SuppressLint("MissingPermission")
    private void startLocationUpdates() {

        mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, null);
    }
}
