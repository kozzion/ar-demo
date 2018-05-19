package com.kozzion.ar.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kozzion.ar.R;
import com.kozzion.ar.coordinate.model.ModelCoordinate;
import com.kozzion.ar.holder.HolderCoordinateRAD;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jaapo on 17-5-2018.
 */

public class AdapterCoordinate extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<ModelCoordinate> mData;

    public AdapterCoordinate() {
        mData = new ArrayList<>();
    }

    public void setData(List<ModelCoordinate> data){
        mData = new ArrayList<>(data);
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        switch (viewType) {
            case ModelCoordinate.TYPE_WGS84:
                View view_wsgs84 = inflater.inflate(R.layout.layout_wgs84, parent, false);
                return new HolderCoordinateRAD(view_wsgs84);
            case ModelCoordinate.TYPE_ECOE:
                View view_ecoe = inflater.inflate(R.layout.layout_ecoe, parent, false);
                return new HolderCoordinateRAD(view_ecoe);
            case ModelCoordinate.TYPE_HCOE:
                View view_hcoe = inflater.inflate(R.layout.layout_hcoe, parent, false);
                return new HolderCoordinateRAD(view_hcoe);
            case ModelCoordinate.TYPE_RAD:
                View view_rad = inflater.inflate(R.layout.layout_rad, parent, false);
                return new HolderCoordinateRAD(view_rad);
            default:
                throw new RuntimeException("Unknown type:" + viewType);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == ModelCoordinate.TYPE_WGS84) {
            ((HolderCoordinateRAD) holder).bind(mData.get(position));
        } else if (getItemViewType(position) == ModelCoordinate.TYPE_ECOE) {
            ((HolderCoordinateRAD) holder).bind(mData.get(position));
        } else if (getItemViewType(position) == ModelCoordinate.TYPE_HCOE) {
            ((HolderCoordinateRAD) holder).bind(mData.get(position));
        } else if (getItemViewType(position) == ModelCoordinate.TYPE_RAD) {
            ((HolderCoordinateRAD) holder).bind(mData.get(position));
        }
    }

    @Override
    public int getItemViewType(int position) {
        return mData.get(position).getType();
    }


    @Override
    public int getItemCount() {
        return mData.size();
    }
}
