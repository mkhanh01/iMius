package com.example.imius.data;

import android.content.Context;
import android.content.SharedPreferences;

public class MySharedPreferences {
    private static final String MY_SHARED_PREFERENCES = "MY_SHARED_PREFERENCES";
    private Context mContext;

    public MySharedPreferences(Context mContext) {
        this.mContext = mContext;
    }

    public void clearShare (){
        SharedPreferences settings = mContext.getSharedPreferences("MY_SHARED_PREFERENCES", Context.MODE_PRIVATE);
        settings.edit().clear().commit();
    }

    public void putStringValue(String key, String value) {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences
                (MY_SHARED_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public void putIntValue(String key, int value) {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences
                (MY_SHARED_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(key, value);
        editor.apply();
    }

    public String getStringValue(String key) {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences
                (MY_SHARED_PREFERENCES, Context.MODE_PRIVATE);
        return sharedPreferences.getString(key, "");
    }

    public void putBoolean(String key, boolean value) {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences
                (MY_SHARED_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    public boolean getBooleanValue(String key) {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences
                (MY_SHARED_PREFERENCES, Context.MODE_PRIVATE);

        return sharedPreferences.getBoolean(key, false);
    }
    public int getIntValue(String key) {
        SharedPreferences sharedPreferences = mContext.getSharedPreferences
                (MY_SHARED_PREFERENCES, Context.MODE_PRIVATE);

        return sharedPreferences.getInt(key, 0);
    }
}
