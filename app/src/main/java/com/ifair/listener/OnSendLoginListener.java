package com.ifair.listener;

import com.ifair.module.LoginResponse;

/**
 * 監聽雲端回傳值
 */

public interface OnSendLoginListener {
    void onSendLoginMessage(String json, String error);
}
