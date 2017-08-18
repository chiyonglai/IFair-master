package com.ifair.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.ifair.R;
import com.ifair.activity.ScaleImageActivity;
import com.ifair.myUtil.AppUtil;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 最新消息內頁，廣告輪播
 */
public class ADNewFragment extends BaseFragment {

    private static final String TAG = "ADNewFragment";


    @BindView(R.id.img_ad)
    ImageView imgAd;

    private String imageId = "";
    private String url = "";

    public static ADNewFragment getInstance(String id, String url) {
        ADNewFragment adFragment = new ADNewFragment();
        Bundle bundle = new Bundle();
        bundle.putString("ImageId", id);
        bundle.putString("url", url);
        adFragment.setArguments(bundle);

        return adFragment;

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        imageId = getArguments().getString("ImageId");
        url = getArguments().getString("url");
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_adnew, container, false);
        ButterKnife.bind(this, view);
        initParam();
        return view;
    }

    @Override
    protected void initParam() {
        Glide.with(getActivity()).load(imageId).centerCrop().into(imgAd);
    }


    @OnClick(R.id.img_ad)
    public void onViewClicked() {
        if (Objects.equals(url, "")) {
            Bundle bundle = new Bundle();
            bundle.putString("imgUrl", imageId);
            goToActivity(ScaleImageActivity.class, bundle);
        } else {
            AppUtil.intentUrl(getActivity(), url);
        }
    }
}
