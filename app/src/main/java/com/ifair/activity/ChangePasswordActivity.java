package com.ifair.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ifair.MyApplication;
import com.ifair.R;
import com.ifair.api.CloudAPI;
import com.ifair.api.CloudCode;
import com.ifair.listener.OnChangePasswordListener;
import com.ifair.module.BaseResponse;
import com.ifair.myUtil.AppUtil;
import com.ifair.myUtil.MyRegularUtils;
import com.ifair.myUtil.Variable;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 變更密碼
 */
public class ChangePasswordActivity extends BaseActivity implements OnChangePasswordListener {

    @BindView(R.id.txt_title)
    TextView txtTitle;
    @BindView(R.id.img_back)
    ImageView imgBack;
    @BindView(R.id.edt_password)
    EditText edtPassword;
    @BindView(R.id.edt_new_password)
    EditText edtNewPassword;
    @BindView(R.id.edt_confirm_password)
    EditText edtConfirmPassword;
    @BindView(R.id.btn_change_password)
    Button btnChangePassword;
    @BindView(R.id.linear_root)
    LinearLayout linearRoot;
    @BindView(R.id.activity_change_password)
    LinearLayout activityChangePassword;
    @BindView(R.id.img_password_eye)
    ImageView imgPasswordEye;
    @BindView(R.id.img_new_password_eye)
    ImageView imgNewPasswordEye;
    @BindView(R.id.img_password_check_eye)
    ImageView imgPasswordCheckEye;
    private boolean passwordVisible=false;
    private boolean passwordConfirmVisible=false;
    private boolean passwordNewVisible=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        ButterKnife.bind(this);
        adjustUI();
        initParam();
        MyApplication myApplication = (MyApplication) getApplication();
        myApplication.setStartScan(false);
    }

    /**
     * 動態更新UI
     */
    private void adjustUI() {
        getScreenSize(this);
        LinearLayout.LayoutParams linearRootLp = (LinearLayout.LayoutParams) linearRoot.getLayoutParams();
        linearRootLp.width = (int) (width * 0.8);
        linearRoot.setLayoutParams(linearRootLp);
    }

    @Override
    protected void initParam() {

        txtTitle.setText(getString(R.string.setting_change_password));

        cloudAPI = CloudAPI.getInstance();
        cloudAPI.setOnChangePasswordListener(this);
    }

    private boolean checkData() {
        if (AppUtil.editToString(edtPassword).equals("")) {
            alert(getString(R.string.alert_enter_old_password));
            return false;
        } else if (AppUtil.editToString(edtNewPassword).equals("")) {
            alert(getString(R.string.alert_enter_new_password));
            return false;
        } else if (AppUtil.editToString(edtConfirmPassword).equals("")) {
            alert(getString(R.string.alert_enter_confirm_new_password));
            return false;
        } else if (!MyRegularUtils.isPassword(AppUtil.editToString(edtPassword))) {
            alert(getString(R.string.alert_old_password_error_type));
            return false;
        } else if (!MyRegularUtils.isPassword(AppUtil.editToString(edtNewPassword))) {
            alert(getString(R.string.alert_new_password_error_type));
            return false;
        } else if (!MyRegularUtils.isPassword(AppUtil.editToString(edtConfirmPassword))) {
            alert(getString(R.string.alert_confirm_new_password_error_type));
            return false;
        } else if (!AppUtil.editToString(edtNewPassword).equals(AppUtil.editToString(edtConfirmPassword))) {
            alert(getString(R.string.alert_password_no_equals));
            return false;
        }

        return true;
    }

    /**
     * 送出API
     */
    private void sendAPI() {
        getLoginResponse();
        cloudAPI.changePassword(userEmail, AppUtil.editToString(edtPassword), AppUtil.editToString(edtConfirmPassword), this);
    }

    //------------------------------API---------------------------------------//
    @Override
    public void changePasswordResponse(BaseResponse baseResponse, String error) {
        if (error.equals("")) {
            if (baseResponse.getCode() == CloudCode.Code_SUCCESS) {
                new AlertDialog.Builder(this)
                        .setCancelable(false)
                        .setMessage(getString(R.string.alert_re_login))
                        .setNegativeButton(getString(R.string.alert_confirm), (dialog1, which) -> {
                            AppUtil.getSharePrefEditor(this, AppUtil.SHARED_PREF_NAME).clear().commit();
                            Intent logoutIntent = new Intent(Variable.Intent_Finish);
                            sendBroadcast(logoutIntent);
                            finish();
                        }).show();
            } else if (baseResponse.getCode() == CloudCode.Code_Password_Error) {
                Toast.makeText(this, getString(R.string.alert_old_password_error), Toast.LENGTH_SHORT).show();
            }
        } else {
            new AlertDialog.Builder(this)
                    .setCancelable(false)
                    .setMessage(getString(R.string.alert_no_network))
                    .setTitle(getString(R.string.alert_warming))
                    .setNegativeButton(getString(R.string.alert_confirm), (dialog1, which) -> {
                        sendAPI();
                    }).show();
        }
    }

    //------------------------------API---------------------------------------//

    @OnClick({R.id.img_back, R.id.btn_change_password, R.id.activity_change_password,R.id.img_password_eye, R.id.img_new_password_eye, R.id.img_password_check_eye})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                finish();
                break;
            case R.id.btn_change_password:
                hideSoftInput();
                if (checkData()) {
                    sendAPI();
                }
                break;
            case R.id.activity_change_password:
                hideSoftInput();
                break;
            case R.id.img_password_eye:
                int startEditPassword = edtPassword.getSelectionStart();
                int endEditPassword = edtPassword.getSelectionEnd();
                if (!passwordVisible) {
                    edtPassword.setTransformationMethod(new PasswordTransformationMethod());
                    passwordVisible=true;
                    imgPasswordEye.setBackground(getResources().getDrawable(R.drawable.registration_btn_a_visiblegray_1));
                } else {
                    edtPassword.setTransformationMethod(null);
                    passwordVisible=false;
                    imgPasswordEye.setBackground(getResources().getDrawable(R.drawable.registration_btn_a_visiblegray_0));
                }
                edtPassword.setSelection(startEditPassword, endEditPassword);
                break;
            case R.id.img_new_password_eye:
                int startEditPasswordNew = edtNewPassword.getSelectionStart();
                int endEditPasswordNew = edtNewPassword.getSelectionEnd();
                if (!passwordNewVisible) {
                    edtNewPassword.setTransformationMethod(new PasswordTransformationMethod());
                    passwordNewVisible=true;
                    imgNewPasswordEye.setBackground(getResources().getDrawable(R.drawable.registration_btn_a_visiblegray_1));
                } else {
                    edtNewPassword.setTransformationMethod(null);
                    passwordNewVisible=false;
                    imgNewPasswordEye.setBackground(getResources().getDrawable(R.drawable.registration_btn_a_visiblegray_0));
                }
                edtNewPassword.setSelection(startEditPasswordNew, endEditPasswordNew);
                break;
            case R.id.img_password_check_eye:
                int startEditPasswordCheck = edtConfirmPassword.getSelectionStart();
                int endEditPasswordCheck = edtConfirmPassword.getSelectionEnd();
                if (!passwordConfirmVisible) {
                    edtConfirmPassword.setTransformationMethod(new PasswordTransformationMethod());
                    passwordConfirmVisible=true;
                    imgPasswordCheckEye.setBackground(getResources().getDrawable(R.drawable.registration_btn_a_visiblegray_1));
                } else {
                    edtConfirmPassword.setTransformationMethod(null);
                    passwordConfirmVisible=false;
                    imgPasswordCheckEye.setBackground(getResources().getDrawable(R.drawable.registration_btn_a_visiblegray_0));
                }
                edtConfirmPassword.setSelection(startEditPasswordCheck, endEditPasswordCheck);
                break;
        }
    }


}
