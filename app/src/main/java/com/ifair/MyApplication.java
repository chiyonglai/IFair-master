package com.ifair;

import android.app.Application;

import com.lzy.okgo.OkGo;

/**
 * 初始化一些訊息
 */

public class MyApplication extends Application{

    //是否開始掃描
    public boolean isStartScan =  false;

    public boolean isStartScan() {
        return isStartScan;
    }

    public void setStartScan(boolean startScan) {
        isStartScan = startScan;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        OkGo.init(this);

        OkGo.getInstance()
                .setConnectTimeout(10000)  //全局的连接超时时间
                .setReadTimeOut(10000)     //全局的读取超时时间
                .setWriteTimeOut(10000)    //全局的写入超时时间
                .setRetryCount(0)          //超時重連次數
                .setCertificates();

    }
}
