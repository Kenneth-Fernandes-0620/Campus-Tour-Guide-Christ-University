package org.pytorch.demo.objectdetection.location;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;

import java.util.List;

public class GeofenceBroadcastReceiver extends BroadcastReceiver {

    private static final String TAG = "GeofenceReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        GeofencingEvent geofencingEvent = GeofencingEvent.fromIntent(intent);
        if (geofencingEvent != null && geofencingEvent.hasError()) {
            Log.e(TAG, "GeofencingEvent error: " + geofencingEvent.getErrorCode());
            return;
        }

        if (geofencingEvent != null) {
            int geofenceTransition = geofencingEvent.getGeofenceTransition();
            List<Geofence> triggeredGeofences = geofencingEvent.getTriggeringGeofences();

            if (triggeredGeofences != null)
                switch (geofenceTransition) {
                    case Geofence.GEOFENCE_TRANSITION_ENTER:
                        handleEnterGeofence(context, triggeredGeofences);
                        break;
                    case Geofence.GEOFENCE_TRANSITION_EXIT:
                        handleExitGeofence(context, triggeredGeofences);
                        break;
                    case Geofence.GEOFENCE_TRANSITION_DWELL:
                        handleDwellGeofence(context, triggeredGeofences);
                        break;
                    default:
                        Log.e(TAG, "Unknown geofence transition: " + geofenceTransition);
                }
        }
    }

    private void handleDwellGeofence(Context context, List<Geofence> triggeredGeofences) {
        for (Geofence geofence : triggeredGeofences) {
            String geofenceId = geofence.getRequestId();
            Toast.makeText(context, "Entered Geofence: " + geofenceId, Toast.LENGTH_SHORT).show();
            Log.d(TAG, "handleEnterGeofence: Entered Geofence" + geofenceId);
        }
    }

    private void handleEnterGeofence(Context context, List<Geofence> triggeredGeofences) {
        for (Geofence geofence : triggeredGeofences) {
            String geofenceId = geofence.getRequestId();
            Toast.makeText(context, "Entered Geofence: " + geofenceId, Toast.LENGTH_SHORT).show();
            Log.d(TAG, "handleEnterGeofence: Entered Geofence" + geofenceId);
        }
    }

    private void handleExitGeofence(Context context, List<Geofence> triggeredGeofences) {
        for (Geofence geofence : triggeredGeofences) {
            String geofenceId = geofence.getRequestId();
            Toast.makeText(context, "Exited Geofence: " + geofenceId, Toast.LENGTH_SHORT).show();
            Log.d(TAG, "handleExitGeofence: Exited Geofence");
        }
    }
}
