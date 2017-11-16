package com.greason.autotest.utils;

import android.util.Log;

/**
 * Created by Greason on 12/09/2017.
 */

public class LogUtils {

    private static boolean debug = true;

    public static void d(int des) {
        d(String.valueOf(des));
    }

    public static void d(String des) {
        if (debug) {
            Log.d("greasonLog", des);
        }
    }
}
