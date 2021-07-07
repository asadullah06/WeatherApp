package com.app.weather.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.IntentSender;
import android.location.Location;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

public class LocationTracker extends LocationCallback {
    final String TAG = "LocationTracker";
    public static final int REQUEST_CHECK_SETTINGS = 0x1;
    FusedLocationProviderClient mFusedLocationProviderClient;
    LocationRequest mLocationRequest;

    public Location getCurrentLocation() {
        return currentLocation;
    }

    public void setCurrentLocation(Location currentLocation) {
        this.currentLocation = currentLocation;
    }

    private Location currentLocation;
    private Activity mActivity;
    private LocationFound listener;

    /*private static class SingletonHolder {
        public static final LocationTracker INSTANCE = new LocationTracker();
    }

    public static LocationTracker getInstance()  {
        return SingletonHolder.INSTANCE;
    }
*/


    public void createLocationRequest() {
        long UPDATE_INTERVAL_IN_MILLISECONDS = 180000;
        long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS =
                UPDATE_INTERVAL_IN_MILLISECONDS / 2;
//        mLocationRequest = new LocationRequest(); // this constructor is deprecated to create LocationRequest
        mLocationRequest = LocationRequest.create();
        mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    public void startLocationUpdates(Activity mActivity, LocationFound listener) {
        this.mActivity = mActivity;
        this.listener = listener;


        SettingsClient mSettingsClient = LocationServices.getSettingsClient(mActivity);
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(mActivity);

        createLocationRequest();

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);
        LocationSettingsRequest mLocationSettingsRequest = builder.build();

        // Begin by checking if the device has the necessary location settings.
        mSettingsClient.checkLocationSettings(mLocationSettingsRequest)
                .addOnSuccessListener(mActivity, new OnSuccessListener<LocationSettingsResponse>() {
                    @SuppressLint("MissingPermission")
                    @Override
                    public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                        Log.i(TAG, "All location settings are satisfied, location update started!.");
                        mFusedLocationProviderClient.requestLocationUpdates(mLocationRequest, LocationTracker.this, Looper.myLooper());
                    }
                })
                .addOnFailureListener(mActivity, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        int statusCode = ((ApiException) e).getStatusCode();
                        switch (statusCode) {
                            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                                Log.i(TAG, "Location settings are not satisfied. Attempting to upgrade location settings ");
                                try {
                                    // Show the dialog by calling startResolutionForResult(), and check the
                                    // result in onActivityResult().
                                    ResolvableApiException rae = (ResolvableApiException) e;
                                    rae.startResolutionForResult(mActivity, REQUEST_CHECK_SETTINGS);
                                } catch (IntentSender.SendIntentException sie) {
                                    sie.printStackTrace();
                                }
                                break;
                            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                                String errorMessage = "Location settings are inadequate, and cannot be fixed here. Fix in Settings.";
                                Toast.makeText(mActivity, "Enable GPS for accuracy", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }


    @Override
    public void onLocationResult(LocationResult locationResult) {
        super.onLocationResult(locationResult);

        // Using the Google Play services location APIs, your app can request the last known location
        // of the user's device. In most cases, you are interested in the user's current location,
        // which is usually equivalent to the last known location of the device.
        currentLocation = locationResult.getLastLocation();
        listener.onLocationFound(currentLocation);

        //to stop the current location fetching frequently
        stopLocationUpdates();

    }

    private void stopLocationUpdates() {

        try {
            if (mFusedLocationProviderClient != null) {
                mFusedLocationProviderClient.removeLocationUpdates(this)
                        .addOnCompleteListener(mActivity, new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                            }
                        });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
