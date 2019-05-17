package com.bigdig.mapsproject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MainActivity extends FragmentActivity implements OnMapReadyCallback, LocationListener, PermissionHelper.IPermissionCallback {

    private GoogleMap mMap;
    private LocationManager locationManager;
    private boolean mapIsReady = false;
    private static final long MIN_DISTANCE_FOR_UPDATE = 10;
    private static final long MIN_TIME_FOR_UPDATE = 1;
    Location location;

    private Button btnGPS;
    private Button btnNetwork;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        btnGPS = (Button) findViewById(R.id.gps);
        btnGPS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {

                Location gpsLocation = getLocation(LocationManager.GPS_PROVIDER);

                if (gpsLocation != null) {
                    double latitude = gpsLocation.getLatitude();
                    double longitude = gpsLocation.getLongitude();
                    updateMarker("ourPosition", latitude, longitude);
                    Toast.makeText(
                            getApplicationContext(),
                            "Mobile Location (GPS): \nLatitude: " + latitude
                                    + "\nLongitude: " + longitude,
                            Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(MainActivity.this, "GPS", Toast.LENGTH_SHORT).show();
                }

            }
        });

        btnNetwork = (Button) findViewById(R.id.network);
        btnNetwork.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {

                Location nwLocation = getLocation(LocationManager.NETWORK_PROVIDER);

                if (nwLocation != null) {
                    double latitude = nwLocation.getLatitude();
                    double longitude = nwLocation.getLongitude();
                    updateMarker("ourPosition", latitude, longitude);
                    Toast.makeText(
                            getApplicationContext(),
                            "Mobile Location (NW): \nLatitude: " + latitude
                                    + "\nLongitude: " + longitude,
                            Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(MainActivity.this, "Network", Toast.LENGTH_SHORT).show();
                }

            }
        });
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            PermissionHelper.getPermissions(this, this);
        } else {
            onPermissionSuccess();
        }

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mapIsReady = true;
        mMap = googleMap;
    }

    private void updateMarker(String tag, double latitude, double longtitude){
        if(mapIsReady) {
            LatLng position = new LatLng(latitude, longtitude);
            mMap.addMarker(new MarkerOptions().position(position).title(tag));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(position));
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        updateMarker("ourPosition",location.getLatitude(), location.getLongitude());
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onProviderDisabled(String provider) {
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionHelper.onRequestPermissionResult(this, this, requestCode, permissions, grantResults);
    }

    @Override
    public void onPermissionSuccess() {
        btnGPS.setEnabled(true);
        btnNetwork.setEnabled(true);
    }

    @SuppressLint("MissingPermission")
    public Location getLocation(String provider) {
        if (locationManager.isProviderEnabled(provider)) {
            locationManager.requestLocationUpdates(provider,
                    MIN_TIME_FOR_UPDATE, MIN_DISTANCE_FOR_UPDATE, this);
            if (locationManager != null) {
                location = locationManager.getLastKnownLocation(provider);
                return location;
            }
        }
        return null;
    }
}
