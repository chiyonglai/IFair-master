package com.ifair.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ifair.R;
import com.ifair.activity.ProductMoreActivity;
import com.ifair.activity.ProductSubmitActivity;
import com.ifair.api.CloudAPI;
import com.ifair.api.CloudCode;
import com.ifair.listener.OnGetTrueGoodsListener;
import com.ifair.module.NfcProductResponse;
import com.ifair.myUtil.AppUtil;
import com.ifair.myUtil.MyAnimationUtils;
import com.ifair.myUtil.Variable;

import java.util.Locale;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * 判斷真偽頁面
 */
public class NfcProductFragment extends BaseFragment implements OnGetTrueGoodsListener {

    public static final String TAG = "NfcProductFragment";

    private static final String ARG_PARAM1 = "product_id";
    @BindView(R.id.img_product)
    ImageView imgProduct;
    @BindView(R.id.img_youtube)
    ImageView imgYoutube;
    @BindView(R.id.rela_youtube)
    RelativeLayout relaYoutube;
    @BindView(R.id.img_real_btn_a_playbtn)
    ImageView imgRealPlayBtn;
    @BindView(R.id.linear_more)
    LinearLayout linearMore;
    Unbinder unbinder;
    @BindView(R.id.rela_real_product)
    RelativeLayout relaRealProduct;
    //@BindView(R.id.btn_submit)
    //Button btnSubmit;
    @BindView(R.id.rela_fake_product)
    RelativeLayout relaFakeProduct;
    @BindView(R.id.image_scale)
    ImageView imageScale;
    @BindView(R.id.txt_real_product)
    TextView txtRealProduct;

    private String product_id;
    // Youtube網址
    private String strYoutube;


    public static NfcProductFragment newInstance() {
        return new NfcProductFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_nfc_product, container, false);
        unbinder = ButterKnife.bind(this, view);
        adJustUi();
        initParam();
        return view;
    }

    private void adJustUi() {
        getScreenSize();
        //RelativeLayout.LayoutParams btnSubmitLp = (RelativeLayout.LayoutParams) btnSubmit.getLayoutParams();
        //btnSubmitLp.width = (int) (width * 0.8);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            initParam();
        }
    }

    @Override
    protected void initParam() {
        product_id = AppUtil.getSharedPref(getActivity(), AppUtil.SHARED_PREF_NAME).getString(Variable.Nfc_productId, "");
        Log.i(TAG, "initParam: " + product_id);
        if (!product_id.equals("")) {
            cloudAPI = CloudAPI.getInstance();
            cloudAPI.setOnGetTrueGoodsListener(this);
            cloudAPI.getTrueGoods(product_id, getActivity(), Locale.getDefault().getLanguage());
            relaFakeProduct.setVisibility(View.GONE);
            relaRealProduct.setVisibility(View.VISIBLE);
        } else {
            relaFakeProduct.setVisibility(View.VISIBLE);
            relaRealProduct.setVisibility(View.GONE);
        }
    }

    /**
     * 顯示真品UI
     */
    private void showRealUI() {
        relaRealProduct.setVisibility(View.VISIBLE);
        ScaleAnimation scaleAnimation = MyAnimationUtils.nfcAnimation();
        scaleAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                txtRealProduct.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                txtRealProduct.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        imageScale.startAnimation(scaleAnimation);

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


    //----------------------------------- API ---------------------------------------//

    @Override
    public void getTrueGoodsMessage(NfcProductResponse nfcProductResponse, String error) {
        if (nfcProductResponse != null) {
            if (nfcProductResponse.getCode() == CloudCode.Code_SUCCESS) {
                if (!Objects.equals(nfcProductResponse.getData().get(0).getGoods_image3().get(0), ""))
                    Glide.with(this).load(nfcProductResponse.getData().get(0).getGoods_image3().get(0)).centerCrop().into(imgProduct);

                strYoutube = nfcProductResponse.getData().get(0).getYoutube_url();
                Log.i(TAG, "getTrueGoodsMessage: " + strYoutube);
                if (!Objects.equals(strYoutube, "")) {

                    String img_url = "";
                    if (AppUtil.extractYTId(strYoutube) != null)
                        img_url = "http://img.youtube.com/vi/" + AppUtil.extractYTId(strYoutube) + "/0.jpg";
                    else if (AppUtil.extractYoutubeId(strYoutube) != null)
                        img_url = "http://img.youtube.com/vi/" + AppUtil.extractYoutubeId(strYoutube) + "/0.jpg";
                    Log.i(TAG, "getTrueGoodsMessage: " + img_url);
                    imgYoutube.setVisibility(View.VISIBLE);
                    imgRealPlayBtn.setVisibility(View.VISIBLE);
                    Glide.with(this).load(img_url).centerCrop().into(imgYoutube);

                } else {
                    //relaYoutube.setVisibility(View.INVISIBLE);
                    imgRealPlayBtn.setVisibility(View.INVISIBLE);
                    if (!Objects.equals(nfcProductResponse.getData().get(0).getGoods_image3().get(1), ""))
                        Glide.with(this).load(nfcProductResponse.getData().get(0).getGoods_image3().get(1)).centerCrop().into(imgYoutube);
                }
                showRealUI();
            } else if (nfcProductResponse.getCode() == CloudCode.Code_No_data) {
                relaFakeProduct.setVisibility(View.VISIBLE);
            }
        }
    }

    //----------------------------------- API ---------, R.id.btn_submit------------------------------//


    @OnClick({R.id.rela_youtube, R.id.linear_more})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rela_youtube:
                if (!Objects.equals(strYoutube, "")) {
                    AppUtil.intentUrl(getActivity(), strYoutube);
                }
                break;
            case R.id.linear_more:
                Bundle bundle = new Bundle();
                bundle.putString("product_id", product_id);
                goToActivity(ProductMoreActivity.class, bundle);
                break;
            //case R.id.btn_submit:
            //    goToActivity(ProductSubmitActivity.class, null);
            //    break;
        }
    }

}
