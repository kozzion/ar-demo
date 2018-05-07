package com.kozzion.ar.event;

import android.location.Location;

/**
 * Created by jaapo on 22-4-2018.
 */

public class EventUpdateCurrentLocation {
    public Location mLocation;

    public EventUpdateCurrentLocation(Location location)
    {
        mLocation = location;
    }
}
