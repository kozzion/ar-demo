package com.kozzion.ar;

import android.app.Application;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

import com.kozzion.ar.service.ServiceARNode;
import com.kozzion.ar.service.ServiceLocationManager;
import com.kozzion.ar.service.ServiceOrientation;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by jaapo on 22-4-2018.
 */

public class ApplicationMain extends Application {

    protected String TAG;

    @Override
    public void onCreate() {
        super.onCreate();
        TAG = this.getClass().getName();
        Log.e(TAG, "onCreate1: ");
        ServiceLocationManager.init(this);
        Log.e(TAG, "onCreate2: ");
        ServiceARNode.init(this);
        Log.e(TAG, "onCreate3: ");
        ServiceOrientation.init(this);
        Log.e(TAG, "onCreate4: ");

        /*StoroBuilder.configure(Const.Build.CACHE_SIZE)
                .setDefaultCacheDirectory(this)
                .initialize();*/

        //performFeatureCheck();
    }

    /**
     * Check what features are available to this phone and report back to analytics
     * so we can judge when to add/drop support.
     */
    private void performFeatureCheck() {
        SensorManager sensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        if (sensorManager == null) {
            Log.e(TAG, "No sensor manager");
        }
        // Minimum requirements
        if (hasDefaultSensor(sensorManager, Sensor.TYPE_ACCELEROMETER)) {
            if (hasDefaultSensor(sensorManager, Sensor.TYPE_MAGNETIC_FIELD)) {
                Log.e(TAG, "Minimal sensors available");
                //analytics.trackEvent(
                //        Analytics.SENSOR_CATEGORY, Analytics.SENSOR_AVAILABILITY, "Minimal Sensors: Yes", 1);
            } else {
                Log.e(TAG, "No magnetic field sensor");
                //analytics.trackEvent(
                //        Analytics.SENSOR_CATEGORY, Analytics.SENSOR_AVAILABILITY, "No Mag Field Sensor", 0);
            }
        } else {
            if (hasDefaultSensor(sensorManager, Sensor.TYPE_MAGNETIC_FIELD)) {
                Log.e(TAG, "No accelerometer");
                //analytics.trackEvent(
                //        Analytics.SENSOR_CATEGORY, Analytics.SENSOR_AVAILABILITY, "No Accel Sensor", 0);
            } else {
                Log.e(TAG, "No magnetic field sensor or accelerometer");
                //analytics.trackEvent(
                //        Analytics.SENSOR_CATEGORY, Analytics.SENSOR_AVAILABILITY, "No Mag Field/Accel Sensors", 0);
            }
        }

        // Check for a particularly strange combo - it would be weird to have a rotation sensor
        // but no accelerometer or magnetic field sensor
        boolean hasRotationSensor = false;
        if (hasDefaultSensor(sensorManager, Sensor.TYPE_ROTATION_VECTOR)) {
            if (hasDefaultSensor(sensorManager, Sensor.TYPE_ACCELEROMETER) && hasDefaultSensor(sensorManager, Sensor.TYPE_MAGNETIC_FIELD)
                    && hasDefaultSensor(sensorManager, Sensor.TYPE_GYROSCOPE)) {
                hasRotationSensor = true;
                //analytics.trackEvent(
                //        Analytics.SENSOR_CATEGORY, Analytics.ROT_SENSOR_AVAILABILITY, "OK - All Sensors", 1);
            } else if (hasDefaultSensor(sensorManager, Sensor.TYPE_ACCELEROMETER) && hasDefaultSensor(
                    sensorManager, Sensor.TYPE_MAGNETIC_FIELD)) {
                // Even though it allegedly has the rotation vector sensor too many gyro-less phones
                // lie about this, so put these devices on the 'classic' sensor code for now.
                hasRotationSensor = false;
                //analytics.trackEvent(
                //        Analytics.SENSOR_CATEGORY, Analytics.ROT_SENSOR_AVAILABILITY, "Disabled - No gyro", 1);
            } else {
                //analytics.trackEvent(
                //        Analytics.SENSOR_CATEGORY, Analytics.ROT_SENSOR_AVAILABILITY, "Disabled - Missing Mag/Accel", 0);
            }
        } else {
            //analytics.trackEvent(
            //        Analytics.SENSOR_CATEGORY, Analytics.ROT_SENSOR_AVAILABILITY, "No rotation", 0);
        }

        //TODO Enable Gyro if available and user hasn't already disabled it.
        //if (!preferences.contains(ApplicationConstants.SHARED_PREFERENCE_DISABLE_GYRO)) {
        //    preferences.edit().putBoolean(
        //            ApplicationConstants.SHARED_PREFERENCE_DISABLE_GYRO, !hasRotationSensor).apply();
        //}

        // Do we at least have defaults for the main ones?
        int[] importantSensorTypes = {Sensor.TYPE_ACCELEROMETER, Sensor.TYPE_GYROSCOPE,
                Sensor.TYPE_MAGNETIC_FIELD, Sensor.TYPE_LIGHT, Sensor.TYPE_ROTATION_VECTOR,
                Sensor.TYPE_ORIENTATION};

        for (int sensorType : importantSensorTypes) {
            if (hasDefaultSensor(sensorManager, sensorType)) {
                Log.e(TAG, "No sensor of type " + sensorType);
            } else {
                Log.e(TAG, "Sensor present of type " + sensorType);
            }
        }

        // Lastly a dump of all the sensors.
        Log.d(TAG, "All sensors:");
        List<Sensor> allSensors = sensorManager.getSensorList(Sensor.TYPE_ALL);
        Set<String> sensorTypes = new HashSet<>();
        for (Sensor sensor : allSensors) {
            Log.e(TAG, sensor.getName());
            //sensorTypes.add(getSafeNameForSensor(sensor));
        }
        /*Log.d(TAG, "All sensors summary:");
        for (String sensorType : sensorTypes) {
            Log.i(TAG, sensorType);
            analytics.trackEvent(
                    Analytics.SENSOR_CATEGORY, Analytics.SENSOR_NAME, sensorType, 1);
        }*/
    }

    private boolean hasDefaultSensor(SensorManager sensorManager, int sensorType) {
        if (sensorManager == null) {
            return false;
        }
        Sensor sensor = sensorManager.getDefaultSensor(sensorType);
        if (sensor == null) {
            return false;
        }
        SensorEventListener dummy = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                // Nothing
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {
                // Nothing
            }
        };
        boolean success = sensorManager.registerListener(
                dummy, sensor, SensorManager.SENSOR_DELAY_UI);
        if (!success) {
            //analytics.trackEvent(
            //        Analytics.SENSOR_CATEGORY, Analytics.SENSOR_LIAR, getSafeNameForSensor(sensor),
            //        1);
        }
        sensorManager.unregisterListener(dummy);
        return success;
    }
}
