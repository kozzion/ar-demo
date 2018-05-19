package com.kozzion.ar.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.opengl.Matrix;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceView;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.kozzion.ar.coordinate.CoordinateWGS84;
import com.kozzion.ar.event.EventPermissionChanged;
import com.kozzion.ar.event.EventRequestARNodeListUpdate;
import com.kozzion.ar.event.EventUpdateCurrentLocation;
import com.kozzion.ar.event.EventARNodeListUpdate;
import com.kozzion.ar.R;
import com.kozzion.ar.event.EventUpdateOrientation;
import com.kozzion.ar.view.ARCamera;
import com.kozzion.ar.view.ViewOverlayAR;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.BindView;

public class ActivityCamera extends ActivityBase {

    private final static int REQUEST_CAMERA_PERMISSIONS_CODE = 11;
    public static final int REQUEST_LOCATION_PERMISSIONS_CODE = 0;


    @BindView(R.id.camera_button_map)
    Button mButtonMap;

    @BindView(R.id.camera_surface_view)
    SurfaceView surfaceView;

    @BindView(R.id.camera_layout_container)
    FrameLayout cameraContainerLayout;



    @BindView(R.id.camera_text_location)
    TextView mTextLocation;

    //@BindView(R.id.activity_ar)
    ViewOverlayAR mViewOverlay;

    private Camera mCamera;
    private ARCamera mARCamera;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        mButtonMap.setOnClickListener(view -> ActivityMap.start(ActivityCamera.this));
        mViewOverlay = new ViewOverlayAR(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        EventBus.getDefault().post(new EventRequestARNodeListUpdate());
        requestLocationPermission();
        requestCameraPermission();
        initAROverlayView();
    }

    @Override
    public void onPause() {
        releaseCamera();
        super.onPause();
    }

    public void requestCameraPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                this.checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            this.requestPermissions(new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSIONS_CODE);
        } else {
            initARCameraView();
        }
    }

    public void requestLocationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                this.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            this.requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_PERMISSIONS_CODE);
        } else {
            Log.e(TAG, "requestLocationPermission");
            EventBus.getDefault().post(new EventPermissionChanged());
        }
    }

    public void initAROverlayView() {
        if (mViewOverlay.getParent() != null) {
            ((ViewGroup) mViewOverlay.getParent()).removeView(mViewOverlay);
        }
        cameraContainerLayout.addView(mViewOverlay);
    }

    public void initARCameraView() {
        reloadSurfaceView();

        if (mARCamera == null) {
            mARCamera = new ARCamera(this, surfaceView);
        }
        if (mARCamera.getParent() != null) {
            ((ViewGroup) mARCamera.getParent()).removeView(mARCamera);
        }
        cameraContainerLayout.addView(mARCamera);
        mARCamera.setKeepScreenOn(true);
        initCamera();
    }

    private void initCamera() {
        int numCams = Camera.getNumberOfCameras();
        if(numCams > 0){
            try{
                mCamera = Camera.open();
                mCamera.startPreview();
                mARCamera.setCamera(mCamera);
            } catch (RuntimeException ex){
                Toast.makeText(this, "Camera not found", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void reloadSurfaceView() {
        if (surfaceView.getParent() != null) {
            ((ViewGroup) surfaceView.getParent()).removeView(surfaceView);
        }

        cameraContainerLayout.addView(surfaceView);
    }

    private void releaseCamera() {
        if(mCamera != null) {
            mCamera.setPreviewCallback(null);
            mCamera.stopPreview();
            mARCamera.setCamera(null);
            mCamera.release();
            mCamera = null;
        }
    }

    @Subscribe
    public void onEvent(EventUpdateCurrentLocation event) {
        //Log.e(TAG, "EventUpdateCurrentLocation");
        if (mViewOverlay !=null) {
            Location location = event.mLocation;
            mViewOverlay.updateCurrentLocation(new CoordinateWGS84(location.getLatitude(), location.getLongitude(), location.getAltitude()));
            mTextLocation.setText(String.format("lat: %s \nlon: %s \naltitude: %s \n",
                    location.getLatitude(), location.getLongitude(), location.getAltitude()));
        }
    }

    @Subscribe
    public void onEvent(EventARNodeListUpdate event) {

        if (mViewOverlay !=null) {
            //mViewOverlay.updateARNodeList(event.mARNodeList);
        }
    }

    @Subscribe
    public void onEvent(EventUpdateOrientation event) {
        Log.e(TAG, "EventUpdateOrientation");
        if ((mViewOverlay !=null) && (mARCamera != null)) {
            float[] projectionMatrix = new float[16];
            float[] rotatedProjectionMatrix = new float[16];
            if (mARCamera != null) {
                projectionMatrix = mARCamera.getProjectionMatrix();
            }

            Matrix.multiplyMM(rotatedProjectionMatrix, 0, projectionMatrix, 0, event.mRotationMatrix, 0);
            this.mViewOverlay.updateRotatedProjectionMatrix(rotatedProjectionMatrix);
        }
    }
}
