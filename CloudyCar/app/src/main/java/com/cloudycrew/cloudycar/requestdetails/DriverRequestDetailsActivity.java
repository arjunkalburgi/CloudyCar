package com.cloudycrew.cloudycar.requestdetails;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.cloudycrew.cloudycar.R;
import com.cloudycrew.cloudycar.models.requests.CompletedRequest;
import com.cloudycrew.cloudycar.models.requests.ConfirmedRequest;
import com.cloudycrew.cloudycar.models.requests.PendingRequest;
import com.cloudycrew.cloudycar.models.requests.Request;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

/**
 * Created by George on 2016-11-11.
 */

public class DriverRequestDetailsActivity extends BaseRequestDetailsActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener{
    GoogleApiClient mGoogleApiClient;
    GoogleMap mMap;
    Request current_request;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_request_details);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
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
                .findFragmentById(R.id.routeDisplayFragment);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void displayPendingRequest(PendingRequest pendingRequest) {
        displayBaseRequestInformation(pendingRequest);
        current_request = pendingRequest;
        if (pendingRequest.hasBeenAcceptedBy(userPreferences.getUserName())) {
            statusTextView.setText("Waiting for the rider to confirm");
        } else {
            statusTextView.setText(R.string.havent_accepted_ride);
            updateButton.setText(R.string.accept_request_button_text);
            updateButton.setOnClickListener(v -> requestDetailsController.acceptRequest());
            updateButton.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void displayConfirmedRequest(ConfirmedRequest confirmedRequest) {
        displayBaseRequestInformation(confirmedRequest);
        statusTextView.setText(R.string.rider_wants_pickup);
        updateButton.setVisibility(View.GONE);
        current_request = confirmedRequest;
        setDriver(confirmedRequest.getDriverUsername());
    }

    @Override
    public void displayCompletedRequest(CompletedRequest completedRequest) {
        displayBaseRequestInformation(completedRequest);
        current_request = completedRequest;
        setDriver(completedRequest.getDriverUsername());
    }

    /**
     * Once the Google API client has connected, add the markers representing the current route
     * start and end points, and configure the camera to include all markers
     * @param bundle
     */
    @Override
    public void onConnected(@Nullable Bundle bundle) {
        LatLng start = new LatLng(current_request.getRoute().getStartingPoint().getLatitude(),
                                    current_request.getRoute().getStartingPoint().getLongitude());
        LatLng end = new LatLng(current_request.getRoute().getEndingPoint().getLatitude(),
                                    current_request.getRoute().getEndingPoint().getLongitude());
        List<Marker> markers = new ArrayList<>();
        markers.add(mMap.addMarker(new MarkerOptions()
                .title("Start")
                .position(start)
                .draggable(true)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.currect_location_24dp))
        ));
        markers.add(mMap.addMarker(new MarkerOptions()
                .title("End")
                .position(end)
                .draggable(true)
        ));
        CameraUpdate cu = configureCamera(markers);
        mMap.animateCamera(cu);
    }

    /**
     * Based on the markers present in the map, configure the camera to zoom to a level so that all
     * markers are visible
     * @param markers The list of markers to be included in the view
     * @return
     */
    @NonNull
    private CameraUpdate configureCamera(List<Marker> markers) {
        //Credit: http://stackoverflow.com/questions/14828217/android-map-v2-zoom-to-show-all-the-markers
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for (Marker marker : markers) {
            builder.include(marker.getPosition());
        }
        LatLngBounds bounds = builder.build();

        int padding = 150; // offset from edges of the map in pixels
        return CameraUpdateFactory.newLatLngBounds(bounds, padding);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
    }
}
