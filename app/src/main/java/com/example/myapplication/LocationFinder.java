package com.example.myapplication;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.util.Log;

import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.List;
import java.util.Locale;

public class LocationFinder {

    public static final int LOCATION_REQUEST_CODE = 1000;

    public static void getLocation(Activity activity) {

        FusedLocationProviderClient fusedLocationProviderClient;


        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(activity);

        final Geocoder geocoder = new Geocoder(activity, Locale.getDefault());

        if(ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(activity,
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                    LOCATION_REQUEST_CODE);
        }else
        fusedLocationProviderClient.getLastLocation()
                        .addOnSuccessListener(new OnSuccessListener<Location>() {
                            @Override
                            public void onSuccess(android.location.Location location) {
                                try {
                                    List<Address> addresses = geocoder.getFromLocation
                                            (location.getLatitude(), location.getLongitude(), 1);
                                     String countryName = addresses.get(0).getCountryName();
                                     Application.setLocation(countryName);
                                     Log.d("LocationFinder", countryName);
                                     Log.d("LocationFinder", Application.getLocation());
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                            }
                        });
    }

}
