package com.sefford.test.core;

import android.util.Log;

import com.sefford.kor.common.interfaces.Loggable;

/**
 * Logger class for Kor library
 */
public class Logger implements Loggable {
    @Override
    public void d(String logtag, String message) {
        Log.d(logtag, message);
    }

    @Override
    public void printPerformanceLog(String logtag, String message, long time) {
        // Do nothing
    }

    @Override
    public void e(String logtag, String message, Throwable throwable) {
        Log.e(logtag, message);
    }
}
