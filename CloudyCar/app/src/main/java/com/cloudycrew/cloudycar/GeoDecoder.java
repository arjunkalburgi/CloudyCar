package com.cloudycrew.cloudycar;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by Harley Vanselow on 2016-11-11.
 */

public class GeoDecoder {
    private Context context;
    public GeoDecoder(Context context){
        this.context = context;
    }
    public String decodeLatLng(double longitude, double latitude){
        String result;
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        List<Address> addresses = new ArrayList<>();
        try {
            addresses = geocoder.getFromLocation(latitude, longitude,1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(addresses == null ||addresses.size() == 0) {
            return noInfoFound();
        }
        Address addressToDisplay = addresses.get(0);
        String addressLine = addressToDisplay.getAddressLine(0);
        if(addressLine != null){
            result = addressLine;
        }else{
            result = noInfoFound();
        }

        return result;
    }

    private String noInfoFound() {
        return "";
    }
}
