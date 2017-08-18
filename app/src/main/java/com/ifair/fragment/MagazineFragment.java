package com.ifair.fragment;


import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.ksoichiro.android.observablescrollview.ObservableRecyclerView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;
import com.github.ksoichiro.android.observablescrollview.ScrollUtils;
import com.ifair.R;
import com.ifair.activity.BaseActivity;
import com.ifair.activity.MagazineDetailActivity;
import com.ifair.adapter.ADAdapter;
import com.ifair.adapter.MagazineAdapter;
import com.ifair.api.CloudAPI;
import com.ifair.api.CloudCode;
import com.ifair.customView.CustomTitleView;
import com.ifair.listener.OnGetAdvertisingListener;
import com.ifair.listener.OnGetMagazineListener;
import com.ifair.module.AdvertisingResponse;
import com.ifair.module.MagazineResponse;
import com.nineoldandroids.view.ViewHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 商品雜誌Fragment
 */
public class MagazineFragment extends BaseFragment implements ObservableScrollViewCallbacks, OnGetMagazineListener, OnGetAdvertisingListener, ViewPager.OnPageChangeListener {

    public static final String TAG = "MagazineFragment";
    @BindView(R.id.viewPager)
    ViewPager viewPager;
    @BindView(R.id.frame_scroll)
    FrameLayout frameScroll;
    @BindView(R.id.head_view)
    CustomTitleView headView;
    @BindView(R.id.txt_head)
    TextView txtHead;
    @BindView(R.id.frame_head)
    FrameLayout frameHead;
    @BindView(R.id.recycler_magazine_view)
    ObservableRecyclerView recyclerMagazineView;
    @BindView(R.id.viewGroup)
    LinearLayout viewGroup;
    @BindView(R.id.img_top)
    ImageView imgTop;
    @BindView(R.id.linear_top)
    LinearLayout linearTop;


    //滑動的背景圖高度
    private int mFlexibleSpaceImageHeight;
    //是否可以開始滑動
    private boolean mReady;
    //Y軸位置
    private int mPrevScrollY;
    //商品雜誌資料
    private List<MagazineResponse.DataBean> dataBeanList;
    //產品Adapter
    private MagazineAdapter magazineAdapter;
    //廣告執行緒
    private Handler handler = new Handler();
    //廣告Adapter
    private ADAdapter adAdapter;
    //viewPager 總數
    private int dotCount;
    //指示器 圖片
    private ImageView[] dots;
    private BaseActivity activity;

    public static MagazineFragment getInstance() {
        return new MagazineFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_magazine, container, false);
        ButterKnife.bind(this, view);
        activity = (BaseActivity) getActivity();
        adjustUI();
        initRecyclerView();
        initParam();
        setAD();
        return view;
    }

    /**
     * 動態設定UI
     */
    private void adjustUI() {
        getScreenSize();
        headView.setWidth(width);
    }

    /**
     * 初始化RecyclerView
     */
    private void initRecyclerView() {
        dataBeanList = new ArrayList<>();
        mFlexibleSpaceImageHeight = getActivity().getResources().getDimensionPixelOffset(R.dimen.frame_height_magazine);
        recyclerMagazineView.setScrollViewCallbacks(this);
        recyclerMagazineView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (recyclerView.canScrollVertically(-1))
                    linearTop.setVisibility(View.VISIBLE);
                else
                    linearTop.setVisibility(View.GONE);

            }
        });
        View headView = new View(getActivity());
        headView.setLayoutParams(new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, mFlexibleSpaceImageHeight));
        headView.setMinimumHeight(mFlexibleSpaceImageHeight);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2);
        gridLayoutManager.setOrientation(GridLayoutManager.VERTICAL);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                int index = position % 7;
                switch (index) {
                    case 0:
                        return 1;
                    case 4:
                        return 1;
                    case 5:
                        return 1;
                    case 6:
                        return 1;
                    default:
                        return 2;
                }
            }
        });

        recyclerMagazineView.setLayoutManager(gridLayoutManager);
        magazineAdapter = new MagazineAdapter(dataBeanList, headView, getActivity());
        recyclerMagazineView.setAdapter(magazineAdapter);
        handler.postDelayed(updateTimer, 3000);
        magazineAdapter.setOnMagazineClickListener(position -> {
            Bundle bundle = new Bundle();
            bundle.putString("Goods_id", dataBeanList.get(position - 1).getGoods_id());
            goToActivity(MagazineDetailActivity.class, bundle);
        });
    }


    @Override
    protected void initParam() {
        ScrollUtils.addOnGlobalLayoutListener(recyclerMagazineView, () -> {
            mReady = true;
            updateViews(recyclerMagazineView.getCurrentScrollY(), false);
        });

        callApi();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) callApi();
    }

    /**
     * 呼叫API
     */
    private void callApi() {
        cloudAPI = CloudAPI.getInstance();
        cloudAPI.setOnGetMagazineListener(this);
        cloudAPI.getMagazine("", getActivity());
    }


    /**
     * 載入商品廣告輪播
     */
    private void setAD() {
        cloudAPI = CloudAPI.getInstance();
        cloudAPI.setOnGetAdvertisingListener(this);
        cloudAPI.getAdvertising(getActivity());
    }

    /**
     * 廣告輪播的執行緒
     */
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            viewPager.setCurrentItem((viewPager.getCurrentItem() + 1) % viewPager.getAdapter().getCount());
            handler.postDelayed(runnable, 5000);
        }
    };

    /**
     * setting Indicator
     */
    private void setUiPageViewController() {
        dotCount = adAdapter.getCount();
        if (dotCount > 1) {

            dots = new ImageView[dotCount];
            for (int i = 0; i < dotCount; i++) {
                dots[i] = new ImageView(getActivity());
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
    }

    /**
     * 更新UI
     *
     * @param scrollY
     * @param animated
     */
    protected void updateViews(int scrollY, boolean animated) {

        if (!mReady) {
            return;
        }
        ViewHelper.setTranslationY(frameScroll, -scrollY);

        // Translate header
        float headerY = getHeaderTranslationY(scrollY);
        ViewHelper.setTranslationY(frameHead, headerY);


        final int headerHeight = frameHead.getHeight();
        boolean scrollUp = mPrevScrollY < scrollY;
        int margin_top = getActivity().getResources().getDimensionPixelOffset(R.dimen.margin_20);
        if (scrollUp) {
            if (mFlexibleSpaceImageHeight - headerHeight <= (scrollY - margin_top)) {
                changeHeaderBackgroundHeightAnimated(false, animated);
            }
        } else {
            if ((scrollY - margin_top) <= mFlexibleSpaceImageHeight - headerHeight) {
                changeHeaderBackgroundHeightAnimated(true, animated);
            }
        }

        mPrevScrollY = scrollY;
    }

    /**
     * 取得HeadView的位置
     *
     * @param scrollY
     * @return
     */
    private float getHeaderTranslationY(int scrollY) {
        int margin_top = getActivity().getResources().getDimensionPixelOffset(R.dimen.margin_20);
        return ScrollUtils.getFloat(-scrollY + mFlexibleSpaceImageHeight -
                headView.getHeight() + margin_top, -10, Float.MAX_VALUE);
    }

    /**
     * 是否開始動畫
     *
     * @param shouldShowGap
     * @param animated
     */
    private void changeHeaderBackgroundHeightAnimated(boolean shouldShowGap, boolean animated) {
        if (animated) {
            if (shouldShowGap) {
                headView.setNarrowAnim();
            } else {
                headView.setExpandAnim();
            }
        }
    }


    @Override
    public void onScrollChanged(int scrollY, boolean firstScroll, boolean dragging) {
        updateViews(scrollY, true);
    }

    @Override
    public void onDownMotionEvent() {

    }

    @Override
    public void onUpOrCancelMotionEvent(ScrollState scrollState) {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (handler != null) {
            handler.removeCallbacks(runnable);
            handler.removeCallbacks(updateTimer);
        }
    }

    private void showViewPager(AdvertisingResponse advertisingResponse) {
        List<Fragment> fragments = new ArrayList<>();
        for (int i = 0; i < advertisingResponse.getData().size(); i++) {
            fragments.add(ADFragment.getInstance(advertisingResponse.getData().get(i).getImg_file(), advertisingResponse.getData().get(i).getLink_page(), advertisingResponse.getData().get(i).getAd_id()));
        }
        adAdapter = new ADAdapter(getFragmentManager(), fragments);
        if (advertisingResponse.getCode() != CloudCode.Code_No_data) {
            viewPager.setAdapter(adAdapter);
            viewPager.addOnPageChangeListener(this);
            handler.postDelayed(runnable, 5000);
            setUiPageViewController();
        }


    }


    //----------------------------------API--------------------------------------//
    @Override
    public void onGetMagazineMessage(MagazineResponse magazineResponse, String error) {
        if (magazineResponse != null) {
            if (magazineResponse.getCode() == CloudCode.Code_SUCCESS) {
                dataBeanList = magazineResponse.getData();
                if (dataBeanList.size() > 0)
                    magazineAdapter.setDataList(dataBeanList);
            }
        }
    }

    @Override
    public void GetAdvertisingMessage(AdvertisingResponse advertisingResponse, String Error) {
        if (Objects.equals(Error, "")) {
            if (advertisingResponse != null) {
                showViewPager(advertisingResponse);
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
     * 更新時間
     */
    private Runnable updateTimer = new Runnable() {
        public void run() {
            magazineAdapter.notifyDataSetChanged();
            handler.postDelayed(updateTimer, 60000);
        }
    };

    @OnClick(R.id.img_top)
    public void onClick() {
        recyclerMagazineView.smoothScrollToPosition(0);
    }


}
