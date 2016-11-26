package com.cloudycrew.cloudycar.search;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.widget.Toast;

import com.cloudycrew.cloudycar.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.slf4j.MarkerFactory;

public class LocationSearchActivity extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    private GoogleApiClient mGoogleApiClient;
    private GoogleMap mMap;
    private Marker currentMarker;
    private Circle currentRadius;
    private MarkerOptions markerOptions;
    private double radius;
    private static final int REQUEST_LOCATION_PERMISSIONS = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_search);
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
        mGoogleApiClient.connect();
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        radius = 500; //Draw from prev activity
    }

    public void drawMarkerAndRange(GoogleMap mMap, LatLng latLng){
        if(currentMarker != null){currentMarker.remove();}
        if(currentRadius != null){currentRadius.remove();}
        currentRadius = mMap.addCircle(new CircleOptions().center(latLng).radius(radius));
        currentMarker = mMap.addMarker(markerOptions.position(latLng));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        /**
         * Kiuwan suggests that this should have a defualt in the switch. After closer inspection
         * this should probably just be an if since we don't actually look for any other cases.
         */
        switch (requestCode) {
            case REQUEST_LOCATION_PERMISSIONS: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Location permission granted", Toast.LENGTH_SHORT).show();
                    this.recreate();
                } else {
                    Toast.makeText(this, "No location permission", Toast.LENGTH_SHORT).show();
                }

            }
        }
    }
    /**
     * The GoogleMaps API has connected, so now LocationServices can be used to access user location
     * and place the initial start marker
     * @param bundle
     */
    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_PERMISSIONS);

            return;
        }
        markerOptions = new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.currect_location_24dp)).anchor(0.5f,0.5f);
        mMap.setMyLocationEnabled(true);
        Location currentLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        LatLng myLocation = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myLocation, 15));
        drawMarkerAndRange(mMap,myLocation);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        CardView searchCard = (CardView) findViewById(R.id.map_search_card);
        mMap.setPadding(0,searchCard.getHeight()+15,0,0);
        mMap.setOnMapClickListener(latLng -> drawMarkerAndRange(mMap,latLng));
    }

    @Override
    public void onConnectionSuspended(int i) {

    }
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
