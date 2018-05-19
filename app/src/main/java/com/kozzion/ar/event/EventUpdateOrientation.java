package com.kozzion.ar.event;

/**
 * Created by jaapo on 19-5-2018.
 */

public class EventUpdateOrientation {
    public float [] mRotationVector;
    public float [] mRotationMatrix;

    public EventUpdateOrientation(float [] rotationVector, float [] rotationMatrix)
    {
        mRotationVector = rotationVector;
        mRotationMatrix = rotationMatrix;
    }

}
