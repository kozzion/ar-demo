package com.kozzion.ar.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.kozzion.ar.R;
import com.kozzion.ar.coordinate.model.ModelCoordinate;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by jaapo on 17-5-2018.
 */

public class HolderCoordinateRAD extends RecyclerView.ViewHolder {

    private View mRootview;

    @BindView(R.id.rad_text_name)
    TextView mTextName;

    @BindView(R.id.rad_text_ra_radians)
    TextView mRARadians;

    @BindView(R.id.rad_text_d_radians)
    TextView mDRadians;

    public HolderCoordinateRAD(View rootview) {
        super(rootview);
        mRootview = rootview;
        ButterKnife.bind(this, rootview);
    }

    public void bind(ModelCoordinate modelCoordinate) {
        Locale fmtLocale = Locale.getDefault();

        mTextName.setText(modelCoordinate.mName);
        mRARadians.setText(String.format(fmtLocale, "%.2f", modelCoordinate.mCoordinateStellar.mRightAscensionRadians));
        mDRadians.setText(String.format(fmtLocale, "%.2f",modelCoordinate.mCoordinateStellar.mDeclenationRadians));

    }
}
