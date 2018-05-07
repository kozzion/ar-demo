package com.kozzion.ar.event;

import com.kozzion.ar.model.ModelARNode;

import java.util.ArrayList;

/**
 * Created by jaapo on 22-4-2018.
 */

public class EventARNodeListUpdate {

    public ArrayList<ModelARNode> mARNodeList;

    public EventARNodeListUpdate(ArrayList<ModelARNode> arNodeList) {
        mARNodeList = new ArrayList<>(arNodeList);
    }
}
