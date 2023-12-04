package org.pytorch.demo.objectdetection.location;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.app.JobIntentService;
import androidx.core.app.NotificationCompat;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationServices;

import org.pytorch.demo.objectdetection.R;
import org.pytorch.demo.objectdetection.sensors.SensorHelper;

import java.util.Map;

public class LocationMonitorService extends JobIntentService {

    private static final int JOB_ID = 1;
    private static final String TAG = "LocationMonitorService";

    private static final int NOTIFICATION_ID = 1;
    private static final String CHANNEL_ID = "MyServiceChannel";

    private GeofenceHelper geofenceHelper;
    private SensorHelper sensorHelper;
    private LocationHelper locationHelper;

    @Override
    public void onCreate() {
        super.onCreate();
        sensorHelper = new SensorHelper(this); // sensors
        locationHelper = new LocationHelper((LocationManager) getSystemService(Context.LOCATION_SERVICE)); // location
        geofenceHelper = new GeofenceHelper(this, LocationServices.getGeofencingClient(this)); // geofencing
        locationHelper.listenForLocationUpdates();
        startForeground();
    }

    @Override
    protected void onHandleWork(@NonNull Intent intent) {
        for (int i = 0; i < 10; i++) {
        }
    }

    @Override
    public int onStartCommand(@Nullable Intent intent, int flags, int startId) {
//        sensorHelper.sensorManager.registerListener(this, sensorHelper.accelerometer, SensorManager.SENSOR_DELAY_UI);
//        sensorHelper.sensorManager.registerListener(this, sensorHelper.magnetometer, SensorManager.SENSOR_DELAY_UI);
        populateGeoFences();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: Going to stop service");
        removeGeoFence();
        locationHelper.stopListeningForUpdates();
        stopForeground(true);
    }

    public static void enqueueWork(Context context, Intent intent) {
        enqueueWork(context, LocationMonitorService.class, JOB_ID, intent);
    }

    public void showNotification() {
        Notification notification = new NotificationCompat.Builder(this, "test_channel")
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("Test Notification")
                .setContentText("Test Text")
                .build();
        startForeground(1, notification);
    }

    private void startForeground() {
        createNotificationChannel();

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("My Service")
                .setContentText("Running in the background")
                .setSmallIcon(R.mipmap.ic_launcher)
                .build();

        startForeground(NOTIFICATION_ID, notification);
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    CHANNEL_ID,
                    "My Service Channel",
                    NotificationManager.IMPORTANCE_DEFAULT
            );

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(serviceChannel);
        }
    }


    private void populateGeoFences() {
        for (Map.Entry<String, double[]> geofence : GeofenceHelper.GEOFENCE_List.entrySet())
            addGeofence(geofence.getKey(), new LatLong(geofence.getValue()[0], geofence.getValue()[1]), (float) geofence.getValue()[2]);
    }

    private void removeGeoFence() {
        Log.d(TAG, "Removing GeoFences:");
        geofenceHelper.geofencingClient.removeGeofences(geofenceHelper.getPendingIntent());
    }


    private void addGeofence(String id, LatLong latLong, float radius) {
        Log.d(TAG, "Adding Geo fences");
        Geofence geofence = geofenceHelper.getGeofence(id, latLong, radius);
        GeofencingRequest geofencingRequest = geofenceHelper.getGeofencingRequest(geofence);
        PendingIntent pendingIntent = geofenceHelper.getPendingIntent();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        geofenceHelper.geofencingClient
                .addGeofences(geofencingRequest, pendingIntent)
                .addOnSuccessListener(success -> Log.d(TAG, "addGeofence (on Success): Geofence " + id + " Added"))
                .addOnFailureListener(failure -> Log.d(TAG, "addGeofence (on Failure): id: " + id + "," + geofenceHelper.getErrorString(failure)));
    }
}