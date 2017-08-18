package com.ifair.fragment;


import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.customtabs.CustomTabsIntent;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.ifair.R;
import com.ifair.activity.MagazineDetailActivity;
import com.ifair.activity.ScaleImageActivity;
import com.ifair.api.CloudAPI;
import com.ifair.myUtil.AppUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 廣告的Fragment
 */
public class ADFragment extends BaseFragment {

    private static final String TAG = "ADFragment";

    @BindView(R.id.img_ad)
    ImageView imgAd;
    private String imageId = "";
    private String link_page = "";
    private String ad_id = "";


    public static ADFragment getInstance(String imageId, String link_page,String ad_id) {
        ADFragment adFragment = new ADFragment();
        Bundle bundle = new Bundle();
        bundle.putString("ImageId", imageId);
        bundle.putString("Link_page", link_page);
        bundle.putString("ad_id", ad_id);
        adFragment.setArguments(bundle);

        return adFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        imageId = getArguments().getString("ImageId");
        link_page = getArguments().getString("Link_page");
        ad_id= getArguments().getString("ad_id");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ad, container, false);
        ButterKnife.bind(this, view);
        initParam();
        return view;
    }

    @Override
    protected void initParam() {

        Glide.with(getActivity()).load(imageId).fitCenter().into(imgAd);
    }


    @OnClick(R.id.img_ad)
    public void onClick() {
        if (userEmail==null){
            getLoginResponse();
            cloudAPI = CloudAPI.getInstance();
        }
        cloudAPI.onAddAdClick(userEmail.equals("") ? AppUtil.getMacAddress() : userEmail,ad_id,getActivity());
        if (!link_page.equals("")) {
            CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
            builder.setToolbarColor(getResources().getColor(R.color.colorAccent));
            builder.setCloseButtonIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_arrow_back));
            CustomTabsIntent customTabsIntent = builder.build();
            customTabsIntent.launchUrl(getActivity(), Uri.parse(link_page));
        }else {
            //放大照片
            Bundle bundle = new Bundle();
            bundle.putString("imgUrl",imageId);
            goToActivity(ScaleImageActivity.class, bundle, 1);
        }
    }
}
