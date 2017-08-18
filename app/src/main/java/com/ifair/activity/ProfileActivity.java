package com.ifair.activity;

import android.app.DatePickerDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.ifair.R;
import com.ifair.api.CloudAPI;
import com.ifair.api.CloudCode;
import com.ifair.listener.OnSendEditUserInformationListener;
import com.ifair.module.EditUserInformationResponse;
import com.ifair.myUtil.AppUtil;
import com.ifair.myUtil.LocationUtil;
import com.ifair.myUtil.MyRegularUtils;
import com.ifair.myUtil.MyTimeUtils;
import com.ifair.myUtil.Variable;
import com.orhanobut.logger.Logger;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * 會員資料
 */
public class ProfileActivity extends BaseNFcActivity implements OnSendEditUserInformationListener {


    @BindView(R.id.txt_title)
    TextView txtTitle;
    @BindView(R.id.img_back)
    ImageView imgBack;
    @BindView(R.id.btn_save)
    Button btnSave;
    @BindView(R.id.edt_account)
    EditText edit_account;
    @BindView(R.id.rl_account)
    RelativeLayout rl_account;
    @BindView(R.id.edt_name)
    EditText edtName;
    @BindView(R.id.rl_name)
    RelativeLayout rl_name;
    @BindView(R.id.edt_birthday)
    EditText edt_birthday;
    @BindView(R.id.rl_birthday)
    RelativeLayout rl_birthday;
    @BindView(R.id.img_male)
    ImageView imgMale;
    @BindView(R.id.linear_male)
    LinearLayout linearMale;
    @BindView(R.id.img_female)
    ImageView imgFemale;
    @BindView(R.id.linear_female)
    LinearLayout linearFemale;
    @BindView(R.id.linear_gender)
    LinearLayout linearGender;
    @BindView(R.id.activity_profile)
    LinearLayout activityProfile;
    @BindView(R.id.edt_phone)
    EditText edtPhone;
    @BindView(R.id.rl_phone)
    RelativeLayout relativeLayoutPhone;
    @BindView(R.id.edt_area)
    EditText edtArea;
    @BindView(R.id.rl_area)
    RelativeLayout rl_area;
    @BindView(R.id.img_name)
    ImageView imgName;
    @BindView(R.id.img_telephone)
    ImageView imgTelephone;
    //記錄被選的性別 0：女，1：男
    private int sex;
    //廣播接收器
    private NfcBroadCast nfcBroadCast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);


        adjustUI();
        initParam();
    }

    /**
     * 廣播接收器
     */
    class NfcBroadCast extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(Variable.Intent_Close_NFC_Activity)) finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(nfcBroadCast);
    }

    /**
     * 動態設定UI
     */
    private void adjustUI() {
        getScreenSize(this);

        LinearLayout.LayoutParams rl_accountLp = (LinearLayout.LayoutParams) rl_account.getLayoutParams();
        rl_accountLp.width = (int) (width * 0.8);
        rl_account.setLayoutParams(rl_accountLp);

        LinearLayout.LayoutParams linearNameLp = (LinearLayout.LayoutParams) rl_name.getLayoutParams();
        linearNameLp.width = (int) (width * 0.8);
        rl_name.setLayoutParams(linearNameLp);

        LinearLayout.LayoutParams rl_birthdayLp = (LinearLayout.LayoutParams) rl_birthday.getLayoutParams();
        rl_birthdayLp.width = (int) (width * 0.8);
        rl_birthday.setLayoutParams(rl_birthdayLp);

        LinearLayout.LayoutParams linearGenderLp = (LinearLayout.LayoutParams) linearGender.getLayoutParams();
        linearGenderLp.width = (int) (width * 0.8);
        linearGender.setLayoutParams(linearGenderLp);

        LinearLayout.LayoutParams relativeLayoutPhoneLp = (LinearLayout.LayoutParams) relativeLayoutPhone.getLayoutParams();
        relativeLayoutPhoneLp.width = (int) (width * 0.8);
        relativeLayoutPhone.setLayoutParams(relativeLayoutPhoneLp);

        LinearLayout.LayoutParams rl_areaLp = (LinearLayout.LayoutParams) rl_area.getLayoutParams();
        rl_areaLp.width = (int) (width * 0.8);
        rl_area.setLayoutParams(rl_areaLp);


    }

    @Override
    protected void initParam() {

        nfcBroadCast = new NfcBroadCast();
        registerReceiver(nfcBroadCast, new IntentFilter(Variable.Intent_Close_NFC_Activity));

        getLoginResponse();
        txtTitle.setText(getString(R.string.setting_profile_title));
        edit_account.setText(userEmail);
        Logger.d(loginResponse.getData().get(0).getBirthday());
        edt_birthday.setText(MyTimeUtils.timeToString1(loginResponse.getData().get(0).getBirthday()));
        edt_birthday.setLongClickable(false);
        changeGender(Integer.parseInt(loginResponse.getData().get(0).getSex()));
        edtName.setText(loginResponse.getData().get(0).getName());
        edtName.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                if (edtName.getText().length() != 0 && !edtName.getText().equals("")) {
                    imgName = setImageType(imgName, true);
                } else {
                    imgName = setImageType(imgName, false);
                }
                hideSoftInput();
            }
        });
        edtArea.setText(loginResponse.getData().get(0).getArea());
        edtPhone.setText(loginResponse.getData().get(0).getPhone());
        edtPhone.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if (MyRegularUtils.isTel(edtPhone.getText().toString())) {
                        imgTelephone = setImageType(imgTelephone, true);
                    } else {
                        imgTelephone = setImageType(imgTelephone, false);
                    }
                    hideSoftInput();
                }
            }
        });

    }

    /**
     * 切換性別狀態
     *
     * @param type 0: 女生, 1: 男生
     */
    private void changeGender(int type) {
        if (type == 1) {
            sex = 1;
            imgMale.setImageResource(R.drawable.ic_check_circle);
            imgFemale.setImageResource(R.drawable.ic_panorama_fish_eye);
        } else if (type == 0) {
            sex = 0;
            imgFemale.setImageResource(R.drawable.ic_check_circle);
            imgMale.setImageResource(R.drawable.ic_panorama_fish_eye);
        }
    }

    /**
     * 送出API
     */
    private void editMemberDetailAPI() {
        cloudAPI = CloudAPI.getInstance();
        cloudAPI.setOnSendEditUserInformationListener(this);
        String lat = "0";
        String lon = "0";
        if (LocationUtil.getLocationFirstTime(this) != null){
            Location location = LocationUtil.getLocationFirstTime(this);
            lat=String.valueOf(location.getLatitude());
            lon=String.valueOf(location.getLongitude());
        }
        cloudAPI.SendEditUserInformation(edit_account.getText().toString(), edtName.getText().toString(), edt_birthday.getText().toString(), String.valueOf(sex), edtPhone.getText().toString(), edtArea.getText().toString(),lat,lon, this);

    }

    @Override
    public void onSendEditUserInformationMessage(String json, String error) {
        if (error.equals("")) {
            Gson gson = new Gson();
            EditUserInformationResponse editUserInformationResponse = gson.fromJson(json, EditUserInformationResponse.class);
            if (editUserInformationResponse.getCode() == CloudCode.Code_SUCCESS) {
                AppUtil.getSharePrefEditor(this, AppUtil.SHARED_PREF_NAME).putString("loginResponse", json).commit();
                Toast.makeText(this, getString(R.string.profile_edit_success), Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }


    @OnClick({R.id.img_back, R.id.btn_save, R.id.edt_birthday, R.id.linear_male, R.id.linear_female, R.id.activity_profile})
    public void onClick(View view) {
        hideSoftInput();
        switch (view.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.btn_save:
                editMemberDetailAPI();
                break;
            case R.id.edt_birthday:
                String[] splitDate = MyTimeUtils.splitDate(edt_birthday.getText().toString(), 2);
                new DatePickerDialog(this, (view1, year, month, dayOfMonth) -> {
                    if (MyTimeUtils.compareDate(MyTimeUtils.getCurrentDate("yyyy/MM/dd"), MyTimeUtils.formatToDate(year, month + 1, dayOfMonth), "yyyy/MM/dd") == -1) {
                        alert(getString(R.string.profile_data_format_error));
                    } else {
                        edt_birthday.setText(MyTimeUtils.formatToDate(year, month + 1, dayOfMonth));
                    }
                }, Integer.valueOf(splitDate[0]), Integer.valueOf(splitDate[1]) - 1, Integer.valueOf(splitDate[2])).show();
                break;
            case R.id.linear_male:
                changeGender(1);
                break;
            case R.id.linear_female:
                changeGender(0);
                break;
            case R.id.activity_profile:
                hideSoftInput();
                break;
        }
    }

    /*
    依資料是否正確顯示圖示
    */
    private ImageView setImageType(ImageView imgV, Boolean m_boolean) {
        imgV.setVisibility(View.VISIBLE);

        if (!m_boolean) {
            imgV.setBackgroundResource(R.drawable.ic_error);
        } else
            imgV.setBackgroundResource(R.drawable.ic_check);
        return imgV;
    }


}
