package com.ifair.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;
import com.ifair.MyApplication;
import com.ifair.R;
import com.ifair.api.CloudAPI;
import com.ifair.api.CloudCode;
import com.ifair.listener.OnSendLoginListener;
import com.ifair.listener.OnSendNewRegisterListener;
import com.ifair.module.LoginResponse;
import com.ifair.module.NewRegisterResponse;
import com.ifair.myUtil.AppUtil;
import com.ifair.myUtil.LocationUtil;
import com.ifair.myUtil.MyRegularUtils;
import com.orhanobut.logger.Logger;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.ifair.R.string.registered_account_type;
import static com.ifair.R.string.registered_password_type;
import static com.ifair.R.string.registered_name_type;

public class RegisterActivity extends BaseActivity implements OnSendNewRegisterListener, OnSendLoginListener {


    @BindView(R.id.img_back)
    ImageView imgBack;
    @BindView(R.id.txt_title)
    TextView txtTitle;
    @BindView(R.id.btn_register)
    Button btnRegister;
    @BindView(R.id.edit_account)
    EditText editAccount;
    @BindView(R.id.img_account)
    ImageView imgAccount;
    @BindView(R.id.edit_password)
    EditText editPassword;
    @BindView(R.id.img_password)
    ImageView imgPassword;
    @BindView(R.id.edit_password_check)
    EditText editPasswordCheck;
    @BindView(R.id.img_password_check)
    ImageView imgPasswordCheck;
    @BindView(R.id.linear_general)
    LinearLayout linearGeneral;
    @BindView(R.id.text_facebook_name)
    TextView textFacebookName;
    @BindView(R.id.linear_facebbook_register)
    LinearLayout linearFacebbookRegister;
    @BindView(R.id.edit_name)
    EditText editName;
    @BindView(R.id.img_name)
    ImageView imgName;
    @BindView(R.id.edit_birthday)
    EditText editBirthday;
    @BindView(R.id.img_birthday)
    ImageView imgBirthday;
    @BindView(R.id.img_gender)
    ImageView imgGender;
    @BindView(R.id.linear_register_data)
    LinearLayout linearRegisterData;
    @BindView(R.id.activity_register)
    LinearLayout activityRegister;
    @BindView(R.id.linear_gender)
    LinearLayout linearGender;
    @BindView(R.id.linear_male)
    LinearLayout linearMale;
    @BindView(R.id.linear_female)
    LinearLayout linearFemale;
    @BindView(R.id.img_male)
    ImageView imgMale;
    @BindView(R.id.img_female)
    ImageView imgFemale;
    @BindView(R.id.edit_telephone)
    EditText editTelephone;
    @BindView(R.id.img_telephone)
    ImageView imgTelephone;
    @BindView(R.id.edit_area)
    EditText editArea;
    @BindView(R.id.img_area)
    ImageView imgArea;
    @BindView(R.id.img_ifair_icon)
    ImageView imgIfairIcon;
    @BindView(R.id.img_password_eye)
    ImageView imgPasswordEye;
    @BindView(R.id.img_password_check_eye)
    ImageView imgPasswordCheckEye;

    private int gender;//性別 0：女，1：男
    public Boolean m_boolean_ordinary;//是否是一般註冊
    private boolean passwordVisible = false;
    private boolean passwordCheckVisible = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        MyApplication myApplication = (MyApplication) getApplication();
        myApplication.setStartScan(false);
        adjustUI();
        initParam();

    }

    /**
     * 動態設定UI
     */
    private void adjustUI() {
        getScreenSize(this);
        LinearLayout.LayoutParams linearGeneralLp = (LinearLayout.LayoutParams) linearGeneral.getLayoutParams();
        linearGeneralLp.width = (int) (width * 0.8);
        linearGeneral.setLayoutParams(linearGeneralLp);

        LinearLayout.LayoutParams linearFacebbookRegisterLp = (LinearLayout.LayoutParams) linearFacebbookRegister.getLayoutParams();
        linearFacebbookRegisterLp.width = (int) (width * 0.8);
        linearFacebbookRegisterLp.weight = (float) 0.2;
        linearFacebbookRegister.setLayoutParams(linearFacebbookRegisterLp);

        LinearLayout.LayoutParams linearRegisterDataLp = (LinearLayout.LayoutParams) linearRegisterData.getLayoutParams();
        linearRegisterDataLp.width = (int) (width * 0.8);
        linearRegisterData.setLayoutParams(linearRegisterDataLp);

        editArea.setText("");
        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        m_boolean_ordinary = bundle.getBoolean("m_boolean_ordinary");
        //如果是FB註冊，帳號與密碼隱藏，顯示FB
        if (!m_boolean_ordinary) {
            //帳號：m_facebook_account(email 或 FB_ID)，密碼：m_facebook_password(由帳號加密)，生日：m_facebook_birthday，姓名：m_facebook_name
            editAccount.setText(bundle.getString("m_facebook_account"));
            editPassword.setText(bundle.getString("m_facebook_password"));
            editName.setText(bundle.getString("m_facebook_name"));
            editBirthday.setText(bundle.getString("m_facebook_birthday"));
            textFacebookName.setText(getString(R.string.registered_hollow, bundle.getString("m_facebook_name")));
            if (editAccount.getText().toString().length() > 0)
                imgName = setImageType(imgName, true);
            linearGeneral.setVisibility(View.GONE);
            imgIfairIcon.getLayoutParams().width = (int) getResources().getDimension(R.dimen.register_ifair_icon_size);
            imgIfairIcon.getLayoutParams().height = (int) getResources().getDimension(R.dimen.register_ifair_icon_size);
            linearFacebbookRegister.setVisibility(View.VISIBLE);
        }

        LocationManager mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        boolean isGPSEnable = mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        boolean isNetworkEnable = mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        if (!isGPSEnable && !isNetworkEnable) {

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(getString(R.string.registered_open_gps))
                    .setCancelable(false)
                    .setPositiveButton(getString(R.string.alert_confirm),
                            (dialog1, which) -> startActivityForResult(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS), 1));
            builder.show();
        } else {
            getArea();
        }

        editAccount.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    editAccount.setHint(registered_account_type);
                } else {
                    editAccount.setHint("");
                }
//                if (!hasFocus) {

//                    if (MyRegularUtils.isEmail(editAccount.getText().toString())) {
//                        imgAccount = setImageType(imgAccount, true);
//                    } else {
                        //Alert
//                        imgAccount = setImageType(imgAccount, false);
//                    }
//                    setButtonColor();
//                }
            }
        });

        editPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    editPassword.setHint(registered_password_type);
                } else {
                    editPassword.setHint("");
                }
//                if (!hasFocus) {
//                    if (MyRegularUtils.isPassword(editPassword.getText().toString())) {
//                        imgPassword = setImageType(imgPassword, true);
//                    } else {
//                        //Alert
//                        imgPassword = setImageType(imgPassword, false);
//                    }
//                    setButtonColor();
//                }
            }
        });
//
//        editPasswordCheck.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View v, boolean hasFocus) {
//                if (!hasFocus) {
//                    if (MyRegularUtils.isPassword(editPasswordCheck.getText().toString())) {
//                        Boolean bol = editPassword.getText().toString().equals(editPasswordCheck.getText().toString()); // MyRegularUtils.isMatch(editPassword.getText().toString(),editPasswordCheck.getText().toString());
//                        if (bol) {
//                            imgPasswordCheck = setImageType(imgPasswordCheck, true);
//                        } else {
//                            //Alert
//                            imgPasswordCheck = setImageType(imgPasswordCheck, true);
//                        }
//                    } else {
//                        //Alert
//                        imgPasswordCheck = setImageType(imgPasswordCheck, false);
//                    }
//                    setButtonColor();
//                }
//            }
//        });
//
        editName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    editName.setHint(registered_name_type);
                } else {
                    editName.setHint("");
                }
//                if (!hasFocus) {
//                    if (editName.getText().length() != 0 && !editName.getText().equals("")) {
//                        imgName = setImageType(imgName, true);
//                    } else {
//                        imgName = setImageType(imgName, false);
//                    }
//                    hideSoftInput();
//                    setButtonColor();
//                    editBirthday.setFocusable(true);
//                }
            }
        });
//
//        editTelephone.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View v, boolean hasFocus) {
//                if (!hasFocus) {
//                    if (MyRegularUtils.isTel(editTelephone.getText().toString())) {
//                        imgTelephone = setImageType(imgTelephone, true);
//                    } else {
//                        imgTelephone = setImageType(imgTelephone, false);
//                    }
//                    hideSoftInput();
//                    setButtonColor();
//                }
//            }
//        });
//
//        editTelephone.setOnKeyListener(new View.OnKeyListener() {
//            @Override
//            public boolean onKey(View v, int keyCode, KeyEvent event) {
//                if (keyCode == event.KEYCODE_ENTER) {
//                    setButtonColor();
//                }
//                return false;
//            }
//        });

    }

    @Override
    protected void initParam() {
        gender = 0;
        txtTitle.setText(R.string.login_register);
        editAccount.isFocusable();
        editBirthday.setLongClickable(false);

        editAccount.addTextChangedListener(new mTextWatcher(editAccount));
        editPassword.addTextChangedListener(new mTextWatcher(editPassword));
        editPasswordCheck.addTextChangedListener(new mTextWatcher(editPasswordCheck));
        editName.addTextChangedListener(new mTextWatcher(editName));
        editTelephone.addTextChangedListener(new mTextWatcher(editTelephone));

    }

    /**
     * 輸入框監聽器
     */
    private class mTextWatcher implements TextWatcher {

        private View view;

        private mTextWatcher(View view) {
            this.view = view;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            setButtonColor();
            switch (view.getId()) {
                case R.id.edit_account:
                    if (MyRegularUtils.isEmail(editAccount.getText().toString())) {
                        imgAccount = setImageType(imgAccount, true);
                    } else {
                        imgAccount = setImageType(imgAccount, false);
                    }
                    break;
                case R.id.edit_password:
                    if (MyRegularUtils.isPassword(editPassword.getText().toString())) {
                        imgPassword = setImageType(imgPassword, true);
                    } else {
                        //Alert
                        imgPassword = setImageType(imgPassword, false);
                    }
                    break;
                case R.id.edit_password_check:
                    if (MyRegularUtils.isPassword(editPasswordCheck.getText().toString())) {
                        if (editPassword.getText().toString().equals(editPasswordCheck.getText().toString())) {
                            imgPasswordCheck = setImageType(imgPasswordCheck, true);
                        } else {
                            imgPasswordCheck = setImageType(imgPasswordCheck, false);
                        }
                    } else {
                        imgPasswordCheck = setImageType(imgPasswordCheck, false);
                    }
                    break;
                case R.id.edit_name:
                    if (!Objects.equals(editName.getText().toString(), "")) {
                        imgName = setImageType(imgName, true);
                    } else {
                        imgName = setImageType(imgName, false);
                    }
                    break;
                case R.id.edit_telephone:
                    if (MyRegularUtils.isTel(editTelephone.getText().toString())) {
                        imgTelephone = setImageType(imgTelephone, true);
                    } else {
                        imgTelephone = setImageType(imgTelephone, false);
                    }
                    break;
            }

        }
    }






    /**
     * 依資料是否正確顯示圖示
     */
    private ImageView setImageType(ImageView imgV, Boolean m_boolean) {
        imgV.setVisibility(View.VISIBLE);

        if (!m_boolean)
            imgV.setBackgroundResource(R.drawable.ic_error);
        else
            imgV.setBackgroundResource(R.drawable.ic_check);
        return imgV;
    }

    /**
     * 送出註冊
     */
    private void registerAPI() {
        cloudAPI = CloudAPI.getInstance();
        cloudAPI.setOnSendNewRegisterListener(this);
        String lat = "0";
        String lon = "0";
        if (LocationUtil.getLocation() != null) {
            Location location = LocationUtil.getLocation();
            lat = String.valueOf(location.getLatitude());
            lon = String.valueOf(location.getLongitude());
        }
        if (m_boolean_ordinary)
            //一般註冊(非第三方 register_type="0")
            cloudAPI.SendRegister(editAccount.getText().toString(), editPassword.getText().toString(), editName.getText().toString(), editBirthday.getText().toString(), String.valueOf(gender), "0", editTelephone.getText().toString(), editArea.getText().toString(), lat, lon, this);
        else
            //FB註冊(是第三方 register_type="1")
            cloudAPI.SendRegister(editAccount.getText().toString(), editPassword.getText().toString(), editName.getText().toString(), editBirthday.getText().toString(), String.valueOf(gender), "1", editTelephone.getText().toString(), editArea.getText().toString(), lat, lon, this);
    }


    /**
     * 設定註冊按鈕顏色，未輸入資料為淡色(color_yellow_gray)，有輸入資料為黃色(color_yellow)
     */
    private void setButtonColor() {
        if (m_boolean_ordinary && editAccount.getText().length() != 0 && editPassword.getText().length() != 0 && editPasswordCheck.getText().length() != 0) {
            btnRegister.setEnabled(true);
        } else if (!m_boolean_ordinary) {
            btnRegister.setEnabled(true);
        }
    }

    /**
     * 取得GPS
     */
    private void getArea() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION) ||
                    ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_COARSE_LOCATION)) {
                Snackbar.make(activityRegister, getString(R.string.register_need_permission), Snackbar.LENGTH_INDEFINITE)
                        .setAction(getString(R.string.alert_confirm), view1 -> {
                            ActivityCompat.requestPermissions(this, new String[]{
                                    Manifest.permission.ACCESS_FINE_LOCATION,
                                    Manifest.permission.ACCESS_COARSE_LOCATION}, 0);

                        }).show();
            } else {
                ActivityCompat.requestPermissions(this, new String[]{
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION}, 0);
            }
        } else {

            editArea.setText(LocationUtil.getCurrentLocation(this));

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 0 &&
                grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
            getArea();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == 1) {
            switch (requestCode) {
                case 1:
                    getArea();
                    break;
            }
        }
    }

    //-------------------------------API---------------------------------//

    @Override
    public void onSendLoginMessage(String json, String error) {
        if (error == null) {

            AppUtil.getSharePrefEditor(RegisterActivity.this, AppUtil.SHARED_PREF_NAME).putString("loginResponse", json).commit();
            try {
                AppUtil.getSharePrefEditor(RegisterActivity.this, AppUtil.SHARED_PREF_NAME).putString("userPassword", Base64.encodeToString(AppUtil.EncryptAES(editPassword.getText().toString().getBytes("UTF-8")), Base64.DEFAULT)).commit();
            } catch (Exception e) {
                e.printStackTrace();
            }
            goPage(MainActivity.class, true);
        }
    }

    @Override
    public void onSendNewRegisterMessage(NewRegisterResponse newRegisterResponse, String error) {
        if (error.equals("")) {
            if (newRegisterResponse.getCode() == CloudCode.Code_SUCCESS) {
                Toast.makeText(this, "註冊成功", Toast.LENGTH_SHORT).show();
                String token = FirebaseInstanceId.getInstance().getToken();
                cloudAPI.setOnSendLoginListener(this);
                cloudAPI.sendLogin(AppUtil.editToString(editAccount), AppUtil.editToString(editPassword), token == null ? "" : token, this);
            } else if (newRegisterResponse.getCode() == CloudCode.Code_Account_Registered) {
                alert(getString(R.string.registered_account_registere));
            }
        }
    }

    //-------------------------------API---------------------------------//

    @OnClick({R.id.img_back, R.id.btn_register, R.id.edit_birthday, R.id.linear_male, R.id.linear_female, R.id.img_password_check_eye, R.id.img_password_eye})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                goPage(LoginActivity.class, true);
                break;
            case R.id.btn_register:
                imgAccount = setImageType(imgAccount, true);
                imgPassword = setImageType(imgPassword, true);
                imgPasswordCheck = setImageType(imgPasswordCheck, true);
                imgName = setImageType(imgName, true);
                imgBirthday = setImageType(imgBirthday, true);
                imgGender = setImageType(imgGender, true);
                hideSoftInput();
                if (m_boolean_ordinary) {
                    //一般會員
                    //資料是否完整
                    if (MyRegularUtils.isEmail(editAccount.getText().toString()) && editAccount.getText().length() != 0 && editPassword.getText().length() != 0 && editPasswordCheck.getText().length() != 0) {
                        //帳號格式是否正確及確認密碼與密碼是否一樣
                        if (editPasswordCheck.getText().toString().equals(editPassword.getText().toString())) { // MyRegularUtils.isMatch(editPassword.getText().toString(),editPasswordCheck.getText().toString());
                            // 接查詢是否已註冊之一般會員
                            registerAPI();
                        } else {
                            editPasswordCheck.requestFocus();
                            imgPasswordCheck = setImageType(imgPasswordCheck, false);
                            alert("", getString(R.string.registered_data));
                        }
                    } else {
                        if (editAccount.getText().toString().length() == 0 || MyRegularUtils.isEmail(editAccount.getText().toString()))
                            imgAccount = setImageType(imgAccount, false);

                        if (editPassword.getText().toString().length() == 0 || MyRegularUtils.isPassword(editPassword.getText().toString()))
                            imgPassword = setImageType(imgPassword, false);

                        if (editPasswordCheck.getText().toString().length() == 0 || editPasswordCheck.getText().toString().equals(editPassword.getText().toString()) || MyRegularUtils.isPassword(editPasswordCheck.getText().toString()))
                            imgPasswordCheck = setImageType(imgPasswordCheck, false);

                        if (editName.getText().toString().length() == 0)
                            imgName = setImageType(imgName, false);

                        if (editBirthday.getText().toString().length() == 0)
                            imgBirthday = setImageType(imgBirthday, false);

                        if (gender == 2)
                            imgGender = setImageType(imgGender, false);
                        alert("", getString(R.string.registered_data));
                    }
                } else {
                    //FACEBOOK會員
                    registerAPI();

                }
                break;
            case R.id.edit_birthday:
                //彈出日歷讓使用者選擇
                final Calendar c = Calendar.getInstance();

                new DatePickerDialog
                        (RegisterActivity.this, new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int day) {
                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
                                String strDateFormat = sdf.format(new Date(year + "/" + (month + 1) + "/" + day));
                                editBirthday.setText(strDateFormat);
                                imgBirthday = setImageType(imgBirthday, true);
                                setButtonColor();
                            }
                        }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show();
                break;
            case R.id.linear_male:
                gender = 1;
                imgMale.setImageResource(R.drawable.registration_btn_a_choose_1);
                imgFemale.setImageResource(R.drawable.registration_btn_a_choose_0);
                imgGender = setImageType(imgGender, true);
                setButtonColor();
                break;
            case R.id.linear_female:
                gender = 0;
                imgFemale.setImageResource(R.drawable.registration_btn_a_choose_1);
                imgMale.setImageResource(R.drawable.registration_btn_a_choose_0);
                imgGender = setImageType(imgGender, true);
                setButtonColor();
                break;
            case R.id.img_password_eye:
                int startEditPassword = editPassword.getSelectionStart();
                int endEditPassword = editPassword.getSelectionEnd();
                if (!passwordVisible) {
                    editPassword.setTransformationMethod(new PasswordTransformationMethod());
                    passwordVisible = true;
                    imgPasswordEye.setBackground(getResources().getDrawable(R.drawable.registration_btn_a_visible_1));
                } else {
                    editPassword.setTransformationMethod(null);
                    passwordVisible = false;
                    imgPasswordEye.setBackground(getResources().getDrawable(R.drawable.registration_btn_a_visible_0));

                }
                editPassword.setSelection(startEditPassword, endEditPassword);
                break;
            case R.id.img_password_check_eye:
                int startEditPasswordCheck = editPasswordCheck.getSelectionStart();
                int endEditPasswordCheck = editPasswordCheck.getSelectionEnd();
                if (!passwordCheckVisible) {
                    editPasswordCheck.setTransformationMethod(new PasswordTransformationMethod());
                    passwordCheckVisible = true;
                    imgPasswordCheckEye.setBackground(getResources().getDrawable(R.drawable.registration_btn_a_visible_1));
                } else {
                    editPasswordCheck.setTransformationMethod(null);
                    passwordCheckVisible = false;
                    imgPasswordCheckEye.setBackground(getResources().getDrawable(R.drawable.registration_btn_a_visible_0));
                }
                editPasswordCheck.setSelection(startEditPasswordCheck, endEditPasswordCheck);
                break;
        }
    }



}
