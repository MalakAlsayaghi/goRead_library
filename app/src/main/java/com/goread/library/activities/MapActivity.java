package com.goread.library.activities;

import static com.goread.library.utils.Constants.PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.goread.library.R;
import com.goread.library.models.LocationUpload;

import java.io.Serializable;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {

    private MapView mMapView;
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    boolean mLocationPermissionGranted = false;
    MarkerOptions markerOptions;
    LatLng selectedPlace;
    Button btnConfirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        mMapView = (MapView) findViewById(R.id.map);
        btnConfirm = findViewById(R.id.btnConfirm);
        mMapView.onCreate(savedInstanceState);
        // cardView = findViewById(R.id.test);
        mMapView.getMapAsync(this);
        getLocationPermission();

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selectedPlace != null) {
                    Toast.makeText(MapActivity.this, String.valueOf(selectedPlace.latitude), Toast.LENGTH_SHORT).show();
                    LocationUpload locationUpload = new LocationUpload(selectedPlace.latitude, selectedPlace.longitude);
                    Intent intent = new Intent();
                    intent.putExtra("selected_location", (Serializable) locationUpload);
                    setResult(RESULT_OK, intent);
                    finish();
                }
            }
        });

    }


    private void getLocationPermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;

        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_LOCATION);
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onMapReady(GoogleMap map) {
        double latitude = 15.288740;
        double longitude = 44.2026459;


        map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(@NonNull LatLng latLng) {
                if (markerOptions != null) {
                    map.clear();
                }
                markerOptions = new MarkerOptions().position(latLng).title("");
                selectedPlace = latLng;
                System.out.println(selectedPlace);
                map.addMarker(markerOptions);


            }
        });

        map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 17));
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            System.out.println("Location not enabled");
            return;
        }
        map.setMyLocationEnabled(true);


    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        mLocationPermissionGranted = false;
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mLocationPermissionGranted = true;
                }
            }
        }
    }

    @Override
    protected void onPause() {
        mMapView.onPause();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        mMapView.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mMapView.onSaveInstanceState(outState);
    }

}