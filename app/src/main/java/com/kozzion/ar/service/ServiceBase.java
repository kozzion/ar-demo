package com.kozzion.ar.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.kozzion.ar.event.EventEmpty;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public abstract class ServiceBase extends Service {
	
	@Nullable
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	
	protected String TAG;
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// If we get killed, after returning from here, restart
		if(!EventBus.getDefault().isRegistered(this)) {
			EventBus.getDefault().register(this);
		}
		TAG = this.getClass().getName();
		return START_STICKY;
	}
	
	@Subscribe
	public void onEvent(EventEmpty event) {
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
		TAG = this.getClass().getName();
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		EventBus.getDefault().unregister(this);
	}
	
}
