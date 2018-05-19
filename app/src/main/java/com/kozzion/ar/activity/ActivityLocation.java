package com.kozzion.ar.activity;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Button;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.kozzion.ar.R;
import com.kozzion.ar.adapter.AdapterCoordinate;
import com.kozzion.ar.coordinate.provider.ProviderStellar;
import com.kozzion.ar.coordinate.provider.ProviderTerrestrial;
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

/**
 * Created by jaapo on 17-5-2018.
 */

public class ActivityLocation extends ActivityBase {


    @BindView(R.id.location_recycler)
    RecyclerView mRecycler;

    private AdapterCoordinate mAdapterCoordinate;

    public static void start(Context context) {
        context.startActivity( new Intent(context, ActivityLocation.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_locations);


        mAdapterCoordinate = new AdapterCoordinate();
        mAdapterCoordinate.setData(ProviderStellar.getCoordinateList());


        mRecycler.setLayoutManager(new LinearLayoutManager(this));
        //mRecycler.setItemAnimator(new DefaultItemAnimator());
        mRecycler.setAdapter(mAdapterCoordinate);

    }


    @Subscribe
    public void onEvent(EventUpdateCurrentLocation event) {

    }

}
