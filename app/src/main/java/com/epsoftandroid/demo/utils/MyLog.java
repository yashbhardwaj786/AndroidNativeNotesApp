package com.epsoftandroid.demo.utils;

import android.util.Log;


public class MyLog {

    private static boolean isDebugEnabled() {
        return true;
    }

    public static void i(String tag, String message) {
        if (message != null && isDebugEnabled())
            Log.i(tag, message);
    }

    public static void d(String tag, String message) {
        if (message != null && isDebugEnabled())
            Log.d(tag, message);
    }

    public static void w(String tag, String message) {
        if (message != null && isDebugEnabled())
            Log.w(tag, message);
    }

    public static void v(String tag, String message) {
        if (message != null && isDebugEnabled())
            Log.v(tag, message);
    }

    public static void e(String tag, String message) {
        if (message != null && isDebugEnabled())
            Log.e(tag, message);
    }
}
