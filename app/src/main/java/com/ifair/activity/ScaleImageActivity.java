package com.ifair.activity;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;

import com.github.piasy.biv.BigImageViewer;
import com.github.piasy.biv.indicator.ProgressIndicator;
import com.github.piasy.biv.loader.glide.GlideImageLoader;
import com.github.piasy.biv.view.BigImageView;
import com.ifair.R;
import com.orhanobut.logger.Logger;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ScaleImageActivity extends BaseActivity {

    @BindView(R.id.activity_scale_image)
    RelativeLayout activityScaleImage;
    @BindView(R.id.img_scale)
    BigImageView imgScale;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BigImageViewer.initialize(GlideImageLoader.with(this));
        setContentView(R.layout.activity_scale_image);
        ButterKnife.bind(this);
        initParam();

    }

    @Override
    protected void initParam() {
        imgScale.showImage(Uri.parse(getIntent().getExtras().getString("imgUrl")));
    }
}
