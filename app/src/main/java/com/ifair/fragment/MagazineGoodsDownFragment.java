package com.ifair.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ideabus.ideabuslibrary.util.SizeUtils;
import com.ifair.R;
import com.ifair.activity.MagazineDetailActivity;
import com.ifair.myUtil.AppUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Magazine內頁，推薦商品
 */
public class MagazineGoodsDownFragment extends BaseFragment {

    private static final String TAG = "MagazineGoodsDownFragment";
    @BindView(R.id.img_goods)
    ImageView imgGoods;
    //@BindView(R.id.tv_firm_name)      //2017/08/18不在使用
    //TextView tvFirmName;              //2017/08/18不在使用
    @BindView(R.id.tv_goods_name)
    TextView tvGoodsName;
    //@BindView(R.id.tv_goods_price)          //2017/08/18不在使用
    //TextView tvGoodsPrice;          //2017/08/18不在使用
    @BindView(R.id.tv_goods_price_yellow)
    TextView tvGoodsPriceYellow;


    private String firm_name;
    private String goods_id;
    private String goods_name;
    private String goods_image2;
    private String original_price;
    private String price;

    public static MagazineGoodsDownFragment getInstance(String firm_name, String goods_id, String goods_name, String goods_image2, String original_price, String price) {
        MagazineGoodsDownFragment adFragment = new MagazineGoodsDownFragment();
        Bundle bundle = new Bundle();
        bundle.putString("firm_name", firm_name);
        bundle.putString("goods_id", goods_id);
        bundle.putString("goods_name", goods_name);
        bundle.putString("goods_image2", goods_image2);
        bundle.putString("original_price", original_price);
        bundle.putString("price", price);

        adFragment.setArguments(bundle);

        return adFragment;

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firm_name = getArguments().getString("firm_name");
        goods_id = getArguments().getString("goods_id");
        goods_name = getArguments().getString("goods_name");
        goods_image2 = getArguments().getString("goods_image2");
        original_price = getArguments().getString("original_price");
        price = getArguments().getString("price");
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_magazine_goods_down, container, false);
        ButterKnife.bind(this, view);
        initParam();
        return view;
    }

    @Override
    protected void initParam() {
        Glide.with(getActivity()).load(goods_image2).centerCrop().into(imgGoods);
      //  Glide.with(getActivity()).load("http://35.164.137.155/uploads/goods_image/e3dde1ead3b82888c537801afbd75bdc.png").centerCrop().into(imgGoods);
        //tvFirmName.setText(firm_name);    //2017/08/18不在使用
        tvGoodsName.setText(goods_name);
        //tvFirmName.setText(firm_name);    //2017/08/18不在使用
        //tvGoodsPrice.setText(getString(R.string.magazine_price, AppUtil.numberFormat(String.valueOf(Integer.valueOf(original_price)))));
        if(Integer.valueOf(price) == 0) {
            //2017/9/5價格零不顯示
            tvGoodsPriceYellow.setText("");
        } else {
            tvGoodsPriceYellow.setText(getString(R.string.magazine_price, AppUtil.numberFormat(String.valueOf(Integer.valueOf(price)))));
        }
    }


    @OnClick(R.id.img_goods)
    public void onClick() {
        Bundle bundle = new Bundle();
        bundle.putString("Goods_id", goods_id);
        goToActivity(MagazineDetailActivity.class, bundle);

    }
}
