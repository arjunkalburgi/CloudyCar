package com.cloudycrew.cloudycar.createrequest;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import com.cloudycrew.cloudycar.GeoDecoder;
import com.cloudycrew.cloudycar.R;
import com.cloudycrew.cloudycar.models.Point;
import com.cloudycrew.cloudycar.models.Route;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import butterknife.BindView;
import butterknife.OnClick;

public class RouteSelector extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private static final int REQUEST_LOCATION_PERMISSIONS = 1;
    private static final String startName = "Start";
    private static final String endName = "End";
    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;
    private LatLng start;
    private LatLng end;
    private GeoDecoder geoDecoder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_selector);
        geoDecoder = new GeoDecoder(this);
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
    }

    @OnClick(R.id.submit_route_from_map)
    private void submitOnClick() {
        if(end == null){
            Toast.makeText(this,"Choose a destination!",Toast.LENGTH_SHORT).show();
            return;
        }
        Intent intent = new Intent(this,CreateRequestActivity.class);
        Bundle bundle = new Bundle();
        Route route = getRoute();
        bundle.putSerializable("route",route);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @NonNull
    private Route getRoute() {
        Point startPoint = new Point(start.longitude,start.latitude, geoDecoder.decodeLatLng(start.longitude,start.latitude));
        Point endPoint = new Point(end.longitude,end.latitude, geoDecoder.decodeLatLng(end.longitude,end.latitude));
        return new Route(startPoint,endPoint);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
            @Override
            public void onMarkerDragStart(Marker marker) {

            }

            @Override
            public void onMarkerDrag(Marker marker) {

            }

            @Override
            public void onMarkerDragEnd(Marker marker) {
                if(marker.getTitle().equals(startName)){
                    start = marker.getPosition();
                }else if(marker.getTitle().equals(endName)){
                    end = marker.getPosition();
                }
            }
        });
        mMap.setOnMapClickListener(latLng -> {
            if (end == null) {
                mMap.addMarker(new MarkerOptions()
                        .title(endName)
                        .position(latLng)
                        .draggable(true))
                        .showInfoWindow();
                end = latLng;
            }
        });

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
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

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_PERMISSIONS);

            return;
        }
        mMap.setMyLocationEnabled(true);
        Location currentLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        LatLng myLocation = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myLocation, 15));
        mMap.addMarker(new MarkerOptions()
                .title(startName)
                .position(myLocation)
                .draggable(true)
        ).showInfoWindow();
        start = myLocation;
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


}

