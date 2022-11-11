package com.ess.essandroidbaselibrary.communication;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

import com.ess.essandroidbaselibrary.Interface.OnLocationUpdateListener;
import com.ess.essandroidbaselibrary.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.util.ArrayList;

/**
 * eSS Technologies (pvt) Ltd.
 * All right reserved.
 * <p>
 * Created by Malith on 6/5/2018.
 */

public class LocationHandler implements LocationListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener
{
    private Activity mActivity;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private ArrayList<OnLocationUpdateListener> onLocationUpdateListeners = new ArrayList<>();

    public LocationHandler(Activity mActivity, boolean enableGPSIfDisabled)
    {
        this.mActivity = mActivity;
        buildGoogleApiClient();
        createLocationRequest();

        if (enableGPSIfDisabled)
        {
            statusCheck();
        }
    }

    protected synchronized void buildGoogleApiClient()
    {
        mGoogleApiClient = new GoogleApiClient.Builder(mActivity)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();
    }

    public void startLocationUpdates(Context mActivity)
    {
        if (ActivityCompat.checkSelfPermission(mActivity,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(mActivity,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            return;
        }

        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
    }

    private void stopLocationUpdate(Context mActivity)
    {
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
    }

    //other new Methods but not using right now..
    protected void createLocationRequest()
    {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10000);//set the interval in which you want to get locations
        mLocationRequest.setFastestInterval(5000);//if a location is available sooner you can get it (i.e. another app is using the location services)
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle)
    {
        startLocationUpdates(mActivity);
    }

    @Override
    public void onConnectionSuspended(int i)
    {
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult)
    {
    }

    @Override
    public void onLocationChanged(Location location)
    {
        if(mGoogleApiClient.isConnected())
        {
            for (OnLocationUpdateListener listener: onLocationUpdateListeners)
            {
                if (listener != null)
                {
                    listener.onLocationChange(location);
                }
            }
        }
    }

    public void addOnLocationUpdateListener(OnLocationUpdateListener onLocationUpdateListener)
    {
        this.onLocationUpdateListeners.add(onLocationUpdateListener);
    }

    public void removeOnLocationUpdateListener(OnLocationUpdateListener onLocationUpdateListener)
    {
        if (onLocationUpdateListeners.contains(onLocationUpdateListener))
        {
            this.onLocationUpdateListeners.remove(onLocationUpdateListener);
        }
    }

    private void statusCheck()
    {
        final LocationManager manager = (LocationManager) mActivity.getSystemService(Context.LOCATION_SERVICE);

        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER))
        {
            buildAlertMessageNoGps();
        }
    }

    private void buildAlertMessageNoGps()
    {
        if (mActivity != null)
        {
            mActivity.runOnUiThread(new Runnable()
            {
                @Override
                public void run()
                {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
                    builder.setMessage(mActivity.getString(R.string.main_msg_gps_disabled))
                            .setCancelable(false)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(final DialogInterface dialog, final int id)
                                {
                                    mActivity.startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                public void onClick(final DialogInterface dialog, final int id) {
                                    dialog.cancel();
                                }
                            });

                    final AlertDialog alert = builder.create();
                    alert.show();
                }
            });
        }
    }
}