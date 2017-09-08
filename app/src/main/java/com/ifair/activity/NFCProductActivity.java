package com.ifair.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.customtabs.CustomTabsIntent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.ifair.R;
import com.ifair.api.CloudAPI;
import com.ifair.api.CloudCode;
import com.ifair.listener.OnGetTrueGoodsListener;
import com.ifair.module.NfcProductResponse;
import com.ifair.myUtil.AppUtil;
import com.ifair.myUtil.Variable;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.Locale;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pl.droidsonroids.gif.GifImageView;

/**
 * NFC 真品，產品資訊
 */
public class NFCProductActivity extends BaseActivity implements OnGetTrueGoodsListener {

    private static final String TAG = "NFCProductActivity";

    @BindView(R.id.txt_title)
    TextView txtTitle;
    @BindView(R.id.txt_more_info)
    TextView txtMoreInfo;
    @BindView(R.id.scroll_true_product)
    ScrollView scrollTrueProduct;
    @BindView(R.id.btn_submit)
    Button btnSubmit;
    @BindView(R.id.txt_submit)
    TextView txtSubmit;
    @BindView(R.id.txt_no_register)
    TextView txtNoRegister;
    @BindView(R.id.linear_fake_product)
    LinearLayout linearFakeProduct;
    @BindView(R.id.img_back)
    ImageView imgBack;
    @BindView(R.id.txt_submit_info)
    TextView txtSubmitInfo;
    @BindView(R.id.activity_nfcproduct)
    LinearLayout activityNfcproduct;
    @BindView(R.id.img_produce_youtube)
    RoundedImageView imgProduceYoutube;
    @BindView(R.id.img_youtube)
    RoundedImageView imgYoutube;
    @BindView(R.id.img_produce)
    RoundedImageView imgProduce;
    @BindView(R.id.img_true_product)
    GifImageView imgTrueProduct;

    //商品ID
    private String product_id;
    // Youtube網址
    private String strYoutube;
    //廣播接收器
    private NfcBroadCast nfcBroadCast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nfcproduct);
        ButterKnife.bind(this);

        getScreenSize(this);
        adjustUI();
        initParam();
    }

    /**
     * 動態設定UI
     */
    private void adjustUI() {
        LinearLayout.LayoutParams btnSubmitLp = (LinearLayout.LayoutParams) btnSubmit.getLayoutParams();
        btnSubmitLp.setMargins(0, (int) (height * 0.017), 0, 0);
        btnSubmitLp.width = (int) (width * 0.8);
        btnSubmit.setLayoutParams(btnSubmitLp);

        LinearLayout.LayoutParams txtSubmitLp = (LinearLayout.LayoutParams) txtSubmit.getLayoutParams();
        txtSubmitLp.setMargins(0, (int) (height * 0.08), 0, 0);
        txtSubmit.setLayoutParams(txtSubmitLp);

        LinearLayout.LayoutParams txtNoRegisterLp = (LinearLayout.LayoutParams) txtNoRegister.getLayoutParams();
        txtNoRegisterLp.setMargins(0, 0, (int) (width * 0.01), 0);
        txtNoRegister.setLayoutParams(txtNoRegisterLp);
    }

    @Override
    protected void initParam() {
        Bundle bundle = getIntent().getExtras();
        product_id = bundle.getString("product_id");
        if (Objects.equals(product_id, "")) {
            txtSubmitInfo.setVisibility(View.GONE);
            txtTitle.setText(getString(R.string.nfc_title_no_register));
            scrollTrueProduct.setVisibility(View.GONE);
            linearFakeProduct.setVisibility(View.VISIBLE);
        } else {
            txtTitle.setText(getString(R.string.nfc_title_register));
            cloudAPI = CloudAPI.getInstance();
            cloudAPI.setOnGetTrueGoodsListener(this);
            cloudAPI.getTrueGoods(product_id, this, Locale.getDefault().getLanguage());
            scrollTrueProduct.setVisibility(View.VISIBLE);
        }

        nfcBroadCast = new NfcBroadCast();
        registerReceiver(nfcBroadCast, new IntentFilter(Variable.Intent_Close_NFC_Activity));
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
                if (!Objects.equals(nfcProductResponse.getData().get(0).getGoods_image3(), ""))
                    Glide.with(this).load(nfcProductResponse.getData().get(0).getGoods_image3()).centerCrop().into(imgProduce);
                if (!Objects.equals(nfcProductResponse.getData().get(0).getYoutube_url(), "")) {
                    strYoutube = nfcProductResponse.getData().get(0).getYoutube_url();

                    String img_url = "";
                    if (AppUtil.extractYTId(nfcProductResponse.getData().get(0).getYoutube_url()) != null)
                        img_url = "http://img.youtube.com/vi/" + AppUtil.extractYTId(nfcProductResponse.getData().get(0).getYoutube_url()) + "/0.jpg";
                    else if (AppUtil.extractYoutubeId(nfcProductResponse.getData().get(0).getYoutube_url()) != null)
                        img_url = "http://img.youtube.com/vi/" + AppUtil.extractYoutubeId(nfcProductResponse.getData().get(0).getYoutube_url()) + "/0.jpg";
                    Glide.with(this).load(img_url).fitCenter().into(imgProduceYoutube);
                    imgYoutube.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    //-------------------------API----------------------------------//

    @OnClick({R.id.txt_more_info, R.id.img_produce_youtube, R.id.btn_submit, R.id.img_back, R.id.txt_submit_info})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.txt_more_info:
                Bundle bundle = new Bundle();
                bundle.putString("product_id", product_id);
                goPage(ProductMoreActivity.class, bundle, false);
                break;
            case R.id.img_produce_youtube:
                CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
                builder.setToolbarColor(getResources().getColor(R.color.colorAccent));
                builder.setCloseButtonIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_arrow_back));
                CustomTabsIntent customTabsIntent = builder.build();
                customTabsIntent.launchUrl(this, Uri.parse(strYoutube));
                break;
            case R.id.btn_submit:
                goPage(ProductSubmitActivity.class, false);
                break;
            case R.id.img_back:
                finish();
                break;
            case R.id.txt_submit_info:
                goPage(ProductSubmitActivity.class, false);
                break;
        }
    }
}
