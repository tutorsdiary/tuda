package com.tuda.teacher.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.tuda.teacher.TudaApp;
import com.tuda.teacher.common.GACommon;

public class PackageReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(final Context context, Intent intent) {
        if (intent != null) {
            String packageName = intent.getData().getSchemeSpecificPart();
            String action = intent.getAction();

            if (action.equals(Intent.ACTION_PACKAGE_ADDED)) {
                //TudaApp.gaManager.eventTracking(GACommon.eventCodeMap.get("CA"), "system", "Install");
            } else if (action.equals(Intent.ACTION_PACKAGE_REMOVED)) {
                //TudaApp.gaManager.eventTracking(GACommon.eventCodeMap.get("CA"), "system", "Uninstall");
            }
        }
    }
}
