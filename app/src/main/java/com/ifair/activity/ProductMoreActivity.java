package com.ifair.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ifair.R;
import com.ifair.adapter.ADNewsAdapter;
import com.ifair.api.CloudAPI;
import com.ifair.api.CloudCode;
import com.ifair.fragment.ADNewFragment;
import com.ifair.listener.OnGetTrueGoodsListener;
import com.ifair.module.NfcProductResponse;
import com.ifair.myUtil.AppUtil;
import com.ifair.myUtil.MyTimeUtils;
import com.ifair.myUtil.Variable;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ProductMoreActivity extends BaseNFcActivity implements OnGetTrueGoodsListener, AppBarLayout.OnOffsetChangedListener, ViewPager.OnPageChangeListener {


    @BindView(R.id.view_pager_product)
    ViewPager viewPagerProduct;
    @BindView(R.id.viewGroup)
    LinearLayout viewGroup;
    @BindView(R.id.img_title_back)
    ImageView imgTitleBack;
    @BindView(R.id.txt_title)
    TextView txtTitle;
    @BindView(R.id.button_bar)
    RelativeLayout buttonBar;
    @BindView(R.id.img_collapsing_back)
    ImageView imgCollapsingBack;
    @BindView(R.id.appbar_product_more)
    AppBarLayout appbarProductMore;
    @BindView(R.id.txt_product_name)
    TextView txtProductName;
    @BindView(R.id.txt_product_id)
    TextView txtProductId;
    @BindView(R.id.txt_product_date)
    TextView txtProductDate;
    @BindView(R.id.img_tag_icon)
    ImageView imgTagIcon;
    @BindView(R.id.txt_firm_info)
    TextView txtFirmInfo;
    @BindView(R.id.txt_firm_name)
    TextView txtFirmName;
    @BindView(R.id.txt_firm_phone)
    TextView txtFirmPhone;
    @BindView(R.id.txt_firm_address)
    TextView txtFirmAddress;
    @BindView(R.id.txt_firm_web)
    TextView txtFirmWeb;
    @BindView(R.id.linear_firm_detail)
    LinearLayout linearFirmDetail;
    @BindView(R.id.txt_note)
    TextView txtNote;
    @BindView(R.id.img_sgs)
    ImageView imgSgs;
    @BindView(R.id.img_report)
    ImageView imgReport;
    @BindView(R.id.activity_product_more)
    CoordinatorLayout activityProductMore;
    @BindView(R.id.linear_sgs)
    LinearLayout linearSgs;
    @BindView(R.id.linear_report)
    LinearLayout linearReport;

    //NFC商品資訊
    private NfcProductResponse mNfcProductResponse;
    //廣播接收器
    private NfcBroadCast nfcBroadCast;

    //紀錄滑動最大Size
    private int mMaxScrollSize = 0;
    //紀錄toolBar現在是否隱藏
    private boolean mIsToolBarHidden;
    //滑動百分比
    private static final int PERCENTAGE_TO_SHOW_IMAGE = 96;

    //廣告Adapter
    private ADNewsAdapter adNewsAdapter;
    //viewPager 總數
    private int dotCount;
    //指示器 圖片
    private ImageView[] dots;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_more);
        ButterKnife.bind(this);


        adjustUI();
        initParam();
    }

    /**
     * 動態設定UI
     */
    private void adjustUI() {
        getScreenSize(this);
        LinearLayout.LayoutParams txtFirmInfoLp = (LinearLayout.LayoutParams) txtFirmInfo.getLayoutParams();
        txtFirmInfoLp.setMargins((int) (width * 0.08), (int) (height * 0.03), 0, 0);
        txtFirmInfo.setLayoutParams(txtFirmInfoLp);

        LinearLayout.LayoutParams linearFirmDetailLp = (LinearLayout.LayoutParams) linearFirmDetail.getLayoutParams();
        linearFirmDetailLp.setMargins((int) (width * 0.12), (int) (height * 0.02), (int) (width * 0.12), 0);
        linearFirmDetail.setLayoutParams(linearFirmDetailLp);
    }

    @Override
    protected void initParam() {

        txtTitle.setText("產品資訊");

        cloudAPI = CloudAPI.getInstance();
        cloudAPI.setOnGetTrueGoodsListener(this);
        cloudAPI.getTrueGoods(getIntent().getExtras().getString("product_id"), this, Locale.getDefault().getLanguage());

        nfcBroadCast = new NfcBroadCast();
        registerReceiver(nfcBroadCast, new IntentFilter(Variable.Intent_Close_NFC_Activity));
        appbarProductMore.addOnOffsetChangedListener(this);
    }

    /**
     * 初始化ViewPager
     */
    private void initViewPager() {
        adNewsAdapter = new ADNewsAdapter(getSupportFragmentManager(), getFragment());
        if (adNewsAdapter.getCount() == 1) viewGroup.setVisibility(View.GONE);
        viewPagerProduct.setAdapter(adNewsAdapter);
        viewPagerProduct.addOnPageChangeListener(this);
        setUiPageViewController();
    }

    /**
     * 設定Page 頁面
     */
    private List<Fragment> getFragment() {
        List<Fragment> fragments = new ArrayList<>();
        List<String> urls = mNfcProductResponse.getData().get(0).getGoods_image3();
        List<String> imgUrls = mNfcProductResponse.getData().get(0).getGood_imag_url();
        for (int i = 0; i < urls.size(); i++) {
            fragments.add(ADNewFragment.getInstance(urls.get(i), imgUrls.get(i)));
        }
        return fragments;
    }

    /**
     * setting Indicator
     */
    private void setUiPageViewController() {
        dotCount = adNewsAdapter.getCount();
        dots = new ImageView[dotCount];

        for (int i = 0; i < dotCount; i++) {
            dots[i] = new ImageView(this);
            dots[i].setImageDrawable(getResources().getDrawable(R.drawable.non_select_dot));
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(8, 4, 8, 4);
            viewGroup.addView(dots[i], params);
        }

        dots[0].setImageDrawable(getResources().getDrawable(R.drawable.select_dot)); //預設一進畫面，是被選中的
    }


    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        if (mMaxScrollSize == 0) mMaxScrollSize = appBarLayout.getTotalScrollRange();
        //計算滑動當前的百分比
        int currentScrollPercentage = (Math.abs(verticalOffset)) * 100 / mMaxScrollSize;

        if (currentScrollPercentage >= PERCENTAGE_TO_SHOW_IMAGE) {
            if (!mIsToolBarHidden) {
                mIsToolBarHidden = true;
                buttonBar.setVisibility(View.VISIBLE);

            }
        }

        if (currentScrollPercentage < PERCENTAGE_TO_SHOW_IMAGE) {
            if (mIsToolBarHidden) {
                mIsToolBarHidden = false;
                buttonBar.setVisibility(View.GONE);
            }
        }
    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        for (int i = 0; i < dotCount; i++) {
            dots[i].setImageDrawable(getResources().getDrawable(R.drawable.non_select_dot));
        }
        dots[position].setImageDrawable(getResources().getDrawable(R.drawable.select_dot));
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    /**
     * 廣播接收器
     */
    class NfcBroadCast extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(Variable.Intent_Close_NFC_Activity)) finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (nfcBroadCast != null) unregisterReceiver(nfcBroadCast);
    }


    //-------------------------API----------------------------------//

    @Override
    public void getTrueGoodsMessage(NfcProductResponse nfcProductResponse, String error) {
        if (nfcProductResponse != null) {
            if (nfcProductResponse.getCode() == CloudCode.Code_SUCCESS) {
                this.mNfcProductResponse = nfcProductResponse;
                txtProductName.setText(nfcProductResponse.getData().get(0).getGoods_name()); //商品名稱
                txtProductId.setText(getString(R.string.nfc_product_id, nfcProductResponse.getData().get(0).getGoods_code())); //商品編號
                txtProductDate.setText(getString(R.string.nfc_product_date, MyTimeUtils.clearTime(nfcProductResponse.getData().get(0).getPre_date_s()))); //生產日期
                txtFirmName.setText(nfcProductResponse.getData().get(0).getFirm_name()); //廠商名稱
                txtFirmPhone.setText(String.valueOf(nfcProductResponse.getData().get(0).getTel())); //廠商客服電話
                txtFirmAddress.setText(nfcProductResponse.getData().get(0).getAddress()); //廠商地址
                txtFirmWeb.setText(AppUtil.returnUnderLine(nfcProductResponse.getData().get(0).getUrl())); //廠商網址
                txtNote.setText(nfcProductResponse.getData().get(0).getNote()); //備註
                imgTagIcon.setVisibility(nfcProductResponse.getData().get(0).getBatch_image().equals("") ? View.GONE : View.VISIBLE); // 新品圖標
                linearSgs.setVisibility(nfcProductResponse.getData().get(0).getSgs_url().equals("") ? View.GONE : View.VISIBLE); //第三方檢驗報告
                linearReport.setVisibility(nfcProductResponse.getData().get(0).getBatch_url().equals("") ? View.GONE : View.VISIBLE); //生產報告
                Glide.with(ProductMoreActivity.this).load(nfcProductResponse.getData().get(0).getBatch_image()).centerCrop().into(imgTagIcon);
                initViewPager();

            }
        }
    }


    @OnClick({R.id.img_title_back, R.id.img_collapsing_back, R.id.txt_firm_web, R.id.img_sgs, R.id.img_report})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_title_back:
                finish();
                break;
            case R.id.img_collapsing_back:
                finish();
                break;
            case R.id.txt_firm_web:
                AppUtil.intentUrl(this, mNfcProductResponse.getData().get(0).getUrl());
                break;
            case R.id.img_sgs:
                AppUtil.intentUrl(this, mNfcProductResponse.getData().get(0).getSgs_url());
                break;
            case R.id.img_report:
                AppUtil.intentUrl(this, mNfcProductResponse.getData().get(0).getBatch_url());
                break;
        }
    }
}
