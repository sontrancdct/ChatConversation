package com.example.appchat.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.appchat.config.AppConfig;

public class SharePUtils {
    private static SharePUtils sharePUtils;
    private SharedPreferences sharedPreferences;

    public static SharePUtils getInstance(){
        if (sharePUtils == null){
            sharePUtils = new SharePUtils();
        }
        return sharePUtils;
    }

    public SharePUtils() {
        sharedPreferences = AppConfig.getContext().getSharedPreferences("Data", Context.MODE_PRIVATE);
    }

    public void putData(String key,String value){
        sharedPreferences.edit()
                .putString(key,value)
                .apply();
    }

    public String get(String key){
        return sharedPreferences.getString(key,null);
    }
}
