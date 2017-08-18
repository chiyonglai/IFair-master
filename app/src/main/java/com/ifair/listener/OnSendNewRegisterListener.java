package com.ifair.listener;

import com.ifair.module.NewRegisterResponse;

/**
 * 註冊帳號監聽
 */

public interface OnSendNewRegisterListener {
    void onSendNewRegisterMessage(NewRegisterResponse newRegistenerResponse,String error);
}
