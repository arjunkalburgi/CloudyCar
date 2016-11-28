package com.cloudycrew.cloudycar.search;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.widget.Toast;

import com.cloudycrew.cloudycar.BaseActivity;
import com.cloudycrew.cloudycar.R;
import com.cloudycrew.cloudycar.models.Location;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
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

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.cloudycrew.cloudycar.Constants.MAX_RADIUS;
import static com.cloudycrew.cloudycar.utils.MapUtils.toBounds;

public class LocationSearchActivity extends BaseActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    @BindView(R.id.map_search_fab)
    FloatingActionButton mapSearchFab;

    int AUTOCOMPLETE_REQUEST_CODE = 1;
    public static final int CAMERA_ZOOM_PADDING = 150;
    private GoogleApiClient mGoogleApiClient;
    private GoogleMap mMap;
    private Marker currentMarker;
    private Circle currentRadius;
    private MarkerOptions markerOptions;
    private int radius;
    private LatLng selectedLocation;
    private LatLng myLocation;
    private static final int REQUEST_LOCATION_PERMISSIONS = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_search);
        ButterKnife.bind(this);
        mapSearchFab.hide();
        radius = this.getIntent().getExtras().getInt("radius");
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
        mGoogleApiClient.connect();
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(this, data);
                mMap.animateCamera(CameraUpdateFactory.newLatLng(place.getLatLng()));
                LocationSearchActivity.this.onPlaceSelected(mMap, place.getLatLng());
            }

        }
    }

    public void onPlaceSelected(GoogleMap mMap, LatLng latLng) {
        reDrawMarkerAndRadius(mMap, latLng);
        selectedLocation = latLng;
    }

    private void reDrawMarkerAndRadius(GoogleMap mMap, LatLng latLng) {
        if (currentMarker != null) {
            currentMarker.remove();
        }
        if (currentRadius != null) {
            currentRadius.remove();
        }
        currentRadius = mMap.addCircle(new CircleOptions().center(latLng).radius(radius));
        currentMarker = mMap.addMarker(markerOptions.position(latLng));
    }

    @OnClick(R.id.map_search_fab)
    public void startSearch() {
        try {
            Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY)
                    .setBoundsBias(toBounds(myLocation, MAX_RADIUS))
                    .build(this);
            startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE);
        } catch (GooglePlayServicesRepairableException e) {
            e.printStackTrace();
        } catch (GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }
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
     *
     * @param bundle
     */
    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_PERMISSIONS);
            return;
        }
        markerOptions = new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.drawable.currect_location_24dp)).anchor(0.5f, 0.5f);
        mMap.setMyLocationEnabled(true);
        android.location.Location currentLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        myLocation = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
        mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(toBounds(myLocation, radius), CAMERA_ZOOM_PADDING));
        onPlaceSelected(mMap, myLocation);
        mapSearchFab.show();
    }

    @OnClick(R.id.submit_selected_location)
    public void submitSelectedLocation() {
        Intent intent = new Intent(this, SearchParamsActivity.class);
        intent.putExtra("location", new Location(
                selectedLocation.longitude, selectedLocation.latitude, "User selected point"));
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                onPlaceSelected(mMap, latLng);
            }
        });
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
