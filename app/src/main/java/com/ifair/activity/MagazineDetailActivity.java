package com.ifair.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ResolveInfo;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.customtabs.CustomTabsIntent;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ifair.R;
import com.ifair.adapter.ADNewsAdapter;
import com.ifair.adapter.MagazineGoodsDownAdapter;
import com.ifair.api.CloudAPI;
import com.ifair.api.CloudCode;
import com.ifair.fragment.ADNewFragment;
import com.ifair.fragment.MagazineGoodsDownFragment;
import com.ifair.listener.OnAddLikeGoodsListener;
import com.ifair.listener.OnGetMagazineDetailListener;
import com.ifair.listener.OnGetMagazineGoodsDownListener;
import com.ifair.listener.OnSendAddFavoriteListener;
import com.ifair.listener.OnSendDelFavoriteListener;
import com.ifair.module.AddFavoriteResponse;
import com.ifair.module.DelFavoriteResponse;
import com.ifair.module.MagazineDetailResponse;
import com.ifair.module.MagazineGoodsDownResponse;
import com.ifair.myUtil.AppUtil;
import com.ifair.myUtil.MyAnimationUtils;
import com.ifair.myUtil.MyFileUtil;
import com.ifair.myUtil.MyTimeUtils;
import com.ifair.myUtil.Variable;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.FileCallback;
import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;

public class MagazineDetailActivity extends BaseNFcActivity implements OnGetMagazineDetailListener, OnAddLikeGoodsListener, AppBarLayout.OnOffsetChangedListener, OnSendAddFavoriteListener, OnGetMagazineGoodsDownListener, OnSendDelFavoriteListener, ViewPager.OnPageChangeListener {
    private static final String TAG = "MagazineDetailActivity";
    @BindView(R.id.view_pager_magazine)
    ViewPager viewPagerMagazine;
    @BindView(R.id.viewGroup)
    LinearLayout viewGroup;
    @BindView(R.id.img_title_back)
    ImageView imgTitleBack;
    @BindView(R.id.txt_title)
    TextView txtTitle;
    @BindView(R.id.button_bar)
    RelativeLayout buttonBar;
    @BindView(R.id.txt_product_video)
    TextView txtProductVideo;
    @BindView(R.id.linear_video)
    LinearLayout linearVideo;
    @BindView(R.id.img_collapsing_back)
    ImageView imgCollapsingBack;
    @BindView(R.id.appbar_magazine)
    AppBarLayout appbarMagazine;
    //@BindView(R.id.txt_firm)     //2017/08/18不在使用廠商名稱
    //TextView txtFirm;
    @BindView(R.id.txt_goods_title)
    TextView txtGoodsTitle;
    @BindView(R.id.view)
    View view;
    @BindView(R.id.btn_preOrder)
    Button btnPreOrder;
    @BindView(R.id.img_share)
    ImageView imgShare;
    @BindView(R.id.img_favorite)
    ImageView imgFavorite;
    @BindView(R.id.img_like)
    ImageView imgLike;
    @BindView(R.id.txt_like_num)
    TextView txtLikeNum;
    @BindView(R.id.linear_share)
    LinearLayout linearShare;
    @BindView(R.id.txt_content)
    TextView txtContent;
    @BindView(R.id.activity_magazine)
    CoordinatorLayout activityMagazine;
    @BindView(R.id.tv_goods_price)      //2017/08/22改原價與特價都相同，只秀橘色字顯示位置置中
    TextView tvGoodsPrice;
    @BindView(R.id.tv_goods_sale_price)
    TextView tvGoodsSalePrice;
    @BindView(R.id.tv_mag_detail_rl_2)
    RelativeLayout tvMagDetailRL2;
    @BindView(R.id.tv_pre_amount)
    TextView tvPreAmount;
    @BindView(R.id.tv_goods_pre_date)
    TextView tvGoodsPreDate;
    @BindView(R.id.ll_recommened)
    LinearLayout llRecommened;
    @BindView(R.id.view_pager_magazine_goods_down)
    ViewPager viewPagerMagazineGoodsDown;
    @BindView(R.id.linear_top)
    LinearLayout linearTop;
    @BindView(R.id.scroll_magazine_detail)
    NestedScrollView scrollMagazineDetail;


    private String goodsId;
    private Boolean boolean_Favorite;
    private Handler handler = new Handler();
    //送API用
    private String unique_value = "";
    private MagazineDetailResponse magazineDetailResponse;
    //廣告Adapter
    private ADNewsAdapter adNewsAdapter;
    //viewPager 總數
    private int dotCount;
    //指示器 圖片
    private ImageView[] dots;
    //紀錄滑動最大Size
    private int mMaxScrollSize = 0;
    //紀錄toolBar現在是否隱藏
    private boolean mIsToolBarHidden;
    //滑動百分比
    private static final int PERCENTAGE_TO_SHOW_IMAGE = 95;

    private String firm_id;
    private MagazineGoodsDownResponse magazineGoodsDownResponse;
    private MagazineGoodsDownAdapter recommenedAdapter;
    //廣播接收器
    private NfcBroadCast nfcBroadCast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_magazine_detail);
        ButterKnife.bind(this);
        initParam();
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
    protected void initParam() {
        appbarMagazine.addOnOffsetChangedListener(this);
        //取得使用者資訊
        getLoginResponse();
        unique_value = userEmail.equals("") ? AppUtil.getMacAddress() : userEmail;
        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        goodsId = bundle.getString("Goods_id");

        //detail_API
        cloudAPI = CloudAPI.getInstance();
        cloudAPI.setOnGetMagazineDetailListener(this);
        cloudAPI.setOnGetMagazineGoodsDownListener(this);
        cloudAPI.GetMagazineDetail(unique_value, goodsId, this, Locale.getDefault().getLanguage());

        nfcBroadCast = new NfcBroadCast();
        registerReceiver(nfcBroadCast, new IntentFilter(Variable.Intent_Close_NFC_Activity));
        scrollMagazineDetail.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (scrollMagazineDetail.canScrollVertically(-1))
                    linearTop.setVisibility(View.VISIBLE);
                else
                    linearTop.setVisibility(View.GONE);
            }
        });

    }

    /**
     * 設定資料
     * @param data 雲端資料
     */
    public void setData(MagazineDetailResponse.DataBean data) {
        initViewPager();
        txtTitle.setText(data.getGoods_name()); //標題
        txtGoodsTitle.setText(data.getGoods_name()); //標題

        if(Integer.valueOf(data.getPrice()) > 0){
            tvGoodsSalePrice.setText(getString(R.string.magazine_price, AppUtil.numberFormat(String.valueOf(Integer.valueOf(data.getPrice())))));      //特價
        } else {
            //2017/9/5價格零不顯示
            tvGoodsSalePrice.setText("");      //特價
        }

        if(Integer.parseInt(data.getOriginal_price()) == Integer.parseInt(data.getPrice())){
            //改原價與特價都相同，只秀橘色字顯示位置置中
            //android:gravity="left"
            tvGoodsSalePrice.setGravity(Gravity.CENTER);
            tvMagDetailRL2.setVisibility(View.GONE);
        }else{
            tvGoodsSalePrice.setGravity(Gravity.LEFT);
        }

        if(Integer.valueOf(data.getOriginal_price()) > 0){
            if(Integer.valueOf(data.getPrice()) <= 0){
                tvMagDetailRL2.setVisibility(View.GONE);
            } else {
                tvGoodsPrice.setText(getString(R.string.magazine_price, AppUtil.numberFormat(String.valueOf(Integer.valueOf(data.getOriginal_price())))));  //原價
            }
        } else {
            //2017/9/5價格零不顯示
            tvGoodsPrice.setText("");  //原價
            tvGoodsSalePrice.setGravity(Gravity.CENTER);
            tvMagDetailRL2.setVisibility(View.GONE);
        }

        txtLikeNum.setText(AppUtil.numberFormat(data.getLike())); //按讚數
        txtContent.setText(data.getIntroduction()); // 內文
        //txtFirm.setText((data.getFirm_name())); //廠商名稱

        int[] num = MyTimeUtils.returnPreOrderReciprocal(data.getPre_date_e(), new Date(System.currentTimeMillis()));
        //如果是預購就顯示
        if (data.getIs_preorder().equals("true") && (num[0] >= 0 && num[1] >= 0 && num[2] >= 0)) {
            btnPreOrder.setVisibility(View.VISIBLE);
            String goodsPreDate1 = "<font color='#f6b300'><b><big>" + String.valueOf(num[0]) + "</big></b></font>";
            String goodsPreDate2 = "<font color='#f6b300'><b>" + String.valueOf(num[1]) + "</b></font>";
            String goodsPreDate3 = "<font color='#f6b300'><b>" + String.valueOf(num[2]) + "</b></font>";
            tvGoodsPreDate.setText(Html.fromHtml(getString(R.string.magazine_time, goodsPreDate1, goodsPreDate2, goodsPreDate3)));
            handler.postDelayed(updatePreOrderDate, 5000);

            if (!data.getPre_amount().equals("")) {
                tvPreAmount.setText(getString(R.string.magazine_restriction, data.getPre_amount()));
                tvPreAmount.setVisibility(View.VISIBLE);
            }
        }


        showVideoView();
    }

    /**
     * 初始化ViewPager
     */
    private void initViewPager() {
        adNewsAdapter = new ADNewsAdapter(getSupportFragmentManager(), getFragment());
        if (adNewsAdapter.getCount() == 1) viewGroup.setVisibility(View.GONE);
        viewPagerMagazine.setAdapter(adNewsAdapter);
        viewPagerMagazine.addOnPageChangeListener(this);
        setUiPageViewController();
    }

    /**
     * 設定Page 頁面
     */
    private List<Fragment> getFragment() {
        List<Fragment> fragments = new ArrayList<>();
        List<String> imgUrls = magazineDetailResponse.getData().get(0).getGoods_image();
        List<String> bigimgUrls = magazineDetailResponse.getData().get(0).getBig_imag_url();
        for (int i = 0; i < imgUrls.size(); i++) {
            fragments.add(ADNewFragment.getInstance(imgUrls.get(i), bigimgUrls.get(i)));
        }
        return fragments;
    }

    /**
     * 設定Page 頁面
     */
    private List<Fragment> getRecommenedFragment() {
        List<Fragment> fragments = new ArrayList<>();
        for (int i = 0; i < magazineGoodsDownResponse.getData().size(); i++) {
            if (!magazineGoodsDownResponse.getData().get(i).getGoods_id().equals(magazineDetailResponse.getData().get(0).getGoods_id())){
                //fragments.add(MagazineGoodsDownFragment.getInstance(magazineDetailResponse.getData().get(0).getFirm_name(), magazineGoodsDownResponse.getData().get(i).getGoods_id(), magazineGoodsDownResponse.getData().get(i).getGoods_name(), magazineGoodsDownResponse.getData().get(i).getGoods_image2(), magazineGoodsDownResponse.getData().get(i).getOriginal_price(), magazineGoodsDownResponse.getData().get(i).getPrice()));
                //2017/08/18取消廠商名稱Firm_name、商品原價格Original_price
                fragments.add(MagazineGoodsDownFragment.getInstance("", magazineGoodsDownResponse.getData().get(i).getGoods_id(), magazineGoodsDownResponse.getData().get(i).getGoods_name(), magazineGoodsDownResponse.getData().get(i).getGoods_image2(), magazineGoodsDownResponse.getData().get(i).getOriginal_price(), magazineGoodsDownResponse.getData().get(i).getPrice()));
            }
        }
        return fragments;
    }

    private Runnable updatePreOrderDate = new Runnable() {
        @Override
        public void run() {
            int[] num = MyTimeUtils.returnPreOrderReciprocal(magazineDetailResponse.getData().get(0).getPre_date_e(), new Date(System.currentTimeMillis()));
            String goodsPreDate1 = "<font color='#f6b300'><b><big>" + String.valueOf(num[0]) + "</big></b></font>";
            String goodsPreDate2 = "<font color='#f6b300'><b>" + String.valueOf(num[1]) + "</b></font>";
            String goodsPreDate3 = "<font color='#f6b300'><b>" + String.valueOf(num[2]) + "</b></font>";
            tvGoodsPreDate.setText(Html.fromHtml(getString(R.string.magazine_time, goodsPreDate1, goodsPreDate2, goodsPreDate3)));
            handler.postDelayed(updatePreOrderDate, 5000);
        }
    };


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


    //------------------------------------------API----------------------------------------------//

    @Override
    public void GetMagazineDetailMessage(MagazineDetailResponse magazineDetailResponse, String Error) {
        if (magazineDetailResponse != null) {
            if (magazineDetailResponse.getCode() == CloudCode.Code_SUCCESS) {
                this.magazineDetailResponse = magazineDetailResponse;
                setData(magazineDetailResponse.getData().get(0));

                firm_id = magazineDetailResponse.getData().get(0).getFirm_id();
                cloudAPI.getMagazineGoodsDown("", firm_id, this,Locale.getDefault().getLanguage());
                //按LIKE
                //判斷是不是有按過
                imgLike.setBackgroundResource(magazineDetailResponse.getData().get(0).getIs_like().equals("true") ?
                        R.drawable.all_btn_a_thumb_1 : R.drawable.all_btn_a_thumb_0);
                //按最愛
                boolean_Favorite = magazineDetailResponse.getData().get(0).getIs_favourite().equals("true");
                if (boolean_Favorite) {
                    imgFavorite.setBackgroundResource(R.drawable.product_btn_a_like);
                }

            } else if (magazineDetailResponse.getCode() == CloudCode.Code_No_data) {
                Toast.makeText(this, "查無資料", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    /**
     * 新增最愛
     */
    @Override
    public void SendAddFavoriteMessage(AddFavoriteResponse addFavoriteResponse, String Error) {
        if (addFavoriteResponse != null && addFavoriteResponse.getCode() == CloudCode.Code_SUCCESS) {
            boolean_Favorite = !boolean_Favorite;
            imgFavorite.setBackgroundResource(R.drawable.product_btn_a_like);
            imgFavorite.startAnimation(MyAnimationUtils.imageScaleAnimation());
        }
    }

    /**
     * 取消最愛
     */
    @Override
    public void SendDelFavoriteMessage(DelFavoriteResponse delFavoriteResponse, String Error) {
        if (delFavoriteResponse != null && delFavoriteResponse.getCode() == CloudCode.Code_SUCCESS) {
            boolean_Favorite = !boolean_Favorite;
            imgFavorite.setBackgroundResource(R.drawable.all_btn_a_favourite_0);
        }
    }


    /**
     * 新增讚
     */
    @Override
    public void addLikeGoodsMessage(int code, String error) {
        Log.d(TAG, "addLikeGoodsMessage: " + code);
        if (code == CloudCode.Code_SUCCESS) {
            txtLikeNum.setText(String.valueOf(Integer.valueOf(txtLikeNum.getText().toString()) + 1));
            imgLike.setBackgroundResource(R.drawable.all_btn_a_thumb_1);
            imgLike.startAnimation(MyAnimationUtils.imageScaleAnimation());
        }

    }

    //------------------------------------------API----------------------------------------------//


    @OnClick({R.id.img_top,R.id.img_title_back, R.id.img_collapsing_back, R.id.img_share, R.id.img_favorite, R.id.img_like, R.id.linear_video, R.id.btn_preOrder})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_title_back:
            case R.id.img_collapsing_back:
                finish();
                break;
            case R.id.btn_preOrder:
                Bundle product_detail = new Bundle();
                product_detail.putString("Goods_id", goodsId);
                goPage(PreOrderActivity.class, product_detail, false);
                break;
            case R.id.img_share:
                //分享
                if (checkFilePermission(activityMagazine)) {
                    //先下載圖片，再分享
                    showDialog("");
                    imgShare.setClickable(false);
                    downLoadImage();
                    cloudAPI.onAddShareGoods(unique_value, magazineDetailResponse.getData().get(0).getGoods_id(), this);
                }
                break;
            case R.id.img_favorite:
                if (!Objects.equals(userEmail, "")) {
                    if (!boolean_Favorite) {//點最愛
                        cloudAPI.setOnSendAddFavoriteListener(this);
                        cloudAPI.SetAddFavorite(userEmail, goodsId, this);
                    } else {//取消最愛
                        cloudAPI.setOnSendDelFavoriteListener(this);
                        cloudAPI.SetDelFavorite(userEmail, goodsId, this);
                    }
                }
                break;
            case R.id.img_like:
                if (magazineDetailResponse.getData().get(0).getIs_like().equals("false")) {
                    cloudAPI.setOnAddLikeGoodsListener(this);
                    // 點LIKE
                    cloudAPI.addLikeGoods(unique_value, goodsId, this);
                }
                break;
            case R.id.linear_video:
                CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
                builder.setToolbarColor(getResources().getColor(R.color.colorAccent));
                builder.setCloseButtonIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_arrow_back));
                CustomTabsIntent customTabsIntent = builder.build();
                customTabsIntent.launchUrl(this, Uri.parse(magazineDetailResponse.getData().get(0).getYoutube_url()));
                break;
            case R.id.img_top:
                scrollMagazineDetail.scrollTo(0, 0);
                break;
        }
    }

    /**
     * 判斷是不是需要顯示Video View
     */
    private void showVideoView() {
        if (magazineDetailResponse.getData().get(0).getYoutube_url().equals("")) {
            linearVideo.setVisibility(View.GONE);
        } else {
            linearVideo.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    /**
     * 下載圖片
     */
    private void downLoadImage() {
        String fileName = MyTimeUtils.getCurrentTime("yyyyMMddhhmmss") + ".png";
        OkGo.get(magazineDetailResponse.getData().get(0).getGoods_image().get(0)).tag(this)
                .execute(new FileCallback(MyFileUtil.filePath + "/share", fileName) {
                    @Override
                    public void onSuccess(File file, Call call, Response response) {
                        Logger.d(String.valueOf(file.toURI()));
                        dialog.dismiss();
                        share(file);
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        dialog.dismiss();
                        Logger.d(e.getMessage());
                    }
                });
    }

    /**
     * 分享
     */
    public void share(File filePath) {
        List<Intent> intentShareList = new ArrayList<Intent>();
        Intent shareIntent = new Intent();
        shareIntent.setType("image/png,text/plain");
        shareIntent.setAction(Intent.ACTION_SEND);
        List<ResolveInfo> resolveInfoList = getPackageManager().queryIntentActivities(shareIntent, 0);

        for (ResolveInfo resInfo : resolveInfoList) {
            String packageName = resInfo.activityInfo.packageName;
            String name = resInfo.activityInfo.name;
            if (packageName.contains("facebook") || packageName.contains("line") || packageName.contains("tencent")) {


                Intent intent = new Intent();
                intent.setComponent(new ComponentName(packageName, name));
                intent.setAction(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.setType("image/png");
                intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(filePath));
                if (packageName.contains("tencent"))
                    intent.putExtra("Kdescription", magazineDetailResponse.getData().get(0).getGoods_name());
                intentShareList.add(intent);
            }
        }

        if (intentShareList.size() == 0) {
            alert(getString(R.string.magazine_share_alert));
        } else {
            Intent chooserIntent = Intent.createChooser(intentShareList.remove(0), getString(R.string.magazine_share_button_text));
            chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, intentShareList.toArray(new Parcelable[]{}));
            startActivity(chooserIntent);
            imgShare.setClickable(true);
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

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        if (mMaxScrollSize == 0) mMaxScrollSize = appBarLayout.getTotalScrollRange();
        //計算滑動當前的百分比
        int currentScrollPercentage = (Math.abs(verticalOffset)) * 100 / mMaxScrollSize;

        if (currentScrollPercentage >= PERCENTAGE_TO_SHOW_IMAGE) {
            if (!mIsToolBarHidden) {
                mIsToolBarHidden = true;
                buttonBar.setAlpha(0f);
                buttonBar.setVisibility(View.VISIBLE);
                buttonBar.animate().alpha(1.0f).setDuration(400);
                linearVideo.animate().alpha(0f).setDuration(500).setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        linearVideo.setVisibility(View.GONE);
                        linearVideo.setAlpha(1.0f);
                    }
                });
                imgCollapsingBack.setVisibility(View.GONE);


            }
        }


        if (currentScrollPercentage < PERCENTAGE_TO_SHOW_IMAGE) {
            if (mIsToolBarHidden) {
                mIsToolBarHidden = false;
                Log.d(TAG, "onAnimationEnd: mIsToolBarHidden = false");
                showVideoView();
                buttonBar.setVisibility(View.GONE);
                imgCollapsingBack.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(updatePreOrderDate);
        unregisterReceiver(nfcBroadCast);
    }

    @Override
    public void getMagazineGoodsDown(MagazineGoodsDownResponse magazineGoodsDownResponse, String Error) {
        if (magazineGoodsDownResponse.getCode() == CloudCode.Code_SUCCESS) {
            this.magazineGoodsDownResponse = magazineGoodsDownResponse;
            recommenedAdapter = new MagazineGoodsDownAdapter(getSupportFragmentManager(), getRecommenedFragment());
            if (recommenedAdapter.getCount() == 0) llRecommened.setVisibility(View.GONE);
            viewPagerMagazineGoodsDown.setAdapter(recommenedAdapter);
            viewPagerMagazineGoodsDown.setPageMargin(10);
        }
    }
}
