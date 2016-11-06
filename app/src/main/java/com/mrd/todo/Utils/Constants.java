package com.mrd.todo.Utils;

import android.util.Log;

import com.mrd.todo.BuildConfig;

/**
 * Created by mayurdube on 03/11/16.
 */

public class Constants {
    public static final String API = "https://dl.dropboxusercontent.com/u/6890301/tasks.json";


    public static void log(String tag, String message) {
        Log.d(BuildConfig.APPLICATION_ID, "[" + tag + "]" + message);
    }
}
