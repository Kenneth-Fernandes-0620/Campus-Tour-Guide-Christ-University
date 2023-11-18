package org.pytorch.demo.objectdetection;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.pytorch.demo.objectdetection.data.DatabaseHandler;


public class HomeScreen extends AppCompatActivity implements SensorEventListener {

    private FusedLocationProviderClient mFusedLocationClient;
    private Boolean isAllFabsVisible;
    private SensorManager sensorManager;
    private Sensor accelerometer;
    private Sensor magnetometer;
    private float[] accelerometerData = new float[3];
    private float[] magnetometerData = new float[3];

    private TextView heading;

    private FloatingActionButton editFab, saveFab, signupFab;
    private Animation fabOpen, fabClose, rotateForward, rotateBackward;
    int LOCATION_PERMISSION_ID = 44;

    private static final String TAG = "HomeScreen";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        editFab = findViewById(R.id.editfab);
        saveFab = findViewById(R.id.savefab);
        signupFab = findViewById(R.id.signupfab);
        heading = findViewById(R.id.heading);
        saveFab.setVisibility(View.GONE);
        signupFab.setVisibility(View.GONE);
        fabOpen = AnimationUtils.loadAnimation
                (this, R.anim.from_bottom_animation);
        fabClose = AnimationUtils.loadAnimation
                (this, R.anim.to_bottom_animation);
        rotateForward = AnimationUtils.loadAnimation
                (this, R.anim.anim2);
        rotateBackward = AnimationUtils.loadAnimation
                (this, R.anim.anim1);
        isAllFabsVisible = false;
        editFab.setOnClickListener(v -> {
            animateFab();
            if (!isAllFabsVisible) {
                saveFab.show();
                signupFab.show();
                signupFab.setClickable(true);
                saveFab.setClickable(true);
                isAllFabsVisible = true;
            } else {
                saveFab.setVisibility(View.GONE);
                signupFab.setVisibility(View.GONE);
                isAllFabsVisible = false;
            }
        });
        saveFab.setOnClickListener(v -> {
            Intent i = new Intent(HomeScreen.this, CompassActivity.class);
            startActivity(i);
        });

        signupFab.setOnClickListener(v -> {
            Intent i = new Intent(HomeScreen.this, MainActivity.class);
            startActivity(i);
        });

        // Initialize SensorManager and sensors
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        magnetometer = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
    }

    private void animateFab() {
        if (isAllFabsVisible) {
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

    private void getLastLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "getLastLocation: Coarse and Fine Location Allowed");
            if (isLocationEnabled()) {
                mFusedLocationClient.getLastLocation().addOnCompleteListener(task -> {
                    Location location = task.getResult();
                    if (location == null)
                        requestNewLocationData();
                    else {
                        Log.d(TAG, "getLastLocation: Latitude: " + location.getLatitude() + " longitude: " + location.getLongitude());
                        Log.d(TAG, "getLastLocation: " + location.getBearing());
                        Toast.makeText(getApplicationContext(), location.getLatitude() + "", Toast.LENGTH_SHORT).show();
                        Toast.makeText(getApplicationContext(), location.getLongitude() + "", Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                Log.d(TAG, "getLastLocation: Location not Enabled");
                startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
            }
        } else {
            Log.d(TAG, "getLastLocation: Coarse and Fine Location Not Allowed");
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, LOCATION_PERMISSION_ID);
        }
    }

    @SuppressLint("MissingPermission")
    private void requestNewLocationData() {
        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(5);
        mLocationRequest.setFastestInterval(0);
        mLocationRequest.setNumUpdates(1);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
    }

    private final LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            Location mLastLocation = locationResult.getLastLocation();
            Toast.makeText(getApplicationContext(), mLastLocation.getLatitude() + "", Toast.LENGTH_SHORT).show();
            Toast.makeText(getApplicationContext(), mLastLocation.getLongitude() + "", Toast.LENGTH_SHORT).show();
        }
    };

    private boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    @Override
    public void
    onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_ID) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLastLocation();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_GAME);
        sensorManager.registerListener(this, magnetometer, SensorManager.SENSOR_DELAY_GAME);
        getLastLocation();
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor == accelerometer) {
            System.arraycopy(event.values, 0, accelerometerData, 0, 3);
        } else if (event.sensor == magnetometer) {
            System.arraycopy(event.values, 0, magnetometerData, 0, 3);
        }

        // Calculate device orientation using accelerometer and magnetometer data
        float[] rotationMatrix = new float[9];
        float[] orientationValues = new float[3];
        if (SensorManager.getRotationMatrix(rotationMatrix, null, accelerometerData, magnetometerData)) {
            SensorManager.getOrientation(rotationMatrix, orientationValues);

            float azimuth = (float) Math.toDegrees(orientationValues[0]);

            heading.setText(getString(R.string.heading, getCardinalDirection(azimuth)));
//            Log.d(TAG, "onSensorChanged: Heading: " + azimuth + ", Towards: " + getCardinalDirection(azimuth));
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    private String getCardinalDirection(float heading) {
        String[] cardinalDirections = {"N", "NE", "E", "SE", "S", "SW", "W", "NW", "N"};

        int index = Math.round((heading % 360) / 45);
        if (index < 0) index += 8;

        return cardinalDirections[index];
    }

}