package com.ifair.listener;

import com.ifair.module.EditUserInformationResponse;

/**
 * 編輯帳號回傳監聽
 */

public interface OnSendEditUserInformationListener {
    void onSendEditUserInformationMessage(String json, String error);
}
