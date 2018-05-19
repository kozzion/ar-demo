package com.kozzion.ar.service;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

import com.kozzion.ar.event.EventUpdateOrientation;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by jaapo on 28-4-2018.
 */

public class ServiceOrientation extends ServiceBase {

    private SensorManager mSensorManager;
    private OrientationMode mOrientationMode;

    private SensorEventListener mMagneticUncalibratedListener;
    private SensorEventListener mMagneticListener;
    private SensorEventListener mGravetyListener;
    private SensorEventListener mOrientationListener;
    private SensorEventListener mRotationListeners;

    private float [] mValuesMagneticUncalibrated;
    private float [] mValuesMagnetic;
    private float [] mValuesGravety;
    private float [] mValuesOrientation;
    private float [] mValuesRotation;

    private enum OrientationMode{
        MODE_GRAVIMAGNETIC,
        MODE_GRAVTATIONAL,
        MODE_ORIENTATION,
        MODE_ROTATION,
        MODE_ODOMETRY,
        MODE_FIXED,
    }

    private class SensorEventListenerSimple implements SensorEventListener
    {
        ServiceOrientation mServiceOrientation;
        public SensorEventListenerSimple(ServiceOrientation serviceOrientation){
            mServiceOrientation = serviceOrientation;
        }

        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {
            mServiceOrientation.onSensorChanged(sensorEvent);
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {

        }
    }

    @Override
    public void onCreate() {
        Log.e(TAG, "onCreate:  ");
        mMagneticUncalibratedListener = new SensorEventListenerSimple(this);
        mMagneticListener = new SensorEventListenerSimple(this);
        mGravetyListener = new SensorEventListenerSimple(this);
        mOrientationListener = new SensorEventListenerSimple(this);
        mRotationListeners = new SensorEventListenerSimple(this);

        tryStartLocationManager();
    }

    private void tryStartLocationManager() {
        Log.e(TAG, "tryStartLocationManager:  ");
        mSensorManager = (SensorManager) this.getSystemService(SENSOR_SERVICE);

        mSensorManager.registerListener(mMagneticUncalibratedListener,
                mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD_UNCALIBRATED),
                SensorManager.SENSOR_DELAY_FASTEST);

        mSensorManager.registerListener(mMagneticListener,
                mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD),
                SensorManager.SENSOR_DELAY_FASTEST);

        mSensorManager.registerListener(mGravetyListener,
                mSensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY),
                SensorManager.SENSOR_DELAY_FASTEST);

        mSensorManager.registerListener(mOrientationListener,
                mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION),
                SensorManager.SENSOR_DELAY_FASTEST);

        mSensorManager.registerListener(mRotationListeners,
                mSensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR),
                SensorManager.SENSOR_DELAY_FASTEST);

        Log.e(TAG, "tryStartLocationManager:  ");
    }

    public void setMode(OrientationMode orientationMode)
    {
        //mSensorManager.unregisterListener(this);
        mOrientationMode = orientationMode;

    }

    public static void init(Context context) {
        context.startService(new Intent(context, ServiceLocationManager.class));
    }


    public void onSensorChanged(SensorEvent sensorEvent) {
        Log.e(TAG, "onSensorChanged:  " + sensorEvent.sensor.getType() );
        Log.e(TAG, "onSensorChanged:  " + sensorEvent.values[0] );
        switch (sensorEvent.sensor.getType())
        {
            case Sensor.TYPE_MAGNETIC_FIELD_UNCALIBRATED:
                mValuesMagneticUncalibrated = sensorEvent.values;
                break;

            case Sensor.TYPE_MAGNETIC_FIELD:
                mValuesMagnetic = sensorEvent.values;
                break;

            case Sensor.TYPE_GRAVITY:
                mValuesGravety = sensorEvent.values;
                break;

            case Sensor.TYPE_ORIENTATION:
                mValuesOrientation= sensorEvent.values;
                break;

            case Sensor.TYPE_ROTATION_VECTOR:
                mValuesRotation = sensorEvent.values;
                updateOrientation();
                break;
        }
    }

    public void updateOrientation()
    {
        Log.e(TAG, "updateOrientation: ");
        float [] rotationVector = mValuesRotation.clone();
        float[] rotationMatrix = new float[16];
        SensorManager.getOrientation(rotationMatrix, rotationVector);
        EventBus.getDefault().postSticky(new EventUpdateOrientation(rotationVector, rotationMatrix));
    }



}
