package com.ifair.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ifair.R;
import com.ifair.activity.MagazineDetailActivity;
import com.ifair.adapter.FavoriteAdapter;
import com.ifair.adapter.MagazineAdapter;
import com.ifair.api.CloudAPI;
import com.ifair.api.CloudCode;
import com.ifair.listener.OnGetMagazineListener;
import com.ifair.module.MagazineResponse;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 我的最愛
 */
public class FavoriteFragment extends BaseFragment implements OnGetMagazineListener {

    public static final String TAG = "FavoriteFragment";

    @BindView(R.id.txt_title)
    TextView txtTitle;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.img_top)
    ImageView imgTop;
    @BindView(R.id.linear_top)
    LinearLayout linearTop;

    //商品資料
    private List<MagazineResponse.DataBean> dataBeanList;
    //產品Adapter
    private FavoriteAdapter favoriteAdapter;

    public static FavoriteFragment getInstance() {
        return new FavoriteFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorite, container, false);
        ButterKnife.bind(this, view);

        initRecyclerView();
        initParam();

        return view;
    }

    @Override
    protected void initParam() {
        //取得登入資訊
        getLoginResponse();
        //TITLE設定
        txtTitle.setText(getString(R.string.favorite_title));

        getAPI();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) getAPI();
    }

    /**
     * 取得API
     */
    private void getAPI() {
        cloudAPI = CloudAPI.getInstance();
        cloudAPI.setOnGetMagazineListener(this);
        cloudAPI.getMagazine(userEmail, getActivity());
    }

    /**
     * 初始化RecyclerView
     */
    private void initRecyclerView() {
        View headView = new View(getActivity());
        headView.setLayoutParams(new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0));
        headView.setMinimumHeight(0);

        dataBeanList = new ArrayList<>();
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2);
        gridLayoutManager.setOrientation(GridLayoutManager.VERTICAL);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                int index = (position+1) % 7;
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

        recyclerView.setLayoutManager(gridLayoutManager);

        favoriteAdapter = new FavoriteAdapter(dataBeanList, getActivity());
        recyclerView.setAdapter(favoriteAdapter);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                //因為有headview 所以從0開始
                if (recyclerView.canScrollVertically(-1))
                    linearTop.setVisibility(View.VISIBLE);
                else
                    linearTop.setVisibility(View.GONE);
            }
        });
        favoriteAdapter.setOnMagazineClickListener(position -> {
            Bundle bundle = new Bundle();
            bundle.putString("Goods_id", dataBeanList.get(position).getGoods_id());
            goToActivity(MagazineDetailActivity.class, bundle);
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) getAPI();
    }

    @Override
    public void onGetMagazineMessage(MagazineResponse magazineResponse, String error) {
        if (magazineResponse != null) {
            if (magazineResponse.getCode() == CloudCode.Code_SUCCESS) {
                dataBeanList = magazineResponse.getData();
                favoriteAdapter.setDataList(dataBeanList);
            } else if (magazineResponse.getCode() == CloudCode.Code_No_data) {
                dataBeanList.clear();
                favoriteAdapter.setDataList(dataBeanList);
            }
        }
    }

    @OnClick(R.id.img_top)
    public void onClick() {
        recyclerView.smoothScrollToPosition(0);
    }
}
