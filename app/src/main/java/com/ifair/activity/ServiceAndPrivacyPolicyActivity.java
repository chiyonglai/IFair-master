package com.ifair.activity;

import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.ifair.MyApplication;
import com.ifair.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ServiceAndPrivacyPolicyActivity extends BaseActivity {

    @BindView(R.id.img_back)
    ImageView imgBack;
    @BindView(R.id.txt_title)
    TextView txtTitle;
    @BindView(R.id.webview)
    WebView webview;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_privacy_policy);
        ButterKnife.bind(this);
        initParam();
        MyApplication myApplication = (MyApplication) getApplication();
        myApplication.setStartScan(false);
    }

    @Override
    protected void initParam() {
        txtTitle.setText(getString(R.string.navagation_privacy_policy_2));
        setWebView();
    }
    private void setWebView() {
        WebSettings webViewSettings = webview.getSettings();
        webViewSettings.setJavaScriptEnabled(true);
        // Initialize a webview client
        webview.setWebViewClient(new WebViewClient() {
        });
        // Show main page
        webview.loadUrl("file:///android_asset/serviceandprivacypolicy.html");
    }

    @OnClick(R.id.img_back)
    public void onClick() {
        finish();
    }
}
