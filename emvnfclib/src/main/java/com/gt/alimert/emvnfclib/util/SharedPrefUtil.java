package com.gt.alimert.emvnfclib.util;

import android.content.Context;

/**
 * @author AliMertOzdemir
 * @class SharedPrefUtil
 * @created 21.04.2020
 * @copyright © GARANTI TEKNOLOJI
 */
public final class SharedPrefUtil {

    private static final String PREF_APP = "pref_lib";
    private static final String STAN = "stan";

    public static int getStan(Context context) {
        int stan = context.getSharedPreferences(PREF_APP, Context.MODE_PRIVATE).getInt(STAN, 1);
        saveStan(context, stan);
        return stan;
    }

    private static void saveStan(Context context, int stan) {
        if(stan >= 999999)
            stan = 0;
        context.getSharedPreferences(PREF_APP, Context.MODE_PRIVATE).edit().putInt(STAN, stan + 1).apply();
    }
}
