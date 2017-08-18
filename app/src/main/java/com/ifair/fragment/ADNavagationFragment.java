package com.ifair.fragment;


import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ideabus.ideabuslibrary.util.SizeUtils;
import com.ifair.R;
import com.ifair.myUtil.AppUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 廣告的Fragment
 */
public class ADNavagationFragment extends BaseFragment {

    private static final String TAG = "ADNavagationFragment";
    @BindView(R.id.iv_background)
    ImageView ivBackground;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_scription)
    TextView tvScription;


    private int imageId;
    private String title;
    private String scription;

    public static ADNavagationFragment getInstance(int imageId, String title, String scription) {
        ADNavagationFragment adFragment = new ADNavagationFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("imageId", imageId);
        bundle.putString("title", title);
        bundle.putString("scription", scription);
        adFragment.setArguments(bundle);
        return adFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        imageId = getArguments().getInt("imageId");
        title = getArguments().getString("title");
        scription = getArguments().getString("scription");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ad_navagation, container, false);
        ButterKnife.bind(this, view);
        adjustUI();
        initParam();
        return view;
    }

    private void adjustUI() {
        getScreenSize();
        RelativeLayout.LayoutParams tvTitleLp = (RelativeLayout.LayoutParams) tvTitle.getLayoutParams();
        tvTitleLp.topMargin=(int)(height*0.4);
        tvTitle.setLayoutParams(tvTitleLp);

        RelativeLayout.LayoutParams tvScriptionLp = (RelativeLayout.LayoutParams) tvScription.getLayoutParams();
        tvScriptionLp.topMargin=(int)(height*0.46);
        tvScription.setLayoutParams(tvScriptionLp);
        tvScription.setMaxEms(20);
        tvScription.setGravity(Gravity.CENTER);
    }



    @Override
    protected void initParam() {
        ivBackground.setImageDrawable(AppUtil.ridToDrawable(getResources(), imageId));
        tvTitle.setText(title);
        tvScription.setText(scription);
    }
}
