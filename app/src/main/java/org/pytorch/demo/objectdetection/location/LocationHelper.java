package org.pytorch.demo.objectdetection.location;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;

import org.pytorch.demo.objectdetection.sensors.SensorHelper;

public class LocationHelper {
    private static final String TAG = "LocationHelper";
    public LocationManager locationManager;
    public LocationListener locationListener;
    public static int LOCATION_PERMISSION_ID = 44;


    public LocationHelper(LocationManager locationManager) {
        this.locationManager = locationManager;
    }

    public void listenForLocationUpdates(SensorHelper sensorHelper) {
        this.locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                sensorHelper.currentLatitude = location.getLatitude();
                sensorHelper.currentLongitude = location.getLongitude();
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            @Override
            public void onProviderEnabled(@NonNull String provider) {
                Log.d(TAG, "onProviderEnabled: " + provider);
            }

            @Override
            public void onProviderDisabled(@NonNull String provider) {
                Log.d(TAG, "onProviderDisabled: " + provider);
            }
        };

        try {
            final long MIN_TIME = 1000; // 1 second
            final float MIN_DISTANCE = 0;
            this.locationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    MIN_TIME,
                    MIN_DISTANCE,
                    this.locationListener
            );
            this.locationManager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER,
                    MIN_TIME,
                    MIN_DISTANCE,
                    this.locationListener
            );
            this.locationManager.requestLocationUpdates(
                    LocationManager.PASSIVE_PROVIDER,
                    MIN_TIME,
                    MIN_DISTANCE,
                    this.locationListener
            );
        } catch (SecurityException e) {
            e.printStackTrace();
        }

    }

    public void stopListeningForUpdates() {
        try {
            if (locationListener != null)
                locationManager.removeUpdates(locationListener);
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }
}
