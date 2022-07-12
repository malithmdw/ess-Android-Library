package com.ess.essandroidbaselibrary.communication;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by Malith on 4/9/2018.
 */

public class InternetConnection
{
    private static Activity mActivity;
    private static InternetConnection INSTANCE;

    public enum NetworkMode
    {
        WI_FI,
        MOBILE;
    }

    private void InternetConnection()
    {

    }

    public static InternetConnection getInstance(Activity activity)
    {
        InternetConnection.mActivity = activity;

        if(INSTANCE == null)
        {
            INSTANCE = new InternetConnection();
        }
        return INSTANCE;
    }

    public boolean isNetworkAvailable()
    {
        ConnectivityManager connectivityManager = (ConnectivityManager) mActivity.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager != null)
        {
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            return (activeNetworkInfo != null && activeNetworkInfo.isAvailable() &&  activeNetworkInfo.isConnected());
        }

        return false;
    }

    private NetworkMode connectedNetworkMode()
    {
        NetworkMode networkMode= null;

        ConnectivityManager connectivityManager = (ConnectivityManager) mActivity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] networkInfoList = connectivityManager.getAllNetworkInfo();
        for (NetworkInfo networkInfo : networkInfoList)
        {
            if (networkInfo.getTypeName().equalsIgnoreCase("WIFI"))
                if (networkInfo.isConnected())
                    networkMode = NetworkMode.WI_FI;
            if (networkInfo.getTypeName().equalsIgnoreCase("MOBILE"))
                if (networkInfo.isConnected())
                    networkMode = NetworkMode.MOBILE;
        }
        return networkMode;
    }
/*
    class NetworkChangeReceiver extends BroadcastReceiver
    {
        @Override
        public void onReceive(final Context context, final Intent intent) {
            final ConnectivityManager connMgr = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);

            final android.net.NetworkInfo wifi = connMgr
                    .getNetworkInfo(ConnectivityManager.TYPE_WIFI);

            final android.net.NetworkInfo mobile = connMgr
                    .getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

            if (wifi.isAvailable() || mobile.isAvailable()) {
                // Do something

                Log.d("Network Available ", "Flag No 1");
            }
        }
    }
    */
}
