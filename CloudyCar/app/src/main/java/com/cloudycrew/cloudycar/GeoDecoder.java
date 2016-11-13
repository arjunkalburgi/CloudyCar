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

/**
 * This class is responsible for converting Lat/Long coordinates into more human readable text.
 */
public class GeoDecoder {
    private Context context;
    public GeoDecoder(Context context){
        this.context = context;
    }

    /**
     * Returns a string describing the location indicated by the input arguments
     * @param longitude
     * @param latitude
     * @return          Address corresponding to the lat/long
     */
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

    /**
     * Method called if the Geocoding class can't produce an address
     * @return Returns an empty string if nothing is found
     */
    private String noInfoFound() {
        return "";
    }
}
