package com.kozzion.ar.service;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

/**
 * Created by jaapo on 28-4-2018.
 */

public class ServiceOrientation extends ServiceBase implements SensorEventListener {

    private SensorManager mSensorManager;

    @Override
    public void onCreate() {

        tryStartLocationManager();
    }

    private void tryStartLocationManager() {
        mSensorManager = (SensorManager) this.getSystemService(SENSOR_SERVICE);
        mSensorManager.registerListener(this,
                mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD_UNCALIBRATED),
                SensorManager.SENSOR_DELAY_FASTEST);
        mSensorManager.registerListener(this,
                mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD),
                SensorManager.SENSOR_DELAY_FASTEST);
        mSensorManager.registerListener(this,
                mSensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY),
                SensorManager.SENSOR_DELAY_FASTEST);
    }

    public static void init(Context context) {
        context.startService(new Intent(context, ServiceLocationManager.class));
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if (sensorEvent.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD_UNCALIBRATED) {
            for (int i = 0; i < sensorEvent.values.length; i++) {
                Log.e(TAG, "onSensorChanged:  " + sensorEvent.values[0] );
            }

        }
        if (sensorEvent.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
            float[] rotationMatrixFromVector = new float[16];
            //float[] projectionMatrix = new float[16];
            //float[] rotatedProjectionMatrix = new float[16];

            //SensorManager.getAltitude()
            SensorManager.getOrientation(rotationMatrixFromVector, sensorEvent.values);


            Log.e(TAG, "onSensorChanged: " + sensorEvent.values[0] + sensorEvent.values[1] + sensorEvent.values[2] + sensorEvent.values[3] );

        }
    }

    public void updateOrientation()
    {

    }



}
