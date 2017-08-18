package com.ifair.activity;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Base64;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;
import com.ideabus.ideabuslibrary.util.ScreenUtils;
import com.ifair.BuildConfig;
import com.ifair.R;
import com.ifair.api.CloudAPI;
import com.ifair.api.CloudCode;
import com.ifair.firebase.FireBaseService;
import com.ifair.listener.OnSendLoginListener;
import com.ifair.module.LoginResponse;
import com.ifair.myUtil.AppUtil;
import com.orhanobut.logger.Logger;

import java.io.UnsupportedEncodingException;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LogoActivity extends BaseActivity implements OnSendLoginListener{

    @BindView(R.id.img_logo)
    ImageView imgLogo;
    @BindView(R.id.activity_logo)
    RelativeLayout activityLogo;
    @BindView(R.id.txt_version)
    TextView txtVersion;

    //加密後的密碼
    private String encryptPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ScreenUtils.SetFullScreen(this);
        setContentView(R.layout.activity_logo);
        ButterKnife.bind(this);
        getLoginResponse();
        initParam();

    }

    @Override
    protected void initParam() {

        startService(new Intent(this, FireBaseService.class));

        ObjectAnimator animator = ObjectAnimator.ofFloat(imgLogo, "alpha", 0, 1);
        animator.setDuration(1500);
        animator.start();
        encryptPassword = AppUtil.getSharedPref(LogoActivity.this, AppUtil.SHARED_PREF_NAME).getString("userPassword", "");

        Timer timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                if (loginResponse != null && !Objects.equals(encryptPassword, "")) {
                    checkLogin(encryptPassword);
                } else {
                    goPage(LoginActivity.class, true);
                }
            }
        };
        timer.schedule(timerTask, 3000);

        txtVersion.setText(getString(R.string.logo_version, BuildConfig.VERSION_NAME));
    }

    /**
     * 確認登入
     * @param encryptPassword
     */
    private void checkLogin(String encryptPassword) {
        runOnUiThread(() -> {
            try {
                byte[] bytePassword = AppUtil.DecryptAES(Base64.decode(encryptPassword, Base64.DEFAULT));
                String token = FirebaseInstanceId.getInstance().getToken();
                cloudAPI = CloudAPI.getInstance();
                cloudAPI.setOnSendLoginListener(LogoActivity.this);
                cloudAPI.sendLogin(loginResponse.getData().get(0).getEmail(), new String(bytePassword, "UTF-8"), token == null ? "" : token, LogoActivity.this);
            }catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

        });
    }

    @Override
    public void onSendLoginMessage(String json, String error) {
        if (error == null) {
            Gson gson = new Gson();
            LoginResponse loginResponse = gson.fromJson(json, LoginResponse.class);

            if (loginResponse.getCode() == CloudCode.Code_SUCCESS) {
                AppUtil.getSharePrefEditor(this, AppUtil.SHARED_PREF_NAME).putString("loginResponse", json).commit();
                try {
                    if (getIntent().getExtras() == null || getIntent().getExtras().getString("message_id") == null) {
                        goPage(MainActivity.class, true);
                    } else {
                        Logger.d(getIntent().getExtras().getString("message_id"));
                        //推播消息-點擊數
                        cloudAPI = CloudAPI.getInstance();
                        cloudAPI.onAddPushLikeMessage(userEmail, getIntent().getExtras().getString("message_id"), this);
                        Bundle bundle = new Bundle();
                        bundle.putString("message_id",getIntent().getExtras().getString("message_id"));
                        goPage( NewsDetailActivity.class, bundle, true);
                    }
                } catch (NullPointerException e) {
                    e.printStackTrace();
                    goPage(MainActivity.class, true);
                }

            } else {
                AppUtil.getSharePrefEditor(this, AppUtil.SHARED_PREF_NAME).clear().commit();
                goPage(LoginActivity.class, true);
            }
        } else {
            new AlertDialog.Builder(this)
                    .setCancelable(false)
                    .setMessage(getString(R.string.alert_no_network))
                    .setTitle(getString(R.string.alert_warming))
                    .setNegativeButton(getString(R.string.alert_confirm), (dialog1, which) -> {
                        checkLogin(encryptPassword);
                    }).show();
        }
    }
}
