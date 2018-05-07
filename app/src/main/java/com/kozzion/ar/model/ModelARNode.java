package com.kozzion.ar.model;

import com.google.android.gms.maps.model.LatLng;
import com.kozzion.ar.coordinate.CoordinateWGS84;

/**
 * Created by jaapo on 20-4-2018.
 */

public class ModelARNode {

    public static final int LocationTypePoint = 1;
    public static final int LocationTypeContour = 2;
    public static final int LocationTypeGeom = 3;

    public String mName;
    public CoordinateWGS84 mCoordinateWGS84;
    private int mType;


    public ModelARNode(String name, LatLng location, double altitude)
    {
        mName = name;
        mCoordinateWGS84 = new CoordinateWGS84(location.latitude, location.longitude, altitude);
        mType = LocationTypePoint;
    }
}
