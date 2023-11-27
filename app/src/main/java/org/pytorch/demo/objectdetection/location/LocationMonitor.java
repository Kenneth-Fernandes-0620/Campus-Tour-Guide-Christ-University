package org.pytorch.demo.objectdetection.location;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.core.app.JobIntentService;

public class LocationMonitor extends JobIntentService {

    private static final int JOB_ID = 1;


    @Override
    protected void onHandleWork(@NonNull Intent intent) {
        for (int i = 0; i < 10; i++) {
        }
    }

    public static void enqueueWork(Context context, Intent intent) {
        enqueueWork(context, LocationMonitor.class, JOB_ID, intent);
    }

}