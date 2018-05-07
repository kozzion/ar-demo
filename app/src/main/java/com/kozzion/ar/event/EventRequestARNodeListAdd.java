package com.kozzion.ar.event;

import com.kozzion.ar.model.ModelARNode;

/**
 * Created by jaapo on 22-4-2018.
 */

public class EventRequestARNodeListAdd {
    public ModelARNode mARNode;

    public EventRequestARNodeListAdd(ModelARNode arNode)
    {
        mARNode = arNode;
    }
}
