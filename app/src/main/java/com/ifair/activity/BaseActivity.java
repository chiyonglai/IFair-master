package com.ifair.activity;

import android.Manifest;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.nfc.NfcAdapter;
import android.nfc.tech.NfcA;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.customtabs.CustomTabsIntent;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.ideabus.ideabuslibrary.util.ScreenUtils;
import com.ifair.MyApplication;
import com.ifair.R;
import com.ifair.api.CloudAPI;
import com.ifair.module.LoginResponse;
import com.ifair.myUtil.AppUtil;
import com.ifair.myUtil.Variable;
import com.orhanobut.logger.Logger;

/**
 * 所有的Activity都繼承這裡
 */

public abstract class BaseActivity extends AppCompatActivity {

    private static final String TAG = "BaseActivity";

    /**
     * 螢幕高度
     */
    protected int height;

    /**
     * 螢幕寬度
     */
    protected int width;

    /**
     * 雲端API
     */
    protected CloudAPI cloudAPI;

    public static String[] File_Permission = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA};
    public static final int PREMISSIONSFileRequest = 1;

    /**
     * 載入Bar
     */
    protected ProgressDialog dialog;

    /**
     * 登入資訊
     */
    protected LoginResponse loginResponse;
    /**
     * 使用者信箱
     */
    protected String userEmail;
    public NfcAdapter nfcAdapter;
    public PendingIntent pendingIntent;
    /**
     * NFC Flag
     */
    protected int nfcFlag = NfcAdapter.FLAG_READER_NFC_A |
            NfcAdapter.FLAG_READER_NFC_B |
            NfcAdapter.FLAG_READER_NFC_F |
            NfcAdapter.FLAG_READER_NFC_V |
            NfcAdapter.FLAG_READER_NFC_BARCODE|
            NfcAdapter.FLAG_READER_NO_PLATFORM_SOUNDS;





    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initNFC();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (nfcAdapter != null) {
            //啟用前臺排程，避免掃描到NFC而跳到其他APP
            nfcAdapter.enableForegroundDispatch(this, pendingIntent, null, null);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (nfcAdapter != null) {
            try {
                //關閉前台排程
                nfcAdapter.disableForegroundDispatch(this);
            }catch (IllegalStateException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * APP一開始做的工作都在這邊
     */
    protected abstract void initParam();

    /**
     * 從A跳到B Activity
     * @param mClass 要intent的Activity
     * @param isFinish 是否finish這個Acticity
     */
    protected void goPage(Class mClass, boolean isFinish) {
        Intent it = new Intent(this, mClass);
        this.startActivity(it);
        this.overridePendingTransition(17432576, 17432577);
        if(isFinish) {
            this.finish();
        }
    }

    /**
     * 跳頁
     * @param mClass
     * @param bundle
     */
    protected void goPage(Class mClass, Bundle bundle, boolean isFinish) {
        Intent intent = new Intent();
        intent.setClass(this, mClass);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
        this.overridePendingTransition(17432576, 17432577);
        if(isFinish) {
            this.finish();
        }
    }

    /**
     * 取得螢幕大小
     */
    protected void getScreenSize(Context mContext) {
        height = ScreenUtils.getScreenHeight(mContext);
        width = ScreenUtils.getScreenWidth(mContext);
    }

    /**
     * 收起軟鍵盤
     */
    protected void hideSoftInput(){
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    /**
     * 打開軟鍵盤
     */
    protected void openSoftInput(){
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.toggleSoftInputFromWindow(view.getWindowToken(), InputMethodManager.SHOW_FORCED, 0);
        }
    }

    /**
     * 打開鍵盤
     * @param editText 要為哪一個editText打開鍵盤
     */
    protected void openSoftInput(EditText editText) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInputFromWindow(editText.getWindowToken(), InputMethodManager.SHOW_FORCED, 0);
    }

    /**
     * 確認權限
     * @param view
     * @return
     */
    protected boolean checkFilePermission(View view) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE) ||
                    ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) ||
                    ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
                Snackbar.make(view, "需要給予權限", Snackbar.LENGTH_INDEFINITE)
                        .setAction("確認", view1 -> {
                            ActivityCompat.requestPermissions(this, File_Permission, PREMISSIONSFileRequest);
                        }).show();
                return false;
            } else {
                ActivityCompat.requestPermissions(this, File_Permission, PREMISSIONSFileRequest);
                return false;
            }
        } else {
            return true;
        }
    }

    /**
     * 顯示訊息
     * @param message
     */
    protected void showDialog(String message) {
        dialog = new ProgressDialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); //沒有標題
        dialog.setCanceledOnTouchOutside(false); //設定為false，代表不能點選Dialog以外範圍的區域
        dialog.setCancelable(false);//設定為false，代表不能點選Back
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setMessage(message);
        dialog.show();
    }

    /**
     * 初始化NFC設定
     */
    protected void initNFC() {
        Log.i(TAG, "initNFC: 初始化NFC" );
        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        if (nfcAdapter != null) {
            nfcAdapter.enableReaderMode(this, null, nfcFlag, null);
            pendingIntent = PendingIntent.getActivity(this, 0,
                    new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
            IntentFilter nfcIntentFilter = new IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED);
            try {
                nfcIntentFilter.addDataType("*/*");
            } catch (IntentFilter.MalformedMimeTypeException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 提示訊息
     * @param message
     */
    protected void alert(String message) {
        new AlertDialog.Builder(this)
                .setTitle("")
                .setMessage(message)
                .setNegativeButton(getString(R.string.alert_confirm), null).show();
    }

    /**
     * 提示訊息
     * @param message
     */
    protected void alert(String title,String message) {
        new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)
                .setNegativeButton(getString(R.string.alert_confirm), null).show();
    }

    /**
     * 取得登入資訊
     */
    protected void getLoginResponse() {
        Gson gson = new Gson();
        String json = AppUtil.getSharedPref(this, AppUtil.SHARED_PREF_NAME).getString("loginResponse", "");
        if (json.equals("")) {
            loginResponse = null;
            userEmail = "";
        } else {
            loginResponse = gson.fromJson(json, LoginResponse.class);
            userEmail = loginResponse.getData().get(0).getEmail();
        }
        Log.d(TAG, "getLoginResponse: userEmail="+userEmail);
    }

    /**
     * 隱藏Status Bar
     */
    protected void hideStatusBar() {
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(this, android.R.color.transparent));
        }
    }

    /**
     * 開啟網頁
     * @param url 網址
     */
    protected void intentUrl(String url) {
        try {

            CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
            builder.setToolbarColor(getResources().getColor(R.color.colorAccent));
            builder.setCloseButtonIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_arrow_back));
            CustomTabsIntent customTabsIntent = builder.build();
            customTabsIntent.launchUrl(this, Uri.parse(url));
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
        }
    }

}


