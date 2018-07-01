package com.inc.os_i.journalize.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;

 public class FieldValidationUtils {
    Context context;

    public FieldValidationUtils(Context context){
        this.context = context;
    }

    public static boolean isValidEntry(String entry){
        Boolean goodEntry = false;
        if (TextUtils.isEmpty(entry)|| entry.trim().equals("")||entry.trim().equals(" ")){
           goodEntry = false;

        }else{
            goodEntry=true;
        }
       return goodEntry;
    }



    public boolean isOnline() {
        ConnectivityManager conMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = conMgr.getActiveNetworkInfo();

        if(netInfo == null || !netInfo.isConnected() || !netInfo.isAvailable()){
            //Toast.makeText(this, "No Internet connection!", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }
}
