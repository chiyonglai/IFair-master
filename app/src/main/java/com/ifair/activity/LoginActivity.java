package com.ifair.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;
import com.ideabus.ideabuslibrary.util.BaseUtils;
import com.ideabus.ideabuslibrary.util.SizeUtils;
import com.ifair.MyApplication;
import com.ifair.R;
import com.ifair.adapter.ADNewsAdapter;
import com.ifair.api.CloudAPI;
import com.ifair.api.CloudCode;
import com.ifair.fragment.ADNavagationFragment;
import com.ifair.listener.OnSendLoginListener;
import com.ifair.module.LoginResponse;
import com.ifair.myUtil.AppUtil;
import com.ifair.myUtil.MyRegularUtils;
import com.orhanobut.logger.Logger;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends BaseActivity implements OnSendLoginListener, ViewPager.OnPageChangeListener {

    @BindView(R.id.img_logo)
    ImageView imgLogo;
    @BindView(R.id.edt_account)
    EditText edtAccount;
    @BindView(R.id.linear_account)
    LinearLayout linearAccount;
    @BindView(R.id.edt_password)
    EditText edtPassword;
    @BindView(R.id.txt_forget_password)
    TextView txtForgetPassword;
    @BindView(R.id.linear_password)
    LinearLayout linearPassword;
    @BindView(R.id.btn_register)
    Button btnRegister;
    @BindView(R.id.txt_facebook)
    TextView txtFacebook;
    @BindView(R.id.activity_login)
    RelativeLayout activityLogin;
    @BindView(R.id.linear_facebook)
    LinearLayout linearFacebook;
    @BindView(R.id.txt_skip)
    TextView txtSkip;
    @BindView(R.id.view_pager_navigation)
    ViewPager viewPagerNavigation;
    @BindView(R.id.viewGroup)
    LinearLayout viewGroup;
    @BindView(R.id.tv_terms_of_service_1)
    TextView tvTermsOfService1;
    @BindView(R.id.tv_terms_of_service_2)
    TextView tvTermsOfService2;
    @BindView(R.id.btn_navagation_login)
    Button btnNavagationLogin;
    @BindView(R.id.rl_navagation)
    RelativeLayout rlNavagation;
    @BindView(R.id.ll_login)
    LinearLayout llLogin;
    @BindView(R.id.btn_login)
    Button btnLogin;
    @BindView(R.id.view_line_1)
    View viewLine1;
    @BindView(R.id.tv_terms_of_service_3)
    TextView tvTermsOfService3;
    @BindView(R.id.img_back)
    ImageView imgBack;

    private CallbackManager callbackManager;
    private AccessToken accessToken;
    /**
     * isEmailRegister 是否是FB登入 true  一般；FB false
     * obj_FBLogin  FB登入的個人資料
     */
    private Boolean isEmailRegister;
    private JSONObject obj_FBLogin;
    //使用者密碼
    private String password;
    private String account;
    //導覽頁廣告Adapter
    private ADNewsAdapter adNewsAdapter;
    //廣告執行緒
    private Handler handler = new Handler();
    //viewPager 總數
    private int dotCount;
    //指示器 圖片
    private ImageView[] dots;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        FacebookSdk.sdkInitialize(getApplicationContext());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        MyApplication myApplication = (MyApplication) getApplication();
        myApplication.setStartScan(false);

        adjustUI();
        initParam();
        initViewPager();
    }

    /**
     * 動態設定UI
     */
    private void adjustUI() {
        callbackManager = CallbackManager.Factory.create();
        getScreenSize(this);
        LinearLayout.LayoutParams linearAccountLp = (LinearLayout.LayoutParams) linearAccount.getLayoutParams();
        linearAccountLp.width = (int) (width * 0.8);
        linearAccount.setLayoutParams(linearAccountLp);

        LinearLayout.LayoutParams linearPasswordLp = (LinearLayout.LayoutParams) linearPassword.getLayoutParams();
        linearPasswordLp.width = (int) (width * 0.8);
        linearPassword.setLayoutParams(linearPasswordLp);

        LinearLayout.LayoutParams btnNavagationLoginLp = (LinearLayout.LayoutParams) btnNavagationLogin.getLayoutParams();
        btnNavagationLoginLp.setMargins(0, (int) (height * 0.02), 0, 0);
        btnNavagationLoginLp.width = (int) (width * 0.8);
        btnNavagationLoginLp.height = (int) (height * 0.07);
        btnNavagationLogin.setLayoutParams(btnNavagationLoginLp);

        LinearLayout.LayoutParams btnRegisterLp = (LinearLayout.LayoutParams) btnRegister.getLayoutParams();
        btnRegisterLp.setMargins(0, (int) (height * 0.02), 0, 0);
        btnRegisterLp.width = (int) (width * 0.8);
        btnRegisterLp.height = (int) (height * 0.07);
        btnRegister.setLayoutParams(btnRegisterLp);

        LinearLayout.LayoutParams linearFacebookLayoutParams = (LinearLayout.LayoutParams) linearFacebook.getLayoutParams();
        linearFacebookLayoutParams.setMargins(0, (int) (height * 0.02), 0, 0);
        linearFacebookLayoutParams.width = (int) (width * 0.83);
        linearFacebookLayoutParams.height = (int) (height * 0.085);
        linearFacebook.setLayoutParams(linearFacebookLayoutParams);

        LinearLayout.LayoutParams viewGroupLp = (LinearLayout.LayoutParams) viewGroup.getLayoutParams();
        viewGroupLp.setMargins(0, (int) (height * 0.52), 0, 0);
        viewGroup.setLayoutParams(viewGroupLp);


        LinearLayout.LayoutParams tvTermsOfService1Lp = (LinearLayout.LayoutParams) tvTermsOfService1.getLayoutParams();
        tvTermsOfService1Lp.topMargin = SizeUtils.dp2px(this, 5);
        tvTermsOfService1.setLayoutParams(tvTermsOfService1Lp);

        LinearLayout.LayoutParams tvTermsOfService2Lp = (LinearLayout.LayoutParams) tvTermsOfService2.getLayoutParams();
        tvTermsOfService2Lp.topMargin = SizeUtils.dp2px(this, 5);
        tvTermsOfService2.setLayoutParams(tvTermsOfService2Lp);


        LinearLayout.LayoutParams btnLoginLp = (LinearLayout.LayoutParams) btnLogin.getLayoutParams();
        btnLoginLp.width = (int) (width * 0.8);
        btnLoginLp.height = (int) (height * 0.07);
        btnLogin.setLayoutParams(btnLoginLp);


        LinearLayout.LayoutParams imgLogoLp = (LinearLayout.LayoutParams) imgLogo.getLayoutParams();
        imgLogoLp.topMargin = (int) (height * 0.1);
        imgLogoLp.width = (int) (width * 0.3);
        imgLogoLp.height = (int) (width * 0.3);
        imgLogo.setLayoutParams(imgLogoLp);


        LinearLayout.LayoutParams viewLine1Lp = (LinearLayout.LayoutParams) viewLine1.getLayoutParams();
        viewLine1Lp.setMargins(SizeUtils.dp2px(this, 10), SizeUtils.dp2px(this, 50), SizeUtils.dp2px(this, 10), 0);
        viewLine1.setLayoutParams(viewLine1Lp);

    }

    @Override
    protected void initParam() {
        edtAccount.setText(AppUtil.getSharedPref(LoginActivity.this, AppUtil.SHARED_PREF_NAME).getString("lastUserAccount", ""));
    }

    private void initViewPager() {
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(new ADNavagationFragment().getInstance(R.drawable.walkthrough_bg_a_ifair, getString(R.string.navagation_title_1), getString(R.string.navagation_scription_1)));
        fragments.add(new ADNavagationFragment().getInstance(R.drawable.walkthrough_bg_a_news, getString(R.string.navagation_title_2), getString(R.string.navagation_scription_2)));
        fragments.add(new ADNavagationFragment().getInstance(R.drawable.walkthrough_bg_a_magazine, getString(R.string.navagation_title_3), getString(R.string.navagation_scription_3)));
        fragments.add(new ADNavagationFragment().getInstance(R.drawable.walkthrough_bg_a_nfc, getString(R.string.navagation_title_4), getString(R.string.navagation_scription_4)));
        adNewsAdapter = new ADNewsAdapter(getSupportFragmentManager(), fragments);
        viewPagerNavigation.setAdapter(adNewsAdapter);
        viewPagerNavigation.addOnPageChangeListener(this);
        handler.postDelayed(runnable, 3000);
        setUiPageViewController();
    }

    /**
     * 廣告輪播的執行緒
     */
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            viewPagerNavigation.setCurrentItem((viewPagerNavigation.getCurrentItem() + 1) % viewPagerNavigation.getAdapter().getCount());
            handler.postDelayed(runnable, 3000);
        }
    };

    /**
     * setting Indicator
     */
    private void setUiPageViewController() {
        dotCount = adNewsAdapter.getCount();
        dots = new ImageView[dotCount];

        for (int i = 0; i < dotCount; i++) {
            dots[i] = new ImageView(this);
            dots[i].setImageDrawable(getResources().getDrawable(R.drawable.non_select_dot));
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(8, 4, 8, 4);
            viewGroup.addView(dots[i], params);
        }
        dots[0].setImageDrawable(getResources().getDrawable(R.drawable.select_dot)); //預設一進畫面，是被選中的
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        for (int i = 0; i < dotCount; i++) {
            dots[i].setImageDrawable(getResources().getDrawable(R.drawable.non_select_dot));
        }
        dots[position].setImageDrawable(getResources().getDrawable(R.drawable.select_dot));
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    /**
     * 接登入API
     */
    private void loginAPI(Boolean LoginType) {
        isEmailRegister = LoginType;
        cloudAPI = CloudAPI.getInstance();
        cloudAPI.setOnSendLoginListener(this);
        String token = FirebaseInstanceId.getInstance().getToken();
        if (token == null) token = "";
        if (!LoginType) {
            //FB登入
            String FBPassword;  //FB登入的密碼
            if (obj_FBLogin.optString("email").length() > 0) {
                FBPassword = AppUtil.md5(obj_FBLogin.optString("email"));
                cloudAPI.sendLogin(obj_FBLogin.optString("email"), FBPassword, token,  this);
            } else {
                FBPassword = AppUtil.md5(obj_FBLogin.optString("id"));
                cloudAPI.sendLogin(obj_FBLogin.optString("id"), FBPassword, token, this);
            }
            password = FBPassword;
        } else {
            //一般登入
            cloudAPI.sendLogin(AppUtil.editToString(edtAccount), AppUtil.editToString(edtPassword), token, this);
            password = AppUtil.editToString(edtPassword);
        }
    }

    /**
     * FB 登入
     */
    private void FBLoginManager() {
        // LoginManager.getInstance().logOut();  FB登出
        LoginManager.getInstance().logInWithReadPermissions(LoginActivity.this, Arrays.asList("public_profile", "email", "user_friends"));
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            //登入成功
            @Override
            public void onSuccess(LoginResult loginResult) {
                //accessToken之後或許還會用到 先存起來
                accessToken = loginResult.getAccessToken();
                GraphRequest request = GraphRequest.newMeRequest(
                        accessToken,
                        (object, response) -> {
                            obj_FBLogin = object;
                            Logger.d(obj_FBLogin.optString("email"));
                            loginAPI(false);
                        });
                //包入你想要得到的資料 送出request
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email,birthday");
                request.setParameters(parameters);
                request.executeAsync();
            }

            //登入取消
            @Override
            public void onCancel() {
                // App code
            }

            //登入失敗
            @Override
            public void onError(FacebookException exception) {
                Logger.d(exception.getMessage());
            }
        });
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 確認資料格式
     *
     * @return
     */
    private boolean checkData() {
        if (Objects.equals(AppUtil.editToString(edtAccount), "") || Objects.equals(AppUtil.editToString(edtPassword), "")) {
            alert(getString(R.string.login_account_pw));
            return false;
        } else if (!MyRegularUtils.isEmail(AppUtil.editToString(edtAccount))) {
            alert(getString(R.string.login_email_error_format));
            return false;
        } else if (!MyRegularUtils.isPassword(AppUtil.editToString(edtPassword))) {
            alert(getString(R.string.login_password_error_format));
            return false;
        }

        return true;
    }

    @Override
    public void onBackPressed() {
        if (llLogin.getVisibility() == View.VISIBLE) {
            llLogin.setVisibility(View.GONE);
            rlNavagation.setVisibility(View.VISIBLE);
        } else {
            super.onBackPressed();
        }
    }

    //------------------------------------API------------------------------------//

    @Override
    public void onSendLoginMessage(String json, String error) {
        if (error == null) {
            Gson gson = new Gson();
            LoginResponse loginResponse = gson.fromJson(json, LoginResponse.class);
            if (loginResponse.getCode() == CloudCode.Code_No_data) {
                if (!isEmailRegister) {
                    Bundle bundle_facebook = new Bundle();
                    //是FACEBOOK會員註冊
                    //FaceBook的 ID，姓名，生日，電子郵件
                    bundle_facebook.putBoolean("m_boolean_ordinary", false);
                    String FBPassword = "";  //FB登入的密碼
                    if (obj_FBLogin.optString("email").length() > 0) {
                        FBPassword = AppUtil.md5(obj_FBLogin.optString("email"));
                        bundle_facebook.putString("m_facebook_account", obj_FBLogin.optString("email"));
                    } else {
                        FBPassword = AppUtil.md5(obj_FBLogin.optString("id"));
                        bundle_facebook.putString("m_facebook_account", obj_FBLogin.optString("id"));
                    }
                    bundle_facebook.putString("m_facebook_password", FBPassword);
                    bundle_facebook.putString("m_facebook_name", obj_FBLogin.optString("name"));
                    bundle_facebook.putString("m_facebook_birthday", obj_FBLogin.optString("birthday"));
                    goPage(RegisterActivity.class, bundle_facebook, false);

                } else {
                    alert(getString(R.string.login_no_account));
                }
            } else if (loginResponse.getCode() == CloudCode.Code_Account_Error) {
                alert(getString(R.string.login_account_error));
            } else if (loginResponse.getCode() == CloudCode.Code_Password_Error) {
                if (!isEmailRegister) {
                    alert(getString(R.string.login_not_third_party));
                } else {
                    alert(getString(R.string.login_password_error));
                }
            } else if (loginResponse.getCode() == CloudCode.Code_SUCCESS) {
                AppUtil.getSharePrefEditor(LoginActivity.this, AppUtil.SHARED_PREF_NAME).putString("loginResponse", json).commit();
                try {
                    AppUtil.getSharePrefEditor(LoginActivity.this, AppUtil.SHARED_PREF_NAME).putString("userPassword", Base64.encodeToString(AppUtil.EncryptAES(password.getBytes("UTF-8")), Base64.DEFAULT)).commit();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                goPage(MainActivity.class, true);
            }
        } else {
            new AlertDialog.Builder(this)
                    .setMessage(getString(R.string.alert_no_network))
                    .setTitle(getString(R.string.alert_warming))
                    .setNegativeButton(getString(R.string.alert_confirm), (dialog1, which) -> {
                        loginAPI(isEmailRegister);
                    }).show();
        }
    }
    //------------------------------------API------------------------------------//

    @OnClick({R.id.txt_forget_password, R.id.btn_login, R.id.btn_navagation_login, R.id.btn_register, R.id.linear_facebook, R.id.txt_skip, R.id.tv_terms_of_service_2, R.id.tv_terms_of_service_3, R.id.img_back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.txt_forget_password:
                goPage(ForgetPasswordActivity.class, false);
                break;
            case R.id.btn_navagation_login:
                rlNavagation.setVisibility(View.GONE);
                llLogin.setVisibility(View.VISIBLE);
                break;
            case R.id.btn_login:
                if (checkData()) {
                    loginAPI(true);
                }
                break;
            case R.id.btn_register:
                Bundle bundle_register = new Bundle();
                //是一般會員註冊
                bundle_register.putBoolean("m_boolean_ordinary", true);
                goPage(RegisterActivity.class, bundle_register, false);
                break;
            case R.id.linear_facebook:
                FBLoginManager();
                break;
            case R.id.txt_skip:
                AppUtil.getSharePrefEditor(this, AppUtil.SHARED_PREF_NAME).clear().commit();
                goPage(MainActivity.class, true);
                break;
            case R.id.tv_terms_of_service_2:
                goPage(ServiceAndPrivacyPolicyActivity.class, false);
                break;
            case R.id.tv_terms_of_service_3:
                goPage(ServiceAndPrivacyPolicyActivity.class, false);
                break;
            case R.id.img_back:
                onBackPressed();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (handler != null) {
            handler.removeCallbacks(runnable);
        }
    }
}
