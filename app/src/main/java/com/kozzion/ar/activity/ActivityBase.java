package com.kozzion.ar.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;


import com.kozzion.ar.event.EventEmpty;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.ButterKnife;

/**
 * Created by jaapo on 23-9-2017.
 */

public class ActivityBase extends AppCompatActivity {
    
    protected String TAG;

    @Override
    public void onContentChanged() {
        super.onContentChanged();
        ButterKnife.bind(this);
    }
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TAG = this.getClass().getName();
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        EventBus.getDefault().register(this);
    
    }
    
    @Override
    protected void onPause() {
        super.onPause();
        EventBus.getDefault().unregister(this);
    }
    
    @Subscribe
    public void onEvent(EventEmpty event) {
    }

}
