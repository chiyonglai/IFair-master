package com.ifair.fragment;


import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ifair.MyApplication;
import com.ifair.R;
import com.ifair.activity.ProductSubmitActivity;
import com.ifair.eventModule.NfcEvent;
import com.ifair.listener.OnFakeNfcListener;
import com.ifair.myUtil.Variable;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;

/**
 * NFC 頁面
 */
public class NFCFragment extends BaseFragment {

    public static final String TAG = "NFCFragment";
    @BindView(R.id.txt_title)
    TextView txtTitle;
    @BindView(R.id.img_nfc_background)
    ImageView imgNfcBackground;
    @BindView(R.id.view_nfc_icon)
    View viewNfcIcon;
    @BindView(R.id.rela_nfc_phone)
    RelativeLayout relaNfcPhone;
    @BindView(R.id.txt_nfc)
    TextView txtNfc;
    @BindView(R.id.nfc_gif)
    ImageView nfcGif;
    @BindView(R.id.img_nfc_gif)
    GifImageView imgNfcGif;
    @BindView(R.id.linear_error)
    LinearLayout linearError;
    @BindView(R.id.rela_root)
    RelativeLayout relaRoot;

    private GifDrawable gifDrawable;
    private NfcAdapter nfcAdapter;


    private OnFakeNfcListener onFakeNfcListener;

    public static NFCFragment getInstance() {
        return new NFCFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            onFakeNfcListener = (OnFakeNfcListener) context;
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_nfc, container, false);
        ButterKnife.bind(this, view);
        adjustView();
        initParam();

        return view;
    }

    /**
     * 動態設定UI
     */
    private void adjustView() {
        getScreenSize();
        RelativeLayout.LayoutParams nfcBackground = (RelativeLayout.LayoutParams) imgNfcBackground.getLayoutParams();
        nfcBackground.setMargins(0, (int) (height * 0.257), 0, 0);
        imgNfcBackground.setLayoutParams(nfcBackground);

        RelativeLayout.LayoutParams nfcPhone = (RelativeLayout.LayoutParams) relaNfcPhone.getLayoutParams();
        nfcPhone.setMargins(0, (int) (height * 0.06), 0, 0);
        relaNfcPhone.setLayoutParams(nfcPhone);
    }

    @Override
    protected void initParam() {
        //設定標題
        txtTitle.setText(getActivity().getString(R.string.nfc_title));
        gifDrawable = (GifDrawable) imgNfcGif.getDrawable();
        resetGif();

        nfcAdapter = NfcAdapter.getDefaultAdapter(getActivity());
        initNfc();
    }

    /**
     * 初始化設定NFC
     */
    private void initNfc() {
        if (nfcAdapter != null) {
            if (nfcAdapter.isEnabled()) {
                if (!gifDrawable.isPlaying()) {
                    gifDrawable.start();
                    txtNfc.setText(getString(R.string.nfc_scanning));
                }
            } else {
                txtNfc.setText("請先打開NFC");
                new AlertDialog.Builder(getActivity())
                        .setTitle("警告")
                        .setMessage("請開啟NFC功能，否則無法使用此功能")
                        .setPositiveButton("設定", (dialog, which) -> {
                            Intent intent = new Intent("android.settings.NFC_SETTINGS");
                            startActivityForResult(intent, 1);
                        })
                        .setNegativeButton("取消", (dialog, which) -> dialog.dismiss())
                        .show();
            }
        }
    }

    /**
     * 重置GIF
     */
    private void resetGif() {
        gifDrawable.stop();
        gifDrawable.seekToFrameAndGet(5);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (!isHidden()) initNfc();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this);
    }


    @Override
    public void onStop() {
        super.onStop();
        resetGif();
        EventBus.getDefault().unregister(this);
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onNfcMessageEvent(NfcEvent nfcEvent) {
        Log.i(TAG, "onNfcMessageEvent: sadasdas");
////        MyApplication myApplication = (MyApplication) getActivity().getApplication();
////        myApplication.setStartScan(false);
//        //先通知關閉當前Activity
//        Intent closeIntent = new Intent(Variable.Intent_Close_NFC_Activity);
//        getActivity().sendBroadcast(closeIntent);
//
//        //進到Nfc產品頁面
////        Intent intent = new Intent();
////        intent.setClass(getActivity(), NFCProductActivity.class);
////        Bundle bundle = new Bundle();
////        bundle.putString("product_id", nfcEvent.getGoodCode());
////        intent.putExtras(bundle);
////        startActivityForResult(intent, 1);
//
//        resetGif();
    }


    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {
            Log.i(TAG, "onHiddenChanged: " + hidden);
            txtNfc.setText(getString(R.string.nfc_touch_scan));
            resetGif();
        } else {
            initNfc();
        }
    }


    @OnClick({R.id.linear_error, R.id.rela_root})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.linear_error:
                goToActivity(ProductSubmitActivity.class, null);
                break;
            case R.id.rela_root:
                initNfc();
                break;
        }
    }
}
