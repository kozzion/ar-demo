package com.kozzion.ar.activity;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.kozzion.ar.R;
import com.kozzion.ar.event.EventRequestARNodeListAdd;
import com.kozzion.ar.event.EventRequestARNodeListClear;
import com.kozzion.ar.event.EventUpdateCurrentLocation;
import com.kozzion.ar.model.ModelARNode;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import butterknife.BindView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ActivityMap extends ActivityBase implements OnMapReadyCallback, GoogleMap.OnMapClickListener {

    @BindView(R.id.map_button_centre)
    Button mButtonCentre;

    @BindView(R.id.map_button_clear)
    Button mButtonClear;

    private SupportMapFragment mMapFragment;
    private GoogleMap mMap;
    private Location mLocation;

    public static void start(Context context) {
        context.startActivity( new Intent(context, ActivityMap.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        mMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map_map_view);
        mMapFragment.getMapAsync(this);
        mButtonCentre.setOnClickListener(view -> centreMap());
        mButtonClear.setOnClickListener(view -> clearMap());
    }

    private void clearMap() {
        EventBus.getDefault().post(new EventRequestARNodeListClear());
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        // Add a marker in Sydney, Australia, and move the camera.
        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        mMap.setOnMapClickListener(this);
    }

    @Subscribe
    public void onEvent(EventUpdateCurrentLocation event) {
        if(mLocation == null) {
            mLocation = event.mLocation;
            centreMap();
        } else {
            mLocation = event.mLocation;
        }
    }

    public void centreMap(){
        if((mLocation != null ) &&(mMap != null)){
            LatLng newLocation = new LatLng(mLocation.getLatitude(),mLocation.getLongitude());
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(newLocation, 12));
        }
    }


    @Override
    public void onMapClick(LatLng latLng) {
        String name = "name";
        mMap.addMarker(new MarkerOptions().position(latLng).title(name));
        getAltitudeAndAdd(name, latLng);

    }

    //http://square.github.io/okhttp/
    //https://maps.googleapis.com/maps/api/elevation/json?locations=39.7391536,-104.9847034&key=AIzaSyCmHOZsRalDiVEAS69Swub0AZnNXPGo7nE

    private void getAltitudeAndAdd(String name, LatLng latLng) {
        String apiKey = "AIzaSyCmHOZsRalDiVEAS69Swub0AZnNXPGo7nE";
        String url = "https://maps.googleapis.com/maps/api/elevation/json?locations=" + Double.toString(latLng.latitude) + "," + Double.toString(latLng.longitude) +"&key=" + apiKey;
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(url)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "onFailure:");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                try {
                    Log.e(TAG, "onResponse:");
                    JSONObject Jobject = new JSONObject(response.body().string());
                    double altitude = Jobject.getJSONArray("results").getJSONObject(0).getDouble("elevation");
                    ModelARNode node = new ModelARNode("Name",latLng, altitude);
                    EventBus.getDefault().post(new EventRequestARNodeListAdd(node));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

}
