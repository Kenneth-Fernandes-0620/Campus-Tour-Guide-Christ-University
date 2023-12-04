package org.pytorch.demo.objectdetection.location;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.util.Log;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofenceStatusCodes;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.GeofencingRequest;

import java.util.HashMap;

public class GeofenceHelper extends ContextWrapper {

    private static final String TAG = "GeofenceHelper";
    private PendingIntent pendingIntent;
    public GeofencingClient geofencingClient;

    public static final HashMap<String, double[]> GEOFENCE_List = new HashMap<String, double[]>() {{
        put("Central Block", new double[]{12.934442018122818, 77.60595590343786, 20});
        put("Block 1", new double[]{12.93400975006008, 77.60680613915532, 20});
        put("Block 2", new double[]{12.93284567176461, 77.60642150871381, 20});
        put("KE Hall", new double[]{12.932311542073768, 77.60576906992978, 20});
    }};

    public GeofenceHelper(Context context, GeofencingClient geofencingClient) {
        super(context);
        this.geofencingClient = geofencingClient;
    }

    public GeofencingRequest getGeofencingRequest(Geofence geofence) {
        return new GeofencingRequest.Builder()
                .addGeofence(geofence)
                .setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER | GeofencingRequest.INITIAL_TRIGGER_DWELL | GeofencingRequest.INITIAL_TRIGGER_EXIT)
                .build();
    }

    public Geofence getGeofence(String id, LatLong latLong, float radius) {
        return new Geofence.Builder()
                .setCircularRegion(latLong.Latitude, latLong.Longitude, radius)
                .setRequestId(id)
                .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_DWELL | Geofence.GEOFENCE_TRANSITION_EXIT | Geofence.GEOFENCE_TRANSITION_ENTER)
                .setExpirationDuration(Geofence.NEVER_EXPIRE)
                .setLoiteringDelay(10000)
                .build();
    }


    public PendingIntent getPendingIntent() {
        if (pendingIntent != null)
            return pendingIntent;
        Intent intent = new Intent(this, GeofenceBroadcastReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(this, 2040, intent, PendingIntent.FLAG_MUTABLE);
        return pendingIntent;
    }

    public String getErrorString(Exception e) {
        if (e instanceof ApiException) {
            ApiException apiException = (ApiException) e;
            Log.d(TAG, "getErrorString: " + apiException.getMessage());
            switch (apiException.getStatusCode()) {
                case GeofenceStatusCodes.GEOFENCE_NOT_AVAILABLE:
                    return "GEOFENCE Not available";
                case GeofenceStatusCodes.GEOFENCE_TOO_MANY_GEOFENCES:
                    return "Too Many Geo fences Created";
                case GeofenceStatusCodes.GEOFENCE_TOO_MANY_PENDING_INTENTS:
                    return "Too Many Pending Intents";
                case GeofenceStatusCodes.GEOFENCE_INSUFFICIENT_LOCATION_PERMISSION:
                    return "Insufficient Location Permissions";
                case GeofenceStatusCodes.GEOFENCE_REQUEST_TOO_FREQUENT:
                    return "Geofence Requests too Frequent";
                case GeofenceStatusCodes.INTERNAL_ERROR:
                    return "Internal Error";
                default:
                    return "Unlisted Error";
            }
        }
        return e.getLocalizedMessage();
    }

}
