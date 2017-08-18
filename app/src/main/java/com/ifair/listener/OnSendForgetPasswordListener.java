package com.ifair.listener;

import com.ifair.module.ForgetPasswordResponse;

/**
 *忘記密碼監聽
 */

public interface OnSendForgetPasswordListener {
    void OnSendForgetPasswordMessage(ForgetPasswordResponse forgetPasswordResponse, String error);
}
