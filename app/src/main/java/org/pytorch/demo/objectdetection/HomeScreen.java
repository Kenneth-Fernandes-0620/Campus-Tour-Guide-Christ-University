package org.pytorch.demo.objectdetection;

import android.Manifest;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.pytorch.demo.objectdetection.location.LocationHelper;
import org.pytorch.demo.objectdetection.location.LocationMonitorService;
import org.pytorch.demo.objectdetection.sensors.SensorHelper;


public class HomeScreen extends AppCompatActivity implements SensorEventListener {

    private Boolean areAllFabsVisible;
    private SensorHelper sensorHelper;
    private LocationHelper locationHelper;
    private FloatingActionButton editFab, saveFab, signupFab;
    private Animation fabOpen, fabClose, rotateForward, rotateBackward;
    private static final String TAG = "HomeScreen";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        editFab = findViewById(R.id.editfab);
        saveFab = findViewById(R.id.savefab);
        signupFab = findViewById(R.id.signupfab);
        saveFab.setVisibility(View.GONE);
        signupFab.setVisibility(View.GONE);
        fabOpen = AnimationUtils.loadAnimation(this, R.anim.from_bottom_animation);
        fabClose = AnimationUtils.loadAnimation(this, R.anim.to_bottom_animation);
        rotateForward = AnimationUtils.loadAnimation(this, R.anim.anim2);
        rotateBackward = AnimationUtils.loadAnimation(this, R.anim.anim1);
        areAllFabsVisible = false;
        editFab.setOnClickListener(v -> {
            animateFab();
            if (!areAllFabsVisible) {
                saveFab.show();
                signupFab.show();
                signupFab.setClickable(true);
                saveFab.setClickable(true);
                areAllFabsVisible = true;
            } else {
                saveFab.setVisibility(View.GONE);
                signupFab.setVisibility(View.GONE);
                areAllFabsVisible = false;
            }
        });
        saveFab.setOnClickListener(v -> startActivity(new Intent(HomeScreen.this, MainActivity.class)));
        signupFab.setOnClickListener(v -> startActivity(new Intent(HomeScreen.this, MainActivity.class)));

        sensorHelper = new SensorHelper(this); // sensors
        locationHelper = new LocationHelper((LocationManager) getSystemService(Context.LOCATION_SERVICE)); // location
    }

    private void animateFab() {
        if (areAllFabsVisible) {
            editFab.startAnimation(rotateForward);
            saveFab.startAnimation(fabClose);
            signupFab.startAnimation(fabClose);
            saveFab.setClickable(false);
            signupFab.setClickable(false);
        } else {
            editFab.startAnimation(rotateBackward);
            saveFab.startAnimation(fabOpen);
            signupFab.startAnimation(fabOpen);
            signupFab.setClickable(true);
            saveFab.setClickable(true);
        }
    }

    private boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onResume() {
        super.onResume();
        sensorHelper.sensorManager.registerListener(this, sensorHelper.accelerometer, SensorManager.SENSOR_DELAY_UI);
        sensorHelper.sensorManager.registerListener(this, sensorHelper.magnetometer, SensorManager.SENSOR_DELAY_UI);

        handleLocationPermissions();
    }

    private void handleLocationPermissions() {
        Log.d(TAG, "handleLocationPermissions: Attempting to get location");
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "getLastLocation: Coarse and Fine Location Allowed");
            if (isLocationEnabled()) {
                Log.d(TAG, "getLastLocation: Location Enabled");
//                locationHelper.listenForLocationUpdates(sensorHelper);
                startLocationService();
            } else {
                Log.d(TAG, "getLastLocation: Location not Enabled");
                startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
            }
        } else {
            Log.d(TAG, "getLastLocation: Coarse and Fine Location Not Allowed");
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, LocationHelper.LOCATION_PERMISSION_ID);
        }
    }


    @Override
    protected void onPause() {
        super.onPause();
        sensorHelper.sensorManager.unregisterListener(this);
        locationHelper.stopListeningForUpdates();
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor == sensorHelper.accelerometer)
            System.arraycopy(event.values, 0, sensorHelper.accelerometerData, 0, 3);
        else if (event.sensor == sensorHelper.magnetometer)
            System.arraycopy(event.values, 0, sensorHelper.magnetometerData, 0, 3);
        sensorHelper.calculateSensorReadings();
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }


    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    private void startLocationService() {
        if (isMyServiceRunning(LocationMonitorService.class)) {
            Log.d(TAG, "onResume: Service is already running");
        } else {
            Log.d(TAG, "onResume: Service is not running, it will be started");
            getApplicationContext().startForegroundService(new Intent(this, LocationMonitorService.class));
        }
    }

}