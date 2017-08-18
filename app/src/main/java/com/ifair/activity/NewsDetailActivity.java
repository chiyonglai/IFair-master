package com.ifair.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ifair.R;
import com.ifair.adapter.ADNewsAdapter;
import com.ifair.api.CloudAPI;
import com.ifair.api.CloudCode;
import com.ifair.eventModule.NewEvent;
import com.ifair.fragment.ADNewFragment;
import com.ifair.listener.OnGetAddLikeListener;
import com.ifair.listener.OnGetLatestNewsDetailListener;
import com.ifair.module.NewMessageDetailResponse;
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
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;

public class NewsDetailActivity extends BaseNFcActivity implements OnGetLatestNewsDetailListener, OnGetAddLikeListener, AppBarLayout.OnOffsetChangedListener, ViewPager.OnPageChangeListener {

    private static final String TAG = "NewsDetailActivity";

    @BindView(R.id.txt_firm)
    TextView txtFirm;
    @BindView(R.id.txt_date)
    TextView txtDate;
    @BindView(R.id.txt_goods_title)
    TextView txtGoodsTitle;
    @BindView(R.id.view)
    View view;
    @BindView(R.id.img_share)
    ImageView imgShare;
    @BindView(R.id.img_like)
    ImageView imgLike;
    @BindView(R.id.txt_like_num)
    TextView txtLikeNum;
    @BindView(R.id.linear_share)
    LinearLayout linearShare;
    @BindView(R.id.txt_content)
    TextView txtContent;
    @BindView(R.id.txt_web)
    TextView txtWeb;
    @BindView(R.id.linear_web)
    LinearLayout linearWeb;
    @BindView(R.id.btn_preOrder)
    Button btnPreOrder;
    @BindView(R.id.activity_news_detail)
    CoordinatorLayout activityNewsDetail;
    @BindView(R.id.appbar_news_detail)
    AppBarLayout appbarNewsDetail;
    @BindView(R.id.txt_product_video)
    TextView txtProductVideo;
    @BindView(R.id.linear_video)
    LinearLayout linearVideo;
    @BindView(R.id.view_pager_news)
    ViewPager viewPagerNews;
    @BindView(R.id.viewGroup)
    LinearLayout viewGroup;
    @BindView(R.id.txt_title)
    TextView txtTitle;
    @BindView(R.id.button_bar)
    RelativeLayout buttonBar;
    @BindView(R.id.img_title_back)
    ImageView imgTitleBack;
    @BindView(R.id.img_collapsing_back)
    ImageView imgCollapsingBack;
    @BindView(R.id.scroll_news_detail)
    NestedScrollView scrollNewsDetail;
    @BindView(R.id.linear_top)
    LinearLayout linearTop;


    //訊息ID
    private String message_id;
    //內頁資訊
    private NewMessageDetailResponse mNewMessageDetailResponse;
    //是否有按過讚
    private boolean isLike = false;
    //紀錄滑動最大Size
    private int mMaxScrollSize = 0;
    //紀錄toolBar現在是否隱藏
    private boolean mIsToolBarHidden;
    //滑動百分比
    private static final int PERCENTAGE_TO_SHOW_IMAGE = 95;

    //廣告Adapter
    private ADNewsAdapter adNewsAdapter;
    //viewPager 總數
    private int dotCount;
    //指示器 圖片
    private ImageView[] dots;
    //送API用
    private String unique_value = "";
    //廣播接收器
    private NfcBroadCast nfcBroadCast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);
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
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(nfcBroadCast);
    }

    @Override
    protected void initParam() {
        appbarNewsDetail.addOnOffsetChangedListener(this);


        Bundle bundle = getIntent().getExtras();
        message_id = bundle.getString("message_id");

        //抓取資料
        cloudAPI = CloudAPI.getInstance();
        cloudAPI.setOnGetLatestNewsDetailListener(this);
        cloudAPI.getLatestNewsDetail(AppUtil.getMacAddress(), message_id, this);
        //取得使用者資訊
        getLoginResponse();
        unique_value = userEmail.equals("") ? AppUtil.getMacAddress() : userEmail;

        nfcBroadCast = new NfcBroadCast();
        registerReceiver(nfcBroadCast, new IntentFilter(Variable.Intent_Close_NFC_Activity));

        scrollNewsDetail.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (scrollNewsDetail.canScrollVertically(-1))
                    linearTop.setVisibility(View.VISIBLE);
                else
                    linearTop.setVisibility(View.GONE);
            }
        });
    }

    /**
     * 初始化ViewPager
     */
    private void initViewPager() {
        adNewsAdapter = new ADNewsAdapter(getSupportFragmentManager(), getFragment());
        if (adNewsAdapter.getCount() == 1) viewGroup.setVisibility(View.GONE);
        viewPagerNews.setAdapter(adNewsAdapter);
        viewPagerNews.addOnPageChangeListener(this);
        setUiPageViewController();
    }


    /**
     * 設定Page 頁面
     */
    private List<Fragment> getFragment() {
        List<Fragment> fragments = new ArrayList<>();
        List<String> urls = mNewMessageDetailResponse.getData().get(0).getImg_file();
        List<String> imgUrls = mNewMessageDetailResponse.getData().get(0).getImg_url();
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
                    intent.putExtra("Kdescription", mNewMessageDetailResponse.getData().get(0).getTitle());
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

    /**
     * 下載圖片
     */
    private void downLoadImage() {
        String fileName = MyTimeUtils.getCurrentTime("yyyyMMddhhmmss") + ".png";
        OkGo.get(mNewMessageDetailResponse.getData().get(0).getImg_file().get(0)).tag(this)
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


    @Override
    public void onBackPressed() {
        super.onBackPressed();
//        if (EventBus.getDefault().hasSubscriberForEvent())
        EventBus.getDefault().post(new NewEvent(isLike));
        finish();
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
                buttonBar.animate().alpha(1.0f).setDuration(500);
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
                showVideoView();
                buttonBar.setVisibility(View.GONE);
                imgCollapsingBack.setVisibility(View.VISIBLE);
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
     * 判斷是不是需要顯示Video View
     */
    private void showVideoView() {
        if (mNewMessageDetailResponse.getData().get(0).getYoutube_url().equals("")) {
            linearVideo.setVisibility(View.GONE);
        } else {
            linearVideo.setVisibility(View.VISIBLE);
        }
    }

    //-------------------------------API-------------------------------//

    @Override
    public void getLatestNewsDetailMessage(NewMessageDetailResponse messageDetailResponse, String error) {
        if (messageDetailResponse != null) {
            if (messageDetailResponse.getCode() == CloudCode.Code_SUCCESS) {
                this.mNewMessageDetailResponse = messageDetailResponse;
                initViewPager();
                txtTitle.setText(messageDetailResponse.getData().get(0).getTitle()); //標題
                txtGoodsTitle.setText(messageDetailResponse.getData().get(0).getTitle()); //標題
                txtDate.setText(getString(R.string.news_activity_date,
                        MyTimeUtils.timeToString(messageDetailResponse.getData().get(0).getOn_time()),
                        MyTimeUtils.timeToString(messageDetailResponse.getData().get(0).getOff_time()))); //結束時間
                txtLikeNum.setText(AppUtil.numberFormat(messageDetailResponse.getData().get(0).getLike())); //按讚數
                txtContent.setText(messageDetailResponse.getData().get(0).getContent()); // 內文
                txtFirm.setText((messageDetailResponse.getData().get(0).getFirm_name())); //廠商名稱
                if ((messageDetailResponse.getData().get(0).getSource_content().equals(""))) { //資料來源
                    linearWeb.setVisibility(View.GONE);
                } else {
                    txtWeb.setText(messageDetailResponse.getData().get(0).getSource_content());
                }
                //如果是預購就顯示
                if (messageDetailResponse.getData().get(0).getIs_buy().equals("true")) {
                    btnPreOrder.setVisibility(View.VISIBLE);
                }

                //判斷是不是有按過
                imgLike.setImageDrawable(messageDetailResponse.getData().get(0).getIs_like().equals("true") ?
                        getResources().getDrawable(R.drawable.all_btn_a_thumb_1) : getResources().getDrawable(R.drawable.all_btn_a_thumb_0));

                showVideoView();
            }
        }
    }

    @Override
    public void getAddLikeMessage(int code, String error) {
        if (code == CloudCode.Code_SUCCESS) {
            mNewMessageDetailResponse.getData().get(0).setIs_like("true");
            int likeNum = Integer.parseInt(mNewMessageDetailResponse.getData().get(0).getLike()) + 1;
            mNewMessageDetailResponse.getData().get(0).setLike(String.valueOf(likeNum));
            imgLike.setImageDrawable(getResources().getDrawable(R.drawable.all_btn_a_thumb_1));
            txtLikeNum.setText(AppUtil.numberFormat(mNewMessageDetailResponse.getData().get(0).getLike())); //按讚數
            isLike = true;
            imgLike.startAnimation(MyAnimationUtils.imageScaleAnimation());
        }
    }

    //-------------------------------API-------------------------------//

    @OnClick({R.id.img_top,R.id.img_share, R.id.img_like, R.id.txt_web, R.id.btn_preOrder, R.id.linear_video, R.id.img_title_back, R.id.img_collapsing_back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_share:
                if (checkFilePermission(activityNewsDetail)) {
                    //先下載圖片，再分享
                    showDialog("");
                    imgShare.setClickable(false);
                    downLoadImage();
                    cloudAPI.onAddShareMessage(userEmail, mNewMessageDetailResponse.getData().get(0).getMessage_id(), this);
                }
                break;
            case R.id.img_like:
                if (mNewMessageDetailResponse.getData().get(0).getIs_like().equals("false")) {
                    cloudAPI.setOnGetAddLikeListener(this);
                    cloudAPI.addLikeMessage(message_id, AppUtil.getMacAddress());
                }
                break;
            case R.id.txt_web:
                intentUrl(mNewMessageDetailResponse.getData().get(0).getUrl());
                break;
            case R.id.btn_preOrder:
                if (mNewMessageDetailResponse.getData().get(0).getPre_order_url().equals("")) {
                    Bundle product_detail = new Bundle();
                    product_detail.putString("Goods_id", mNewMessageDetailResponse.getData().get(0).getGoods_id());
                    goPage(PreOrderActivity.class, product_detail, false);
                } else {
                    intentUrl(mNewMessageDetailResponse.getData().get(0).getPre_order_url());
                }

                break;
            case R.id.linear_video:
                intentUrl(mNewMessageDetailResponse.getData().get(0).getYoutube_url());
                break;
            case R.id.img_title_back:
                onBackPressed();
                break;
            case R.id.img_collapsing_back:
                onBackPressed();
                break;
            case R.id.img_top:
                scrollNewsDetail.scrollTo(0, 0);
                break;
        }
    }
}
