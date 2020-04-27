package com.gt.alimert.emvnfclib.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.provider.Settings;
import android.util.Log;

/**
 * @author AliMertOzdemir
 * @class DeviceUtil
 * @created 27.04.2020
 * @copyright © GARANTI TEKNOLOJI
 */
public final class DeviceUtil {

    private static final String TAG = DeviceUtil.class.getName();

    @SuppressLint("all")
    public static String getAndroidId(Context context) {
        String androidId =  Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        Log.d(TAG, "ANDROID ID --> " + androidId.toUpperCase());
        return androidId.toUpperCase();
    }
}
