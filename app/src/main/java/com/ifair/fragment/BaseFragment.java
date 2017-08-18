package com.ifair.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import com.google.gson.Gson;
import com.ideabus.ideabuslibrary.util.ScreenUtils;
import com.ifair.R;
import com.ifair.api.CloudAPI;
import com.ifair.module.LoginResponse;
import com.ifair.myUtil.AppUtil;

/**
 * 所有的 Fragment 都須繼承這個
 */

public abstract class BaseFragment extends Fragment {

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

    /**
     * 登入資訊
     */
    protected LoginResponse loginResponse;

    /**
     * 使用者信箱
     */
    protected String userEmail;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    protected abstract void initParam();

    /**
     * 取得螢幕大小
     */
    protected void getScreenSize() {
        height = ScreenUtils.getScreenHeight(getActivity());
        width = ScreenUtils.getScreenWidth(getActivity());
    }

    /**
     * 跳頁
     * @param mClass
     * @param bundle
     */
    protected void goToActivity(Class mClass, Bundle bundle) {
        Intent intent = new Intent();
        intent.setClass(getActivity(), mClass);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
        getActivity().overridePendingTransition(17432576, 17432577);
    }

    /**
     * 跳頁
     * @param mClass
     * @param bundle
     */
    protected void goToActivity(Class mClass, Bundle bundle, int requestCode) {
        Intent intent = new Intent();
        intent.setClass(getActivity(), mClass);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        startActivityForResult(intent, requestCode);
        getActivity().overridePendingTransition(17432576, 17432577);
    }

    /**
     * 取得登入資訊
     */
    protected void getLoginResponse() {
        Gson gson = new Gson();
        String json = AppUtil.getSharedPref(getActivity(), AppUtil.SHARED_PREF_NAME).getString("loginResponse", "");
        if (json.equals("")) {
            userEmail = "";
            loginResponse = null;
        } else {
            loginResponse = gson.fromJson(json, LoginResponse.class);
            userEmail = loginResponse.getData().get(0).getEmail();
        }
    }

    /**
     * 提示訊息
     * @param message
     */
    protected void alert(String message) {
        new AlertDialog.Builder(getActivity())
                .setTitle("")
                .setMessage(message)
                .setNegativeButton(getString(R.string.alert_confirm), null).show();
    }

}
