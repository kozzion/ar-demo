package com.kozzion.ar.event;

/**
 * Created by jaapo on 19-5-2018.
 */

public class EventUpdateOrientation {
    public float mRotationVector0;
    public float mRotationVector1;
    public float mRotationVector2;
    public float mRotationVector3;
    public float mRotationVector4;

    public float [] getRotationVector(){
        return new float[] {mRotationVector0, mRotationVector1, mRotationVector2, mRotationVector3, mRotationVector4};
    }

    public EventUpdateOrientation(float [] rotationVector)
    {
        mRotationVector0 = rotationVector[0];
        mRotationVector1 = rotationVector[1];
        mRotationVector2 = rotationVector[2];
        mRotationVector3 = rotationVector[3];
        mRotationVector4 = rotationVector[4];
    }

}
