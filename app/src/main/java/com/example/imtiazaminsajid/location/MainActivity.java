package com.example.imtiazaminsajid.location;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.io.IOException;
import java.util.List;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {
    GoogleApiClient googleApiClient;
    LocationRequest locationRequest;
    TextView latTV, lonTV, addressTV;
    Geocoder geocoder;
    List<Address>addressesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        latTV = findViewById(R.id.latTV);
        lonTV = findViewById(R.id.lonTV);
        addressTV = findViewById(R.id.addressTV);

        geocoder = new Geocoder(this);

        googleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();


    }

    @Override
    protected void onStart() {
        super.onStart();
        googleApiClient.connect();
    }

    @Override
    protected void onPause() {
        googleApiClient.disconnect();
        super.onPause();

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        locationRequest = LocationRequest.create()
                .setInterval(1000)
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {

        latTV.setText("Latitude "+location.getLatitude());
        lonTV.setText("Longitude "+ location.getLongitude());

        try {
            addressesList = geocoder.getFromLocation(location.getLatitude(),location.getLongitude(), 1);

            String address = addressesList.get(0).getAddressLine(0);
            String city = addressesList.get(0).getLocality();
            String zip = addressesList.get(0).getPostalCode();
            String country = addressesList.get(0).getCountryName();
            String featureName = addressesList.get(0).getFeatureName();

            addressTV.setText("Address: "+address+"\n"+"City: "+city+"\n"+"ZipCode: "+zip+"\n"+"Country: "+country+"\n"+"Feature: "+featureName);


        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
