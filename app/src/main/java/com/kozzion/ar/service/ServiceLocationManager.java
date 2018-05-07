package com.kozzion.ar.service;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.kozzion.ar.event.EventPermissionChanged;
import com.kozzion.ar.event.EventUpdateCurrentLocation;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/**
 * Created by jaapo on 22-4-2018.
 */

public class ServiceLocationManager extends ServiceBase implements LocationListener {

    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 0; // 10 meters
    private static final long MIN_TIME_BW_UPDATES = 0;//1000 * 60 * 1; // 1 minute

    private LocationManager locationManager;
    private Location mLocation;

    boolean isGPSEnabled;
    boolean isNetworkEnabled;
    boolean locationServiceAvailable;

    @Override
    public void onCreate() {

        tryStartLocationManager();
    }

    public static void init(Context context) {
        context.startService(new Intent(context, ServiceLocationManager.class));
    }

    @Subscribe
    public void onEvent(EventPermissionChanged event) {
        Log.e(TAG, "EventPermissionChanged");
        tryStartLocationManager();
    }

    public void tryStartLocationManager() {
        Log.e(TAG, "tryStartLocationManager");
        if ( Build.VERSION.SDK_INT >= 23 &&
                ContextCompat.checkSelfPermission( this, Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED) {
            return  ;
        }

        try   {
            this.locationManager = (LocationManager) this.getSystemService(this.LOCATION_SERVICE);

            // Get GPS and network status
            this.isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            this.isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (!isNetworkEnabled && !isGPSEnabled)    {
                // cannot get location
                this.locationServiceAvailable = false;
            }

            this.locationServiceAvailable = true;

            if (isNetworkEnabled) {
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                        MIN_TIME_BW_UPDATES,
                        MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                if (locationManager != null)   {
                    onLocationChanged(locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER));
                }
            }

            if (isGPSEnabled)  {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                        MIN_TIME_BW_UPDATES,
                        MIN_DISTANCE_CHANGE_FOR_UPDATES, this);

                if (locationManager != null)  {
                    onLocationChanged(locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER));
                }
            }
        } catch (Exception ex)  {
            Log.e(TAG, ex.getMessage());

        }
    }

    @Override
    public void onLocationChanged(Location location) {
        mLocation = location;
        EventBus.getDefault().post(new EventUpdateCurrentLocation(mLocation));
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }
}
