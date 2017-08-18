package com.ifair.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ifair.R;
import com.ifair.activity.NewsDetailActivity;
import com.ifair.adapter.NewsAdapter;
import com.ifair.api.CloudAPI;
import com.ifair.api.CloudCode;
import com.ifair.eventModule.NewEvent;
import com.ifair.listener.OnGetAddLikeListener;
import com.ifair.listener.OnGetLatestNewsListener;
import com.ifair.listener.OnNewsClickListener;
import com.ifair.module.MessageResponse;
import com.ifair.myUtil.AppUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 最新消息頁面
 */
public class NewsFragment extends BaseFragment implements OnGetLatestNewsListener, OnGetAddLikeListener {

    public static final String TAG = "NewsFragment";
    @BindView(R.id.txt_title)
    TextView txtTitle;
    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;
    @BindView(R.id.img_top)
    ImageView imgTop;
    @BindView(R.id.linear_top)
    LinearLayout linearTop;


    private NewsAdapter newsAdapter;
    //最新消息資料
    private List<MessageResponse.DataBean> dataBeanList;
    //接收的Code
    private static final int mRequestCode = 1;


    public static NewsFragment getInstance() {
        return new NewsFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news, container, false);
        ButterKnife.bind(this, view);

        initRecyclerView();
        initParam();
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }

    }


    /**
     * 初始化RecyclerView
     */
    private void initRecyclerView() {

        dataBeanList = new ArrayList<>();


        recyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerview.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
        newsAdapter = new NewsAdapter(getActivity(), dataBeanList);
//        SlideInBottomAnimationAdapter slideInBottomAnimationAdapter = new SlideInBottomAnimationAdapter(newsAdapter);
//        slideInBottomAnimationAdapter.setFirstOnly(false);
//        slideInBottomAnimationAdapter.setInterpolator(new AnticipateInterpolator());
//        slideInBottomAnimationAdapter.setDuration(2500);

        recyclerview.setAdapter(newsAdapter);
        newsAdapter.setOnNewsClickListener(new OnNewsClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent intent = new Intent();
                intent.setClass(getActivity(), NewsDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("message_id", dataBeanList.get(position).getMessage_id());
                intent.putExtras(bundle);
                startActivityForResult(intent, mRequestCode);
            }

            @Override
            public void onLikeClick(int position) {
                if (dataBeanList.get(position).getIs_like().equals("false")) {
                    cloudAPI.setOnGetAddLikeListener(NewsFragment.this);
                    cloudAPI.addLikeMessage(dataBeanList.get(position).getMessage_id(), AppUtil.getMacAddress());
                }
            }
        });

    }

    @Override
    protected void initParam() {

        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }

        txtTitle.setText(getString(R.string.news_title));

        callApi();
    }

    /**
     * 呼叫API
     */
    private void callApi() {
        cloudAPI = CloudAPI.getInstance();
        cloudAPI.setOnGetLatestNewsListener(this);
        cloudAPI.getLatestNews(AppUtil.getMacAddress());
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) callApi();
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refreshUI(NewEvent newEvent) {
        if (newEvent.isLike())
            cloudAPI.getLatestNews(AppUtil.getMacAddress());
    }

    @OnClick(R.id.img_top)
    public void onClick() {
        recyclerview.smoothScrollToPosition(0);
    }

    //-------------------------------API-------------------------------//

    @Override
    public void getLatestNewsMessage(MessageResponse messageResponse, String error) {
        if (messageResponse != null) {
            if (messageResponse.getCode() == CloudCode.Code_SUCCESS) {
                this.dataBeanList = messageResponse.getData();
                newsAdapter.updateUI(dataBeanList);
            }
        }
    }

    @Override
    public void getAddLikeMessage(int code, String error) {
        if (code == CloudCode.Code_SUCCESS) {
            cloudAPI.getLatestNews(AppUtil.getMacAddress());
        }
    }


    //-------------------------------API-------------------------------//

}
