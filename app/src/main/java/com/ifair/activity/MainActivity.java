package com.ifair.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.ifair.R;
import com.ifair.eventModule.NfcEvent;
import com.ifair.fragment.FavoriteFragment;
import com.ifair.fragment.MagazineFragment;
import com.ifair.fragment.NFCFragment;
import com.ifair.fragment.NewsFragment;
import com.ifair.fragment.NfcProductFragment;
import com.ifair.fragment.SettingFragment;
import com.ifair.listener.OnFakeNfcListener;
import com.ifair.myUtil.AppUtil;
import com.ifair.myUtil.Variable;


import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends BaseNFcActivity implements  OnFakeNfcListener{

    private static final String TAG = "MainActivity";

    @BindView(R.id.img_bottom_nfc)
    ImageView imgBottomNfc;
    @BindView(R.id.img_bottom_magazine)
    ImageView imgBottomMagazine;
    @BindView(R.id.img_bottom_news)
    ImageView imgBottomNews;
    @BindView(R.id.img_bottom_favorite)
    ImageView imgBottomFavorite;
    @BindView(R.id.img_bottom_setting)
    ImageView imgBottomSetting;
    @BindView(R.id.activity_main)
    RelativeLayout activityMain;

    //紀錄fragment的位置
    private int fragmentLocation;
    //NFC Fragment
    private NFCFragment nfcFragment;
    //商品雜誌 Fragment
    private MagazineFragment magazineFragment;
    //最新消息 Fragment
    private NewsFragment newsFragment;
    //我的最愛 Fragment
    private FavoriteFragment favoriteFragment;
    //設定 Fragment
    private SettingFragment settingFragment;
    //NFC 真偽頁面
    private NfcProductFragment nfcProductFragment;

    //判斷是否離開
    private static Boolean isQuit = false;
    //設置時間
    private Timer timer = new Timer();

    //廣播接收器
    private MainReceive mainReceive;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initParam();
    }

    @Override
    protected void initParam() {

        mainReceive = new MainReceive();
        registerReceiver(mainReceive, new IntentFilter(Variable.Intent_Finish));

        //預設畫面是最新消息
        fragmentLocation = 3;
        newsFragment = NewsFragment.getInstance();
        getSupportFragmentManager().beginTransaction().add(R.id.frame_news, newsFragment, NewsFragment.TAG).commitAllowingStateLoss();
        imgBottomNews.setSelected(true);

        EventBus.getDefault().register(this);

    }


//TODO 先拿掉
//    @Override
//    protected void onNewIntent(Intent intent) {
//        super.onNewIntent(intent);
//
//        //如果已經開始掃描，才可以偵測
//       Logger.d(String.valueOf(myApplication.isStartScan));
//        if (myApplication.isStartScan) {
//            String action = intent.getAction();
//            strTag = "";
//            if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action) ||
//                    NfcAdapter.ACTION_TECH_DISCOVERED.equals(action)) {
//                //代表被掃描到的Tag物件
//                nfcTag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
//            }
//            if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(action)) {
//                nfcTag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
//
//                //取得TagID
//                byte[] tagId = intent.getByteArrayExtra(NfcAdapter.EXTRA_ID);
//                //TagID
//                strTag = AppUtil.bytToHexString(tagId);
//                Logger.d("TagID : " + strTag);
//                //收到TagId後，跟雲端要password
//                cloudAPI.sendTAGId("", strTag);
//            }
//        }
//    }

    /**
     * 重設定底部按鈕狀態
     */
    private void resetUI(int type) {
        //隱藏前一個Fragment
        hideFragment();
        fragmentLocation = type;
        imgBottomNfc.setSelected(false);
        imgBottomMagazine.setSelected(false);
        imgBottomNews.setSelected(false);
        imgBottomFavorite.setSelected(false);
        imgBottomSetting.setSelected(false);

        switch (type) {
            case 1:
                imgBottomNfc.setSelected(true);
                break;
            case 2:
                imgBottomMagazine.setSelected(true);
                break;
            case 3:
                imgBottomNews.setSelected(true);
                break;
            case 4:
                imgBottomFavorite.setSelected(true);
                break;
            case 5:
                imgBottomSetting.setSelected(true);
                break;
            case 6:
                imgBottomNfc.setSelected(true);
                break;
        }
    }

    /**
     * 隱藏Fragment
     */
    private void hideFragment() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        switch (fragmentLocation) {
            case 1:
                transaction.hide(getSupportFragmentManager().findFragmentByTag(NFCFragment.TAG)).commitAllowingStateLoss();
                break;
            case 2:
                transaction.hide(getSupportFragmentManager().findFragmentByTag(MagazineFragment.TAG)).commitAllowingStateLoss();
                break;
            case 3:
                transaction.hide(getSupportFragmentManager().findFragmentByTag(NewsFragment.TAG)).commitAllowingStateLoss();
                break;
            case 4:
                transaction.hide(getSupportFragmentManager().findFragmentByTag(FavoriteFragment.TAG)).commitAllowingStateLoss();
                break;
            case 5:
                transaction.hide(getSupportFragmentManager().findFragmentByTag(SettingFragment.TAG)).commitAllowingStateLoss();
                break;
            case 6:
                transaction.hide(getSupportFragmentManager().findFragmentByTag(NfcProductFragment.TAG)).commitAllowingStateLoss();
                break;

        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (nfcProductFragment != null && nfcProductFragment.isVisible()) {
            resetUI(1);
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            if (nfcFragment == null) {
                nfcFragment = NFCFragment.getInstance();
                transaction.add(R.id.frame_nfc, nfcFragment, NFCFragment.TAG).commitAllowingStateLoss();
            } else {
                transaction.show(nfcFragment).commitAllowingStateLoss();
            }
        } else {
            //如果按下返回鍵
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                if (!isQuit) {
                    isQuit = true;
                    Toast.makeText(this, getString(R.string.alert_back_app), Toast.LENGTH_SHORT).show();
                    TimerTask task = null;
                    task = new TimerTask() {
                        @Override
                        public void run() {
                            isQuit = false;
                        }
                    };
                    timer.schedule(task, 2000);
                } else {
                    finish();
                    System.exit(0);
                }
            }
        }
        return false;
    }

    @Override
    public void showFake() {
        //倒數時間到，沒掃描到Tag，顯示Fake畫面
        runOnUiThread(() -> showNfcProductUI(""));
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void receiveEventBus(NfcEvent event) {
        showNfcProductUI(event.getGoodCode());
    }

    /**
     * 顯示辨識結果畫面
     */
    private void showNfcProductUI(String product_id) {
        new Handler().post(() -> {
            AppUtil.getSharePrefEditor(this, AppUtil.SHARED_PREF_NAME).putString(Variable.Nfc_productId, product_id).commit();
            resetUI(6);
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            if (nfcProductFragment == null) {
                nfcProductFragment = NfcProductFragment.newInstance();
                transaction.add(R.id.frame_nfc, nfcProductFragment, NfcProductFragment.TAG).commitAllowingStateLoss();
            } else {
                transaction.show(nfcProductFragment).commitAllowingStateLoss();
            }
        });
    }

    /**
     * 廣播接收器
     */
    private class MainReceive extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(Variable.Intent_Finish)) {
                goPage(LoginActivity.class, true);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mainReceive);
        EventBus.getDefault().unregister(this);
    }

    @OnClick({R.id.img_bottom_nfc, R.id.img_bottom_magazine, R.id.img_bottom_news, R.id.img_bottom_favorite, R.id.img_bottom_setting})
    public void onClick(View view) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        switch (view.getId()) {
            case R.id.img_bottom_nfc:
                resetUI(1);
                if (nfcFragment == null) {
                    nfcFragment = NFCFragment.getInstance();
                    transaction.add(R.id.frame_nfc, nfcFragment, NFCFragment.TAG).commitAllowingStateLoss();
                } else {
                    transaction.show(nfcFragment).commitAllowingStateLoss();
                }
                break;
            case R.id.img_bottom_magazine:
                resetUI(2);
                if (magazineFragment == null) {
                    magazineFragment = MagazineFragment.getInstance();
                    transaction.add(R.id.frame_magazine, magazineFragment, MagazineFragment.TAG).commitAllowingStateLoss();
                } else {
                    transaction.show(magazineFragment).commitAllowingStateLoss();
                }
                break;
            case R.id.img_bottom_news:
                resetUI(3);
                if (newsFragment == null) {
                    newsFragment = NewsFragment.getInstance();
                    transaction.add(R.id.frame_news, newsFragment, NewsFragment.TAG).commitAllowingStateLoss();
                } else {
                    transaction.show(newsFragment).commitAllowingStateLoss();
                }
                break;
            case R.id.img_bottom_favorite:
                getLoginResponse();
                if (loginResponse != null) {
                    resetUI(4);
                    if (favoriteFragment == null) {
                        favoriteFragment = FavoriteFragment.getInstance();
                        transaction.add(R.id.frame_favorite, favoriteFragment, FavoriteFragment.TAG).commitAllowingStateLoss();
                    } else {
                        transaction.show(favoriteFragment).commitAllowingStateLoss();
                    }
                } else {
                    Toast.makeText(this, getString(R.string.alert_join_member), Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.img_bottom_setting:
                resetUI(5);
                if (settingFragment == null) {
                    settingFragment = SettingFragment.getInstance();
                    transaction.add(R.id.frame_setting, settingFragment, SettingFragment.TAG).commitAllowingStateLoss();
                } else {
                    transaction.show(settingFragment).commitAllowingStateLoss();
                }
                break;
        }
    }
}
