package com.tuda.teacher.util;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.tuda.teacher.BuildConfig;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class AppInfoManager {
    public static boolean isBackground(Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> tasks = am.getRunningTasks(1);
        if (!tasks.isEmpty()) {
            ComponentName topActivity = tasks.get(0).topActivity;
            if (!topActivity.getPackageName().equals(context.getPackageName())) {
                return true;
            }
        }

        return false;
    }

    public static HashMap<String, String> getVersionInfo(Context context) {
        HashMap<String, String> versionInfo = new HashMap<String, String>();

        versionInfo.put("versionCode", "" + BuildConfig.VERSION_CODE);
        versionInfo.put("versionName", BuildConfig.VERSION_NAME);

        return versionInfo;
    }

    public static String getNetworkStatus(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (null != activeNetwork) {
            if(activeNetwork.getType() == ConnectivityManager.TYPE_WIFI)
                return "WIFI";

            if(activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE || activeNetwork.getType() == ConnectivityManager.TYPE_WIMAX)
                return "DATA";
        }
        return null;
    }

    public static Boolean isServiceRunning(Context context, String serviceName) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo runningServiceInfo : activityManager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceName.equals(runningServiceInfo.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    public static String GetDevicesUUID(Context context) {
        UUID uuid = null;
        final String androidId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        try {
            if (!"9774d56d682e549c".equals(androidId)) {
                uuid = UUID.nameUUIDFromBytes(androidId.getBytes("utf8"));
            } else {
                final String deviceId = ((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
                uuid = deviceId != null ? UUID.nameUUIDFromBytes(deviceId.getBytes("utf8")) : UUID.randomUUID();
            }
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }

        return uuid.toString();
    }

    public static boolean isTelephonyEnabled(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return tm != null && tm.getSimState()==TelephonyManager.SIM_STATE_READY;
    }

    public static boolean checkPlayServices(Context context) {
        int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

        /*
        try {
            GoogleApiAvailability googleAPI = GoogleApiAvailability.getInstance();
            int result = googleAPI.isGooglePlayServicesAvailable(context);
            if (result != ConnectionResult.SUCCESS) {
                if (googleAPI.isUserResolvableError(result)) {
                    googleAPI.getErrorDialog((Activity) context, result,
                            PLAY_SERVICES_RESOLUTION_REQUEST).show();
                } else {
                    Log.e("AppInfoManager", "This device is not supported.");
                    ((Activity)context).finish();
                }

                return false;
            }
        } catch (Exception e) {}
        */

        return true;
    }

    public static String getPhoneNumber(Context context) {
        String phoneNum = null;

        try {
            TelephonyManager telManager = (TelephonyManager) context.getSystemService(context.TELEPHONY_SERVICE);
            phoneNum = telManager.getLine1Number();
            if (phoneNum.startsWith("+82"))
                phoneNum = phoneNum.replace("+82", "0");
        } catch (Exception e) {}

        return phoneNum;
    }

    public static int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    public static String getLauncherClassName(Context context) {
        try {
            PackageManager pm = context.getPackageManager();

            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_LAUNCHER);

            if (pm != null) {
                List<ResolveInfo> resolveInfos = pm.queryIntentActivities(intent, 0);
                for (ResolveInfo resolveInfo : resolveInfos) {
                    String pkgName = resolveInfo.activityInfo.applicationInfo.packageName;
                    if (pkgName.equalsIgnoreCase(context.getPackageName())) {
                        String className = resolveInfo.activityInfo.name;
                        return className;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
    public static void updateIconBadge(Context context, int notiCnt) {
        if (context != null) {
            String packageName = context.getPackageName();
            String launcherClassName = getLauncherClassName(context);

            if (!TextUtils.isEmpty(packageName) && !TextUtils.isEmpty(launcherClassName)) {
                Intent badgeIntent = new Intent("android.intent.action.BADGE_COUNT_UPDATE");
                badgeIntent.putExtra("badge_count", notiCnt);
                badgeIntent.putExtra("badge_count_package_name", packageName);
                badgeIntent.putExtra("badge_count_class_name", launcherClassName);
                context.sendBroadcast(badgeIntent);
            }
        }
    }

    public static boolean isUnderKitkat() {
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT)
            return true;

        return false;
    }

    public static Integer getVersionNameRemoveDot(String versionName) {
        if (!TextUtils.isEmpty(versionName)) {
            String[] vers = versionName.split("\\.");

            if (vers.length == 3) {
                try {
                    String majorVer = vers[0];
                    String middleVer = String.format("%02d", Integer.parseInt(vers[1]));
                    String minorVer = String.format("%02d", Integer.parseInt(vers[2]));

                    return Integer.parseInt(majorVer + middleVer + minorVer);
                } catch (Exception e) {}
            }
        }

        return 0;
    }

    public static int getAppVersionInt(Context context) {
        // 현재 앱 버전
        HashMap<String, String> versionInfo = getVersionInfo(context);
        return getVersionNameRemoveDot(versionInfo.get("versionName"));
    }

    /**
     * Returns whether the SDK is the Jellybean release or later.
     */
    public static boolean isJellybeanOrLater() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN;
    }

    /**
     * Returns whether the SDK is the KeyLimePie release or later.
     */
    public static boolean isKeyLimePieOrLater() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
    }


}
