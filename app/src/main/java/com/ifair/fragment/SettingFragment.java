package com.ifair.fragment;


import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import com.ifair.api.CloudAPI;
import com.ifair.R;
import com.ifair.activity.AboutActivity;
import com.ifair.activity.ChangePasswordActivity;
import com.ifair.activity.IntroductionNfcActivity;
import com.google.gson.Gson;
import com.ifair.api.CloudCode;
import com.ifair.activity.ProfileActivity;
import com.ifair.myUtil.AppUtil;
import com.ifair.myUtil.Variable;
import com.ifair.listener.OnGetPushSWITCListener;
import com.ifair.module.PushSWITCResponse;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 設定頁
 */
public class SettingFragment extends BaseFragment implements OnGetPushSWITCListener{

    public static final String TAG = "SettingFragment";
    @BindView(R.id.txt_title)
    TextView txtTitle;
    @BindView(R.id.switch_noise)
    Switch switchNoise;
    @BindView(R.id.switch_notification)
    Switch switchNotification;
    @BindView(R.id.rela_member)
    RelativeLayout relaMember;
    @BindView(R.id.rela_introduction)
    RelativeLayout relaIntroduction;
    @BindView(R.id.rela_about)
    RelativeLayout relaAbout;
    @BindView(R.id.rela_logout)
    RelativeLayout relaLogout;
    @BindView(R.id.rela_change_password)
    RelativeLayout relaChangePassword;
    @BindView(R.id.txt_member_status)
    TextView txtMemberStatus;
    @BindView(R.id.switch_shock)
    Switch switchShock;

    //資料
    //private List<PushSWITCResponse.DataBean> dataBeanList;

    public static SettingFragment getInstance() {
        return new SettingFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setting, container, false);
        ButterKnife.bind(this, view);

        initParam();
        return view;
    }

    @Override
    protected void initParam() {
        getLoginResponse();
        //需判斷是否有登入過
        if (userEmail.equals("")) {
            relaChangePassword.setVisibility(View.GONE);
            relaMember.setVisibility(View.GONE);
            txtMemberStatus.setText(getString(R.string.login_register));
        }
        txtTitle.setText(getString(R.string.setting_title));
        //判斷是否開啟聲音
        int nfcNoise = AppUtil.getSharedPref(getActivity(), AppUtil.SHARED_PREF_NAME).getInt(Variable.Nfc_noise, 1);
        switchNoise.setChecked(nfcNoise == 1);
        //判斷是否有開啟震動
        int nfcVibrate = AppUtil.getSharedPref(getActivity(), AppUtil.SHARED_PREF_NAME).getInt(Variable.Nfc_vibrate, 1);
        switchShock.setChecked(nfcVibrate == 1);

        //dataBeanList = new ArrayList<>();
        getAPI();
    }
    /**
     * 獲得Push開關
     */
    private void getAPI() {
        cloudAPI = CloudAPI.getInstance();
        cloudAPI.setOnGetPushSWITCListener(this);
        cloudAPI.getPushSWITC(userEmail, getActivity());
    }
    /**
     * 設定開關Push功能
     */
    private void setAPI(String on_or_off) {
        cloudAPI = CloudAPI.getInstance();
        cloudAPI.setPushSWITC(userEmail, on_or_off, getActivity());
    }
    /**
     * 更新NFC廣播設定
     */
    private void sendUpdateNfcBroadCastSetting() {
        Intent nfcIntent = new Intent(Variable.Intent_NFC);
        getActivity().sendBroadcast(nfcIntent);
    }

    @Override
    public void onGetPushSWITCMessage(String json, String error) {
        if (error.equals("")) {
            Gson gson = new Gson();
            PushSWITCResponse pushSWITCResponse = gson.fromJson(json, PushSWITCResponse.class);
            if(pushSWITCResponse.getData().get(0).getIsPush().equals("1")){
                switchNotification.setChecked(true);
            } else {
                switchNotification.setChecked(false);
            }

        }
    }

    @OnClick({R.id.switch_noise, R.id.switch_notification, R.id.rela_member, R.id.rela_introduction, R.id.rela_about,
            R.id.rela_logout, R.id.rela_change_password, R.id.switch_shock})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.switch_noise:
                if (switchNoise.isChecked()) {
                    AppUtil.getSharePrefEditor(getActivity(), AppUtil.SHARED_PREF_NAME).putInt(Variable.Nfc_noise, 1).commit();
                } else {
                    AppUtil.getSharePrefEditor(getActivity(), AppUtil.SHARED_PREF_NAME).putInt(Variable.Nfc_noise, 0).commit();
                }
                sendUpdateNfcBroadCastSetting();
                break;
            case R.id.switch_notification:
                //設定開關Push功能
                if (switchNotification.isChecked()){
                    setAPI("1");
                }else{
                    setAPI("0");
                }
                break;
            case R.id.rela_member:
                goToActivity(ProfileActivity.class, null);
                break;
            case R.id.rela_introduction:
                goToActivity(IntroductionNfcActivity.class, null);
                break;
            case R.id.rela_about:
                goToActivity(AboutActivity.class, null);
                break;
            case R.id.rela_logout:
                getLoginResponse();
                AppUtil.getSharePrefEditor(getActivity(), AppUtil.SHARED_PREF_NAME).clear().commit();
                if (loginResponse.getData().get(0).getRegister_type().equals("0"))
                    AppUtil.getSharePrefEditor(getActivity(), AppUtil.SHARED_PREF_NAME).putString("lastUserAccount", loginResponse.getData().get(0).getEmail()).commit();
                Intent logoutIntent = new Intent(Variable.Intent_Finish);
                getActivity().sendBroadcast(logoutIntent);
                break;
            case R.id.rela_change_password:
                if (loginResponse.getData().get(0).getRegister_type().equals("0"))
                    goToActivity(ChangePasswordActivity.class, null);
                else
                    alert("第三方登入，不能修改密碼");
                break;
            case R.id.switch_shock:
                if (switchShock.isChecked()) {
                    AppUtil.getSharePrefEditor(getActivity(), AppUtil.SHARED_PREF_NAME).putInt(Variable.Nfc_vibrate, 1).commit();
                } else {
                    AppUtil.getSharePrefEditor(getActivity(), AppUtil.SHARED_PREF_NAME).putInt(Variable.Nfc_vibrate, 0).commit();
                }
                sendUpdateNfcBroadCastSetting();
                break;
        }
    }
}
