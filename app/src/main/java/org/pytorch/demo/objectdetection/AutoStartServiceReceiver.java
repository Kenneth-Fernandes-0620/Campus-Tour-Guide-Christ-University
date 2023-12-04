package org.pytorch.demo.objectdetection;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import org.pytorch.demo.objectdetection.location.LocationMonitorService;

import java.util.Objects;

public class AutoStartServiceReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (Objects.equals(intent.getAction(), Intent.ACTION_BOOT_COMPLETED))
            context.startService(new Intent(context, LocationMonitorService.class));
    }
}
