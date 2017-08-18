package com.ifair.callback;

import android.app.Activity;
import android.app.ProgressDialog;
import android.view.Window;

import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.request.BaseRequest;
import com.orhanobut.logger.Logger;

import okhttp3.Call;
import okhttp3.Response;

/**
 * 網路請求提示框
 */

public abstract class DialogCallback extends StringCallback {

    private ProgressDialog dialog;


    public DialogCallback(Activity mActivity) {
        dialog = new ProgressDialog(mActivity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); //沒有標題
        dialog.setCanceledOnTouchOutside(false); //設定為false，代表不能點選Dialog以外範圍的區域
        dialog.setCancelable(false);//設定為false，代表不能點選Back
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setMessage("載入中");

    }

    public DialogCallback(Activity mActivity, String message) {
        dialog = new ProgressDialog(mActivity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); //沒有標題
        dialog.setCanceledOnTouchOutside(false); //設定為false，代表不能點選Dialog以外範圍的區域
        dialog.setCancelable(false);//設定為false，代表不能點選Back
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setMessage(message);

    }

    @Override
    public void onBefore(BaseRequest request) {
        super.onBefore(request);
        //網路請求前
        if (dialog != null && !dialog.isShowing()) {
            dialog.show();
        }
    }

    @Override
    public void onAfter(String s, Exception e) {
        super.onAfter(s, e);
        //網路請求後
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }
}
