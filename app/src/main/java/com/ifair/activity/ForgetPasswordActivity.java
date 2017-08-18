package com.ifair.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ifair.R;
import com.ifair.api.CloudAPI;
import com.ifair.api.CloudCode;
import com.ifair.listener.OnSendForgetPasswordListener;
import com.ifair.module.ForgetPasswordResponse;
import com.ifair.myUtil.MyRegularUtils;

import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ForgetPasswordActivity extends BaseActivity implements OnSendForgetPasswordListener {

    @BindView(R.id.img_back)
    ImageView imgBack;
    @BindView(R.id.txt_title)
    TextView txtTitle;
    @BindView(R.id.edit_email)
    EditText editEmail;
    @BindView(R.id.btn_sent_letter)
    Button btnSentLetter;
    @BindView(R.id.linear_forgetpassword_email)
    LinearLayout linearForgetpasswordEmail;
    @BindView(R.id.linear_forgetpassword_sent_email)
    LinearLayout linearForgetpasswordSentEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        ButterKnife.bind(this);
        adjustUI();
    }

    @Override
    protected void initParam() {

    }

    @OnClick({R.id.img_back, R.id.btn_sent_letter})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                goPage(LoginActivity.class, true);
                break;
            case R.id.btn_sent_letter:

                if (MyRegularUtils.isEmail(editEmail.getText().toString())) {
                    ForgetPasswordAPI();
                } else
                    alert("", getString(R.string.forgetpassword_account_type));
                break;
        }
    }

    /**
     * 動態設定UI
     */
    private void adjustUI() {
        getScreenSize(this);
        LinearLayout.LayoutParams linearForgetpasswordEmailLp = (LinearLayout.LayoutParams) linearForgetpasswordEmail.getLayoutParams();
        linearForgetpasswordEmailLp.width = (int) (width * 0.8);
        linearForgetpasswordEmail.setLayoutParams(linearForgetpasswordEmailLp);

        LinearLayout.LayoutParams linearForgetpasswordSentEmailLp = (LinearLayout.LayoutParams) linearForgetpasswordSentEmail.getLayoutParams();
        linearForgetpasswordSentEmailLp.width = (int) (width * 0.8);
        linearForgetpasswordSentEmail.setLayoutParams(linearForgetpasswordSentEmailLp);

        txtTitle.setText(getString(R.string.forgetpassword_title));
    }

    /*
    忘記密碼API
     */
    private void ForgetPasswordAPI() {
        Boolean m_boolean_have_account = true;//接驗證帳號API的結果
        cloudAPI = CloudAPI.getInstance();
        cloudAPI.setOnSendForgetPasswordListener(this);
        cloudAPI.SendForgetPassword(editEmail.getText().toString(), this);
    }

    @Override
    public void OnSendForgetPasswordMessage(ForgetPasswordResponse forgetPasswordResponse, String error) {
        if (forgetPasswordResponse != null) {
            if (forgetPasswordResponse.getCode() == CloudCode.Code_SUCCESS) {
                AlertDialogShow("", getString(R.string.forgetpassword_sent_letter));
            } else if (forgetPasswordResponse.getCode() == CloudCode.Code_No_Account) {
                alert("", getString(R.string.forgetpassword_no_account));
            }
        }
    }

    private void AlertDialogShow(String title, String mesg) {
        AlertDialog.Builder ad = new AlertDialog.Builder(this);
        ad.setTitle(title);
        ad.setMessage(mesg);
        ad.setNegativeButton(R.string.alert_confirm, (dialog1, which) -> {
            //不做任何事情 直接關閉對話 方塊
            goPage(LoginActivity.class, true);
        });
        ad.show();
    }
}
