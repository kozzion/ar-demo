package com.kozzion.ar.service;

import android.content.Context;
import android.content.Intent;

import com.google.android.gms.maps.model.LatLng;
import com.kozzion.ar.event.EventRequestARNodeListAdd;
import com.kozzion.ar.event.EventRequestARNodeListClear;
import com.kozzion.ar.event.EventRequestARNodeListUpdate;
import com.kozzion.ar.event.EventARNodeListUpdate;
import com.kozzion.ar.model.ModelARNode;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

/**
 * Created by jaapo on 22-4-2018.
 */

public class ServiceARNode extends ServiceBase {

    ArrayList<ModelARNode> mARNodeList;

    @Override
    public void onCreate() {
        mARNodeList = new ArrayList<>();
        mARNodeList.add(new ModelARNode("Medellin centre", new LatLng(6.255096, -75.582328), 1600));

        EventBus.getDefault().post(new EventARNodeListUpdate(mARNodeList));
        //mARNodeList.add(new ModelARNode("Medellin centre 0", new LatLng(6.255096, -75.582328), 0));
        //mARNodeList.add(new ModelARNode("Linh Ung Pagoda", new LatLng(16.1072989, 108.2343984), 0));
    }

    public static void init(Context context) {
        context.startService(new Intent(context, ServiceARNode.class));

    }

    @Subscribe
    public void onEvent(EventRequestARNodeListAdd event) {
        mARNodeList.add(event.mARNode);
        EventBus.getDefault().post(new EventARNodeListUpdate(mARNodeList));
    }

    @Subscribe
    public void onEvent(EventRequestARNodeListClear event) {
        mARNodeList.clear();
        EventBus.getDefault().post(new EventARNodeListUpdate(mARNodeList));
    }

    @Subscribe
    public void onEvent(EventRequestARNodeListUpdate event) {
        EventBus.getDefault().post(new EventARNodeListUpdate(mARNodeList));
    }


}
